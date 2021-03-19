package com.universum.common.auth.jwt;

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
import org.springframework.web.server.ResponseStatusException;

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
    	return Jwts.builder()
    			   .setSubject(authentication.getName())
    			   .claim(AUTHORITIES_KEY, authorities)
    			   .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
    			   .setExpiration(Date.from(Instant.now().plus(Duration.ofMillis(TOKEN_VALIDITY_IN_MILLISECONDS))))
    			   .setIssuedAt(Date.from(Instant.now()))
    			   .compact();
    }
    
    public Authentication getAuthentication(final String authToken) {
    	if(StringUtils.isBlank(authToken) || !validateToken(authToken)) {
    		log.debug("Invalid token, JWT token is either empty or null or invalid.");
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    	}
    	Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
    	Collection<? extends GrantedAuthority> roles = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    	return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", roles), authToken, roles);
    }
    
    public boolean validateToken(final String authToken) {
    	boolean isValidToken = false;
    	try {
    		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
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
