package com.ecommers.product.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadCreateRequest.class)
    public ResponseEntity handleBadInput(){
        log.warn("bad request sent by user product name or service is missing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ProductExisitException.class)
    public ResponseEntity handleDuplicateInserts(){
        log.warn("Entry already present in DB");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
}
