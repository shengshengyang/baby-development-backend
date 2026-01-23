package com.dean.baby.api.exception;

import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        log.warn("API Exception: code={}, message={}", ex.getErrorCode().getCode(), ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getErrorCode().getCode());
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        if (ex.getData() != null) {
            body.put("data", ex.getData());
        }

        HttpStatus status = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("code", SysCode.SYSTEM_ERROR.getCode());
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("System Exception", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("code", SysCode.SYSTEM_ERROR.getCode());
        body.put("message", "System Error");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
