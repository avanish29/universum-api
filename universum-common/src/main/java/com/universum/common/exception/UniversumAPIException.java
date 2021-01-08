package com.universum.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class UniversumAPIException extends RuntimeException {
	private static final long serialVersionUID = 3276809853492564562L;
	private HttpStatus status;
	private String message;
	private Throwable cause;
	
	public UniversumAPIException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public UniversumAPIException(HttpStatus status, String message, Throwable cause) {
		this.status = status;
		this.message = message;
		this.cause = cause;
	}
}
