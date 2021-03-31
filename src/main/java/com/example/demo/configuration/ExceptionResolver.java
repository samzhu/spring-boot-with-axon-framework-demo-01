package com.example.demo.configuration;

import java.time.OffsetDateTime;

import com.example.demo.accountms.interfaces.rest.dto.ErrorMessageBody;
import com.example.demo.exceptions.InsufficientBalanceException;

import org.axonframework.axonserver.connector.command.AxonServerRemoteCommandHandlingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = AxonServerRemoteCommandHandlingException.class)
    public ResponseEntity<ErrorMessageBody> handleCompletionException(AxonServerRemoteCommandHandlingException ex) {
        log.debug("抓到 AxonServerRemoteCommandHandlingException={}", ex.toString());
        return new ResponseEntity<ErrorMessageBody>(new ErrorMessageBody(OffsetDateTime.now(),
                HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), ex.getMessage(), null),
                HttpStatus.FORBIDDEN);
    }
    // 會抓不到
    // @ExceptionHandler(value = InsufficientBalanceException.class)
    // public ResponseEntity<ErrorMessageBody> handleCompletionException(InsufficientBalanceException ex) {
    //     log.debug("抓到 InsufficientBalanceException");
    //     return new ResponseEntity<ErrorMessageBody>(new ErrorMessageBody(OffsetDateTime.now(),
    //             HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), ex.getMessage(), null),
    //             HttpStatus.FORBIDDEN);
    // }

}
