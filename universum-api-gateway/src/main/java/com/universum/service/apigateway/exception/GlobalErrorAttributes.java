package com.universum.service.apigateway.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		
		Throwable error = getError(request);
		MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(error.getClass(), SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
		HttpStatus errorStatus = getHttpStatus(error, responseStatusAnnotation);
		
		errorAttributes.put("status", errorStatus.value());
		errorAttributes.put("timestamp", new Date());
		errorAttributes.put("message", getMessage(error, responseStatusAnnotation));
		
		handleBindingException(errorAttributes, error);
		errorAttributes.put("statusMsg", errorStatus);
		errorAttributes.put("requestId", request.exchange().getRequest().getId());
		errorAttributes.put("path", request.path());
		return errorAttributes;
	}
	
	private HttpStatus getHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
		if (error instanceof ResponseStatusException) {
			return ((ResponseStatusException) error).getStatus();
		}
		return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String getMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
		if (error instanceof BindingResult) {
			return ((WebExchangeBindException)error).getReason();
		}
		if (error instanceof ResponseStatusException) {
			return ((ResponseStatusException) error).getReason();
		}
		String reason = responseStatusAnnotation.getValue("reason", String.class).orElse("");
		if (StringUtils.hasText(reason)) {
			return reason;
		}
		return (error.getMessage() != null) ? error.getMessage() : "";
	}
	
	private void handleBindingException(Map<String, Object> errorAttributes, Throwable error) {
		if (error instanceof BindingResult) {
			BindingResult result = (BindingResult) error;
			if (result.hasErrors()) {
				errorAttributes.put("validationErrors", result.getAllErrors());
			}
		}
	}
}
