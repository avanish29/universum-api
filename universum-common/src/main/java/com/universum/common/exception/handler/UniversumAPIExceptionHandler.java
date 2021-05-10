package com.universum.common.exception.handler;

import com.universum.common.dto.response.APIError;
import com.universum.common.dto.response.APIValidationError;
import com.universum.common.exception.NotFoundException;
import com.universum.common.exception.UniversumAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Set;

@Configuration
@ControllerAdvice
@Component
@Slf4j
public class UniversumAPIExceptionHandler extends ResponseEntityExceptionHandler {
	private static final String VALIDATION_ERROR_MSG = "Validation error";
	
	@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, error, ex));
    }
	
	@Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new APIError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIValidationError apiError = new APIValidationError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(VALIDATION_ERROR_MSG);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
		if (!CollectionUtils.isEmpty(supportedMethods)) {
			headers.setAllow(supportedMethods);
		}
		return buildResponseEntity(new APIError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), ex.getCause()));
	}
	
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new APIValidationError(HttpStatus.BAD_REQUEST, error, ex));
    }
	
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new APIValidationError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }
	
	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIValidationError apiError = new APIValidationError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIValidationError apiError = new APIValidationError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(VALIDATION_ERROR_MSG);
        apiError.addValidationErrors(ex.getFieldErrors());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
		APIError apiError = new APIError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception ex) {
		log.error("An error occurred while performing operation", ex);
		APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR);
		if("org.springframework.security.access.AccessDeniedException".equalsIgnoreCase(ex.getClass().getName())) {
			apiError = new APIError(HttpStatus.FORBIDDEN);
		}
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(UniversumAPIException.class)
	protected ResponseEntity<Object> handleApiException(UniversumAPIException apiEx) {
		return buildResponseEntity(new APIError(apiEx.getStatus(), apiEx.getMessage(), apiEx.getCause()));
	}
	
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        APIValidationError apiError = new APIValidationError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(VALIDATION_ERROR_MSG);
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }
	
	@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
		log.error("NotFoundException {} \n", request.getRequestURI(), ex);
		APIError apiError = new APIError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
	
	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<Object> handleValidationException(ValidationException ex) {
		APIError apiError = new APIError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(final APIError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
