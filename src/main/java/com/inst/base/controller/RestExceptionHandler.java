package com.inst.base.controller;

import com.inst.base.response.ServiceErrorResponse;
import com.inst.base.util.ServiceException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestControllerAdvice
public class RestExceptionHandler {

//    @ExceptionHandler(MailSendException.class)
//    public ResponseEntity<ServiceErrorResponse> handleSMPTAddressFailedException(MailSendException e) {
//        ServiceException exception = new ServiceException("SMPT error", HttpStatus.INTERNAL_SERVER_ERROR);
//        systemErrorLogger.createErrorLog(exception);
//        return new ResponseEntity<>(new ServiceErrorResponse(exception), exception.getHttpCode());
//    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceErrorResponse> handleServiceException(ServiceException e) {
        return new ResponseEntity<>(new ServiceErrorResponse(e), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServiceErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> advices = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            advices.add("%s - %s".formatted(fieldName, errorMessage));
        });

        ServiceException exception = new ServiceException("Request body validation failed", HttpStatus.BAD_REQUEST);

        ServiceErrorResponse errorResponse = new ServiceErrorResponse(exception);

        advices.forEach(errorResponse::addAdvice);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceErrorResponse> handleNotReadableException(HttpMessageNotReadableException e) {
        ServiceException exception = new ServiceException("Can`t read Request body", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new ServiceErrorResponse(exception), HttpStatus.BAD_REQUEST);
    }


}