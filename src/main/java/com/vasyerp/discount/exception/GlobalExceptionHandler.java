package com.vasyerp.discount.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vasyerp.discount.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundExcepton.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(ProductNotFoundExcepton ex) {
        logger.error("Product not found: {}", ex.getMessage());
        return buildResponseEntity(new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ApiErrorResponse> handleProductOutOfStock(ProductOutOfStockException ex) {
        logger.warn("Product out of stock: {}", ex.getMessage());
        return buildResponseEntity(new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(DiscountTypeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDiscountType(DiscountTypeNotFoundException ex) {
        logger.error("Invalid discount type: {}", ex.getMessage());
        return buildResponseEntity(new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildResponseEntity(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."));
    }

    private ResponseEntity<ApiErrorResponse> buildResponseEntity(ApiErrorResponse response) {
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, response.getStatus());
    }
}