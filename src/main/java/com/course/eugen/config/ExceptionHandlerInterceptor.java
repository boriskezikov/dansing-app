package com.course.eugen.config;

import com.course.eugen.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlerInterceptor {


    @ResponseBody
    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorDTO handleApiException(EntityNotFoundException ex) {
        return new ErrorDTO(404, ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = {AppBusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorDTO handleApiException(AppBusinessException ex) {
        return new ErrorDTO(400, ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorDTO handleApiException(Exception ex) {
        return new ErrorDTO(500, ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = {ResponseStatusException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorDTO handleApiException(ResponseStatusException ex) {
        return new ErrorDTO(ex.getStatus().value(), ex.getMessage());
    }


}
