package com.pgs.microservices.product.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	/**
	 * Handles validation exceptions thrown when method argument validation fails in controllers.
	 * 
	 * @param ex the MethodArgumentNotValidException containing validation errors
	 * @return a ResponseEntity with HTTP status 400 (Bad Request) including a map of
	 *         field names to their corresponding validation error messages
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        loadValidationErrors(ex, errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Extracts validation errors from the given MethodArgumentNotValidException
     * and populates the provided map with field names and their corresponding error messages.
     * 
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @param errors the map to populate with field-error message pairs
     */
	private void loadValidationErrors(MethodArgumentNotValidException ex, Map<String, String> errors) {
		ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
	}
}
