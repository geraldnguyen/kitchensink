package com.example.springweb.friend.controller;

import com.example.springweb.friend.data.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void handleNoSuchElementException(NoSuchElementException nsee, HttpServletRequest request) {
        log.error(String.format("[%s] : Not found", request.getPathInfo()), nsee);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected HttpEntity<ErrorDTO> handleEmptyResultDataAccessException(EmptyResultDataAccessException erdae) {
        log.error("EmptyResultDataAccessException", erdae);
        return new HttpEntity<>(ErrorDTO.builder().reason("Server Error").build());
    }
}
