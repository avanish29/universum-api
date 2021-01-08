package com.universum.common.exception.handler;

import java.util.Set;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.universum.common.exception.UniversumAPIException;
import com.universum.common.model.UniversumAPIError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public final class UniversumAPIExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
		if (!CollectionUtils.isEmpty(supportedMethods)) {
			headers.setAllow(supportedMethods);
		}
		return buildResponseEntity(new UniversumAPIError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), ex.getCause()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
		UniversumAPIError apiError = new UniversumAPIError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception ex) {
		UniversumAPIError apiError = new UniversumAPIError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(UniversumAPIException.class)
	protected ResponseEntity<Object> handleApiException(UniversumAPIException apiEx) {
		return buildResponseEntity(new UniversumAPIError(apiEx.getStatus(), apiEx.getMessage(), apiEx.getCause()));
	}

	private ResponseEntity<Object> buildResponseEntity(final UniversumAPIError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
