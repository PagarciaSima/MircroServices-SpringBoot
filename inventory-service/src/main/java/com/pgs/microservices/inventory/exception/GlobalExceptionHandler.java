package com.pgs.microservices.inventory.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles {@link HandlerMethodValidationException} thrown when validation fails for method parameters,
	 * such as those annotated with {@code @RequestParam}, {@code @PathVariable}, etc.
	 *
	 * <p>This method collects all validation errors related to method arguments and builds a response
	 * containing the field names (extracted and simplified) along with their respective error messages.
	 * It returns a {@code 400 Bad Request} status with a JSON object mapping each invalid field to its message.</p>
	 *
	 * @param ex the exception thrown when method-level validation fails
	 * @return a {@link ResponseEntity} containing a map of field names to validation error messages, with HTTP status 400
	 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
	    Map<String, String> errors = new HashMap<>();

	    ex.getValueResults().forEach(validationResult -> {
	        validationResult.getResolvableErrors().forEach(error -> {
	            // Limpiar el nombre del campo
	            String field = extractSimpleFieldName(error.getCodes());
	            String message = error.getDefaultMessage();
	            errors.put(field, message);
	        });
	    });

	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Extracts a simplified field name from an array of validation error codes.
	 *
	 * <p>This method is useful for determining the relevant field name when handling
	 * validation errors, particularly those related to method parameter validation (e.g., request parameters).
	 * It attempts to extract the last segment of the first error code that contains a dot ({@code .}),
	 * skipping common constraint names like {@code Min} and entries that include a method signature ({@code #}).</p>
	 *
	 * <p>If no suitable field name can be determined, the method returns {@code "unknown"}.</p>
	 *
	 * @param codes an array of error codes associated with a validation error
	 * @return the simplified field name (e.g., {@code "quantity"}), or {@code "unknown"} if not found
	 */
	private String extractSimpleFieldName(String[] codes) {
	    if (codes == null || codes.length == 0) return "unknown";

	    // Buscar un código que termine con el nombre del campo (por ejemplo: "quantity")
	    for (String code : codes) {
	        if (code.contains(".")) {
	            String[] parts = code.split("\\.");
	            String lastPart = parts[parts.length - 1];
	            if (!lastPart.equalsIgnoreCase("Min") && !lastPart.contains("#")) {
	                return lastPart;
	            }
	        }
	    }

	    return "unknown";
	}
	
	/**
	 * Handles exceptions thrown when a required request parameter is missing from an HTTP request.
	 *
	 * <p>This method catches {@link MissingServletRequestParameterException}, which is typically
	 * thrown when a request is missing a required query parameter annotated with {@code @RequestParam}.
	 * It returns a {@code 400 Bad Request} response with a JSON body that includes the name of the
	 * missing parameter and the default exception message.</p>
	 *
	 * @param ex the exception thrown when a required request parameter is missing
	 * @return a {@link ResponseEntity} containing a map with the parameter name as the key and the
	 *         exception message as the value, with HTTP status 400
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Map<String, String>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
	    Map<String, String> error = new HashMap<>();
	    error.put(ex.getParameterName(), ex.getMessage());
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
