package com.ing.brokagefirm.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler({HibernateException.class, SQLException.class, PSQLException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleSQLExceptions(HttpServletRequest req, Exception ex) {
        log.error("Error: {} \nFor Request URL: {}", ex.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public void handleRuntimeExceptions(HttpServletRequest req, Exception ex) {
        log.error("Error: {} \nFor Request URL: {}", ex.getMessage(), req.getRequestURL().toString());
    }

}
