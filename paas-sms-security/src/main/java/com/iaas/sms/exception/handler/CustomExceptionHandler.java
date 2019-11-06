/*
 * 
 */
package com.iaas.sms.exception.handler;

import com.iaas.sms.exception.model.ServiceException;
import com.iaas.sms.exception.model.UserNotFoundException;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// TODO: Auto-generated Javadoc
/**
 * The Class CustomExceptionHandler.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * Handle mongo exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(MongoException.class)
    public ResponseEntity handleMongoException(final MongoException exception) {
        log.warn("Processing mongo exception: {}", exception.getMessage());

        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle service exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity handleServiceException(final ServiceException exception) {
        log.warn("Processing service exception: {}", exception.getMessage());

        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle user not found exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(final UserNotFoundException exception) {
        log.warn("Processing user not found exception: {}", exception.getMessage());

        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle abstract exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAbstractException(final Exception exception) {
        log.warn("Processing abstract exception: {}", exception.getMessage());

        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
