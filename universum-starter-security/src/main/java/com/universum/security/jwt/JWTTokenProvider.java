package com.universum.security.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import com.universum.security.property.UniversumSecurityProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

public class JWTTokenProvider {
	private final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);
	private static final String AUTHORITIES_KEY = "ROLES";
	
	private final UniversumSecurityProperties universumProperties;
	
	private Key secretKey;
	
	private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;
	
	public JWTTokenProvider(UniversumSecurityProperties universumProperties) {
        this.universumProperties = universumProperties;
    }
	
	@PostConstruct
    public void init() {
		String secret = universumProperties.getAuthentication().getJwt().getSecret();
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.tokenValidityInMilliseconds = 1000 * this.universumProperties.getAuthentication().getJwt().getTokenValidityInSeconds();
	    this.tokenValidityInMillisecondsForRememberMe = 1000 * this.universumProperties.getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
	}
	
	public String createToken(final Authentication authentication, boolean rememberMe) {
    	Assert.notNull(authentication, "Authentication can not be null.");
    	String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    	
    	long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
    	
    	return Jwts.builder()
    			   .setSubject(authentication.getName())
    			   //.setAudience(authorities) Set Tenant ID as Audience
    			   .claim(AUTHORITIES_KEY, authorities)
    			   .signWith(secretKey, SignatureAlgorithm.HS512)
    			   .setExpiration(validity)
    			   .setIssuedAt(Date.from(Instant.now()))
    			   .compact();
    }
	
	public Authentication getAuthentication(final String authToken) {
    	var claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken).getBody();
    	Collection<? extends GrantedAuthority> roles = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    	return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", roles), authToken, roles);
    }
	
	public boolean validateToken(final String authToken) {
    	var isValidToken = false;
    	try {
    		Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
    		isValidToken = true;
    	} catch (SignatureException signatureException) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace.", signatureException);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace.", expiredJwtException);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace.", unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace.", illegalArgumentException);
        }  catch (MalformedJwtException malformedJwtException) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", malformedJwtException);
        }
    	return isValidToken;
    }
}
