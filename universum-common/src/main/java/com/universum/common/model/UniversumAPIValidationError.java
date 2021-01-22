package com.universum.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UniversumAPIValidationError extends UniversumAPIError {
	private List<ValidationError> validationErrors;
	
	public UniversumAPIValidationError() {
		super();
	}

	public UniversumAPIValidationError(HttpStatus status) {
		super(status);
	}

	public UniversumAPIValidationError(HttpStatus status, Throwable ex) {
		super(status, ex);
	}

	public UniversumAPIValidationError(HttpStatus status, String message, Throwable ex) {
		super(status, message, ex);
	}
	
	public void addValidationErrors(final List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }
	
	public void addValidationError(final List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }
	
	public void addValidationErrors(final Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }
	
	private void addValidationError(final FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }
	
	private void addValidationError(final String object, final String field, final Object rejectedValue, final String message) {
		addValidationError(new ValidationError(object, field, rejectedValue, message));
    }
	
	private void addValidationError(final ValidationError validationError) {
        if (validationErrors == null) {
        	validationErrors = new ArrayList<>();
        }
        validationErrors.add(validationError);
    }
	
	private void addValidationError(final ObjectError objectError) {
        this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
    }
	
	private void addValidationError(final String object, final String message) {
		addValidationError(new ValidationError(object, message));
    }
	
	private void addValidationError(final ConstraintViolation<?> cv) {
        this.addValidationError(cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath().toString(), cv.getInvalidValue(), cv.getMessage());
    }
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	@AllArgsConstructor
	public static final class ValidationError {
		private String object;
	    private String field;
	    private Object rejectedValue;
	    private String message;
	    
	    public ValidationError(final String object, final String message) {
	    	this.object = object;
	    	this.message = message;
	    }
	}
}
