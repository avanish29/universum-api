package com.universum.security.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.universum.common.model.UniversumAPIError;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Order(100)
@ControllerAdvice
@Component
@Slf4j
public class UniversumSecurityExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
		log.debug("Access denied. Message {} ", accessDeniedException);
		UniversumAPIError apiError = new UniversumAPIError(HttpStatus.FORBIDDEN);
        apiError.setMessage(accessDeniedException.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
