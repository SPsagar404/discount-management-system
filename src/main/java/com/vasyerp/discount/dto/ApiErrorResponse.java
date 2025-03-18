package com.vasyerp.discount.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Data;


@Data
public class ApiErrorResponse {
	private HttpStatus status;
	private String message;
	private Map<String, String> errors;
	private LocalDateTime timestamp;

	public ApiErrorResponse(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public ApiErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
		this.timestamp = LocalDateTime.now();
	}

}