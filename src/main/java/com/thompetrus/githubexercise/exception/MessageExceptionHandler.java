package com.thompetrus.githubexercise.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;


@Slf4j
@ControllerAdvice
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MessageException.class)
    private ResponseEntity<Object> handle(MessageException e, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.name());
        body.put("message", e.getMessage());
        log.error("Something went wrong getting or setting the message", e, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MessageNotFoundException.class)
    private ResponseEntity<Object> handle(MessageNotFoundException e, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.name());
        body.put("message", e.getMessage());
        log.error("No message found for key.", e, request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
