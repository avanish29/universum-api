/**
 * Copyright (c) 2021-present Universum Systems. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.universum.service.security.listener;

import com.universum.service.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Component
@Slf4j
public class AuthenticationAuditListener {
	@Autowired
	private UserService userService;
	
	@EventListener
	public void onAuthenticationSuccess(final AuthenticationSuccessEvent authenticationSuccessEvent) {
		final String userName = getUserName(authenticationSuccessEvent.getAuthentication());
		userService.onAuthenticationEvent(userName, eventOn(authenticationSuccessEvent.getTimestamp()), AuthenticationEventType.AUTHENTICATION_SUCCESS);
		log.debug("Authentication Success :: UserName -> {}, Source -> {} and Time -> {} ", userName, authenticationSuccessEvent.getSource(), authenticationSuccessEvent.getTimestamp());
	}
	
	@EventListener
	public void onAuthenticationFailure(final AbstractAuthenticationFailureEvent authenticationFailureEvent) {
		final String userName = getUserName(authenticationFailureEvent.getAuthentication());
		userService.onAuthenticationEvent(userName, eventOn(authenticationFailureEvent.getTimestamp()), AuthenticationEventType.AUTHENTICATION_FAILURE);
		log.debug("Authentication Failure :: UserName -> {}, Exception Message -> {} and Time -> {}", userName, authenticationFailureEvent.getException().getMessage(), authenticationFailureEvent.getTimestamp());
	}
	
	String getUserName(final Authentication authentication) {
		String userName = null;
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			userName = user.getUsername();
            log.debug("The username `{}` has been found using JWT", userName);
		} else if (authentication.getPrincipal() instanceof String) {
			userName = (String) authentication.getPrincipal();
        } else {
            log.debug("The username could not be found");
        }
		return userName;
	}
	
	LocalDateTime eventOn(final long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}

	public enum AuthenticationEventType {
		AUTHENTICATION_SUCCESS,
		AUTHENTICATION_FAILURE;
	}

}
