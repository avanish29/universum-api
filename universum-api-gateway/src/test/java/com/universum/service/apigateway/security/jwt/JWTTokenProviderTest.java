package com.universum.service.apigateway.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.universum.common.exception.UniversumAPIException;

class JWTTokenProviderTest {
	private JWTTokenProvider provider = new JWTTokenProvider();
	@Test
	void testCreateToken() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>() {{ add(new SimpleGrantedAuthority("CONFIG_ADMIN")); add(new SimpleGrantedAuthority("LABEL_ADMIN")); }};
		User user = new User("admin", "abcd", authorities);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, "abcd", authorities);
		String authToken = provider.createToken(authentication);
		Assertions.assertNotNull(authToken);
	}
	
	@Test
	void testGetAuthentication() {
		final String AUTH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIlJPTEVTIjoiQ09ORklHX0FETUlOLExBQkVMX0FETUlOIiwiZXhwIjoxNjEwOTEyNDQ2fQ.CxgMBl4Ns-x08TzGJXC5J4HXc0lVYyMjd_8wIKOcec4W4zKPSbUMhcrI3zNPXhnsZQbS7Z_c1SL5JbIoAWunpw";
		Authentication authentication = provider.getAuthentication(AUTH_TOKEN);
		Assertions.assertNotNull(authentication);
	}
	
	@Test
	void testGetAuthenticationWhenAuthTokenIsBlank() {
		Assertions.assertThrows(UniversumAPIException.class, () -> {provider.getAuthentication(StringUtils.EMPTY);});
	}
	
	@Test
	void testGetAuthenticationWhenAuthTokenIsNull() {
		Assertions.assertThrows(UniversumAPIException.class, () -> {provider.getAuthentication(null);});
	}
	
	@Test
	void testGetAuthenticationWhenAuthTokenIsInvalid() {
		final String AUTH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIlJPTEVTIjoiQ09ORklHX0FETUlOLExBQkVMX0FETUlOIiwiZXhwIjoxNjEwOTEyNDQ2fQ.CxgMBl4Ns-x08TzGJXC5J4HXc0lVYyMjd_";
		Assertions.assertThrows(UniversumAPIException.class, () -> {provider.getAuthentication(AUTH_TOKEN);});
	}
}
