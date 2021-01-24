package com.universum.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UniversumAPIException extends RuntimeException {
	private static final long serialVersionUID = 3276809853492564562L;
	private final HttpStatus status;
	private final String message;
	private final Throwable cause;
	
	public UniversumAPIException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
		this.cause = null;
	}
	
	public UniversumAPIException(HttpStatus status, String message, Throwable cause) {
		this.status = status;
		this.message = message;
		this.cause = cause;
	}
}
