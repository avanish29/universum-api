package com.universum.common.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UniversumAPIError {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;

	private UniversumAPIError() {
		timestamp = LocalDateTime.now();
	}

	public UniversumAPIError(HttpStatus status) {
		this();
		this.status = status;
	}

	public UniversumAPIError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		if(ex != null) this.debugMessage = ex.getLocalizedMessage();
	}

	public UniversumAPIError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		if(ex != null) this.debugMessage = ex.getLocalizedMessage();
	}
	
	public Integer getStatusCode() {
		return status != null ? status.value() : null;
	}
}
