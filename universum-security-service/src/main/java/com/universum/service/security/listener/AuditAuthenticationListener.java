package com.universum.service.security.listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.universum.service.security.dto.UserResponse;
import com.universum.service.security.service.AppUserService;
import com.universum.service.security.util.AuthenticationEventType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuditAuthenticationListener {
	@Autowired
	private AppUserService userService;
	
	@EventListener
	public void auditAuthnticationSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
		final String userName = getUserName(authenticationSuccessEvent.getAuthentication());
		userService.onAuthenticationEvent(userName, eventOn(authenticationSuccessEvent.getTimestamp()), AuthenticationEventType.AUTHENTICATION_SUCCESS);
		log.debug("Authentication Success :: UserName -> {}, Source -> {} and Time -> {} ", userName, authenticationSuccessEvent.getSource(), authenticationSuccessEvent.getTimestamp());
	}
	
	@EventListener
	public void auditAuthnticationFailure(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
		final String userName = getUserName(authenticationFailureEvent.getAuthentication());
		userService.onAuthenticationEvent(userName, eventOn(authenticationFailureEvent.getTimestamp()), AuthenticationEventType.AUTHENTICATION_FAILURE);
		log.debug("Authentication Failure :: UserName -> {}, Exception Message -> {} and Time -> {}", userName, authenticationFailureEvent.getException().getMessage(), authenticationFailureEvent.getTimestamp());
	}
	
	String getUserName(final Authentication authentication) {
		String userName = null;
		if (authentication.getPrincipal() instanceof UserResponse) {
			UserResponse user = (UserResponse) authentication.getPrincipal();
			userName = user.getUsername();
            log.debug("The username `{}` has been found using JWT", userName);
		} else if (authentication.getPrincipal() instanceof String) {
			userName = (String) authentication.getPrincipal();
        } else {
            log.debug("The username could not be found");
        }
		return userName;
	}
	
	LocalDateTime eventOn(long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}
}
