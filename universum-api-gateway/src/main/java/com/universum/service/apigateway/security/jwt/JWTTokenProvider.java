package com.universum.service.apigateway.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.universum.common.exception.UniversumAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenProvider {
	private static final String SALT_KEY = "$2y$10$GBIQaf6gEeU9im8RTKhIgOZ5q5haDA.A5GzocSr5CR.sU8OUsCUwq";
    private static final int TOKEN_VALIDITY = 86400; // Value in second 24 hrs = 86400 seconds
    private static final String AUTHORITIES_KEY = "ROLES";
    
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString(SALT_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long TOKEN_VALIDITY_IN_MILLISECONDS = 1000L * TOKEN_VALIDITY;
    
    public String createToken(final Authentication authentication) {
    	Assert.notNull(authentication, "Authentication can not be null.");
    	String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    	return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities).signWith(SignatureAlgorithm.HS512, SECRET_KEY)
    			.setExpiration(Date.from(Instant.now().plus(Duration.ofMillis(TOKEN_VALIDITY_IN_MILLISECONDS))))
    			.setIssuedAt(Date.from(Instant.now())).compact();
    }
    
    public Authentication getAuthentication(final String authToken) {
    	if(StringUtils.isBlank(authToken) || !hasValidClaims(authToken)) {
    		log.debug("Invalid token, JWT token is either empty or null or invalid.");
    		throw new UniversumAPIException(HttpStatus.UNAUTHORIZED, "Invalid token");
    	}
    	Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
    	Collection<? extends GrantedAuthority> roles = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    	return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", roles), authToken, roles);
    }
    
    private boolean hasValidClaims(final String authToken) {
    	boolean hasClaims = false;
    	try {
    		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
    		hasClaims = true;
    	} catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException parseEx) {
    		log.error("Unable to parse claims from auth token " + authToken, parseEx);
    	}
    	return hasClaims;
    }
}
