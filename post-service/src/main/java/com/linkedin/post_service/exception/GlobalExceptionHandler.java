package com.linkedin.post_service.exception;

import com.linkedin.post_service.dto.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// @ControllerAdvice to handle exceptions globally
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> handleApiException(ApiException exception,
                                                           WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, exception.getStatus());
    }

    // handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handle Validation exceptions
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        try {
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message);
            });

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        catch (ClassCastException exception){

            AtomicInteger atomicIndex = new AtomicInteger();

            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String message = error.getDefaultMessage();

                int val = atomicIndex.get();

                String stringToAdd = val == 0 ? "" : Integer.toString(val);

                errors.put("error" + stringToAdd, message);
                atomicIndex.addAndGet(1);
            });

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }
}
