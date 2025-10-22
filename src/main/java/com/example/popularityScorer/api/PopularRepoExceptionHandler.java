package com.example.popularityScorer.api;

import com.example.popularityScorer.ErrorResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PopularRepoExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(Exception e) {
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                new ErrorResponseEntity("system down", e.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.UnprocessableEntity.class)
    public ResponseEntity handleUnProcessableEntity(Exception e) {
        return createResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY,
                new ErrorResponseEntity("The entity cannot be processed", e.getLocalizedMessage()));

    }


    private ResponseEntity createResponseEntity(HttpStatus status,
                                                ErrorResponseEntity errorResponseEntity) {
        return ResponseEntity.status(status).body(errorResponseEntity);
    }


}
