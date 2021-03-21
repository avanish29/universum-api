package com.universum.security.jwt.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universum.security.jwt.JWTConfigurer;
import com.universum.security.jwt.JWTTokenProvider;
import com.universum.security.property.UniversumSecurityProperties;
import com.universum.security.util.AuthenticationConstant;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({UniversumSecurityProperties.class})
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Profile("!" + AuthenticationConstant.PROFILE_OAUTH2)
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
	private final Logger log = LoggerFactory.getLogger(JWTSecurityConfig.class);
	private static final String[] AUTH_WHITELIST = {"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/services/security/authenticate/**", "/authenticate/**", "/settings/**", "/services/label/messages/**", "/favicon.ico"};
	
	private final UniversumSecurityProperties universumProperties;
	
	public JWTSecurityConfig(UniversumSecurityProperties universumProperties) {
        this.universumProperties = universumProperties;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable().headers().frameOptions().disable().and();
        
        // Set session management to stateless
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        
        // Set unauthorized requests exception handler
        http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
            log.debug("Unauthorized request - {}", ex.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }).and();
        
        // Set permissions on endpoints
        http = http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        						// Actuator endpoints must be publicly accessible
        						.antMatchers(AuthenticationConstant.ACTUATOR_PATH).permitAll()
        						// All JS, CSS & HTML pages should be publicly accessible
        						.antMatchers(AUTH_WHITELIST).permitAll()
        						// Allow all admin endpoints for user with role ADMIN
        						.antMatchers(universumProperties.getSecurity().getAdminEndpoints()).hasRole("ADMIN")
        						// Allow all unsecured endpoints
        						.antMatchers(universumProperties.getSecurity().getUnsecuredEndpoints()).permitAll()
        						// User with role SUPER_ADMIN has all permission
        						.antMatchers(HttpMethod.GET, "/services/label/**").permitAll()
        						// Our private endpoints
        		                .anyRequest().authenticated().and();
        
        // Add JWT token filter
        http.apply(securityConfigurerAdapter());
	}
	
	private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider());
    }
	
	@Bean
    public JWTTokenProvider tokenProvider() {
        return new JWTTokenProvider(universumProperties);
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
