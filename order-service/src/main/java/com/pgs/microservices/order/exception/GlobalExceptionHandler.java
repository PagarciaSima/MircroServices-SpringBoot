package com.pgs.microservices.order.exception;

import java.time.LocalDateTime;
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
	 * Handles ProductNotInStockException exceptions thrown by controller methods.
	 * Constructs a response body containing error details including timestamp, HTTP status,
	 * error description, exception message, and the request path.
	 *
	 * @param ex the ProductNotInStockException instance thrown
	 * @return a ResponseEntity containing a map with error details and HTTP status 400 (Bad Request)
	 */
	@ExceptionHandler(ProductNotInStockException.class)
	public ResponseEntity<Map<String, Object>> handleProductNotInStockException(ProductNotInStockException ex) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("timestamp", LocalDateTime.now().toString());
	    body.put("status", HttpStatus.BAD_REQUEST.value());
	    body.put("error", "Product Not Available");
	    body.put("message", ex.getMessage());
	    body.put("path", "/api/order");

	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
    
	/**
	 * Handles MethodArgumentNotValidException exceptions thrown when validation on an argument annotated
	 * with @Valid fails. Collects all validation error messages and returns them as a map where
	 * the keys are the field names and the values are the corresponding error messages.
	 *
	 * @param ex the MethodArgumentNotValidException containing validation errors
	 * @return a ResponseEntity containing a map of field error messages and HTTP status 400 (Bad Request)
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        loadValidationErrors(ex, errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Extracts validation error messages from the given MethodArgumentNotValidException and populates
     * the provided map with field names as keys and their corresponding error messages as values.
     *
     * @param ex the MethodArgumentNotValidException containing the validation errors
     * @param errors the map to populate with field error messages
     */
	private void loadValidationErrors(MethodArgumentNotValidException ex, Map<String, String> errors) {
		ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
	}
}
