package com.TrungTinhBackend.inventory_backend.exceptions;

import com.TrungTinhBackend.inventory_backend.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllExceptions(Exception e) {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
               response.setMessage(e.getMessage());
         return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException e) {
        Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameValueRequiredException.class)
    public ResponseEntity<Response> handleNameValueRequiredException(NameValueRequiredException e) {
        Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> handleInvalidCredentialsException(InvalidCredentialsException e) {
        Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
