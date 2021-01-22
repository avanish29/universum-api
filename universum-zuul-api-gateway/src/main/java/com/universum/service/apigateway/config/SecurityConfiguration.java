package com.universum.service.apigateway.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.universum.service.apigateway.security.JWTAuthenticationManager;
import com.universum.service.apigateway.security.JWTHeaderRequestMatcher;
import com.universum.service.apigateway.security.JWTTokenAuthenticationConverter;
import com.universum.service.apigateway.security.JWTTokenAuthenticationEntryPoint;
import com.universum.service.apigateway.security.JWTTokenProvider;
import com.universum.service.apigateway.service.LoadBalancedUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String[] AUTH_WHITELIST = {"/resources/**", "/webjars/**", "/authenticate/**", "/favicon.ico"};
	
	@Autowired
	private LoadBalancedUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(new JWTTokenAuthenticationEntryPoint())
		.and()
		.authorizeRequests().antMatchers("/actuator/**").permitAll()
		.and()
		.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
		.and()
		.authorizeRequests().antMatchers("/**").hasAuthority("ROLE_SUPER_ADMIN")
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated();
		
		http.addFilterBefore(webFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(AUTH_WHITELIST);
	}
	
	@Bean
    public Filter webFilter() {
		AuthenticationFilter authenticationWebFilter = new AuthenticationFilter(authenticationManagerBean(), new JWTTokenAuthenticationConverter(new JWTTokenProvider()));
        authenticationWebFilter.setRequestMatcher(new JWTHeaderRequestMatcher());
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
