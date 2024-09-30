package com.ing.brokagefirm.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {

    private final String errorMessage = "Error: {} \nFor Request URL: {}";


    @ExceptionHandler({HibernateException.class, SQLException.class, PSQLException.class})
    public ResponseEntity<AppExceptionEntity> handleSQLExceptions(HttpServletRequest req, Exception ex) {
        log.error(errorMessage, ex.getMessage(), req.getRequestURL().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppExceptionEntity(ex.getMessage(), req.getRequestURL().toString()));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<AppExceptionEntity> handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
        log.error(errorMessage, ex.getMessage(), req.getRequestURL().toString());

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new AppExceptionEntity(ex.getMessage(), req.getRequestURL().toString()));
    }

    @ExceptionHandler({AuthorizationServiceException.class})
    public ResponseEntity<AppExceptionEntity> handleAuthorizationException(HttpServletRequest req, Exception ex) {
        log.error(errorMessage, ex.getMessage(), req.getRequestURL().toString());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AppExceptionEntity(ex.getMessage(), req.getRequestURL().toString()));
    }

}
