package com.universum.service.apigateway.config;

import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.universum.service.apigateway.security.JWTAuthenticationManager;
import com.universum.service.apigateway.security.JWTTokenAuthenticationConverter;
import com.universum.service.apigateway.security.JWTTokenProvider;
import com.universum.service.apigateway.service.LoadBalancedUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String[] AUTH_WHITELIST = {"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/authenticate/**", "/favicon.ico"};
	
	@Autowired
	private LoadBalancedUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();
        
        // Set session management to stateless
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        
        // Set unauthorized requests exception handler
        http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
            log.error("Unauthorized request - {}", ex.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }).and();
        
        // Set permissions on endpoints
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        						// Actuator endpoints must be publicly accessible
        						.antMatchers("/actuator/**").permitAll()
        						// All JS, CSS & HTML pages should be publicly accessible
        						.antMatchers(AUTH_WHITELIST).permitAll()
        						// User with role SUPER_ADMIN has all permission
        						//.antMatchers("/api/**").hasAuthority("ROLE_SUPER_ADMIN")
        						// Our private endpoints
        		                .anyRequest().authenticated();
        
        // Add JWT token filter
		http.addFilterBefore(webFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	// Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
	
	@Bean
    public Filter webFilter() {
		AuthenticationFilter authenticationWebFilter = new AuthenticationFilter(authenticationManagerBean(), new JWTTokenAuthenticationConverter(new JWTTokenProvider()));
        authenticationWebFilter.setRequestMatcher(request -> {
        	return Collections.list(request.getHeaderNames()).stream().filter(h -> h.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)).findFirst().map(h -> {return true;}).orElse(false);
        });
        authenticationWebFilter.setSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
        	log.info("JWT token validated successfully.");
        });
        return authenticationWebFilter;
    }
	
	@Override
	@Bean
    public AuthenticationManager authenticationManagerBean() {
		return new JWTAuthenticationManager(userDetailsService(), passwordEncoder());
    }
	
	@Override
    public UserDetailsService userDetailsService() {
		return userDetailsService;
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public JWTTokenProvider tokenProvider() {
		return new JWTTokenProvider();
	}
}
