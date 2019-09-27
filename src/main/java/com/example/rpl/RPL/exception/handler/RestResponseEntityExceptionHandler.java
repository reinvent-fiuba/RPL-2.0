package com.example.rpl.RPL.exception.handler;


import com.example.rpl.RPL.exception.BaseAPIException;
import com.example.rpl.RPL.exception.dto.ErrorResponse;
import com.example.rpl.RPL.exception.dto.UnprocessableEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Global exception Handler. This component handles all exceptions and
 * build an appropriate JSON response.
 */
@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "invalid_json", "Malformed JSON request.", ex);
        log.info("[method:exception-handler][exception:HttpMessageNotReadableException][message:{}][error:{}][status:{}]", errorResponse.getMessage(), errorResponse.getError(), errorResponse.getStatus());
        return new ResponseEntity<>(errorResponse, headers, errorResponse.getStatusObj());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "missing_parameter", ex.getParameterName() + " is not present.");
        log.info("[method:exception-handler][exception:MissingServletRequestParameterException][message:{}][error:{}][status:{}]", errorResponse.getMessage(), errorResponse.getError(), errorResponse.getStatus());
        return new ResponseEntity<>(errorResponse, errorResponse.getStatusObj());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        UnprocessableEntity unprocessableEntity = new UnprocessableEntity();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            unprocessableEntity.addValidationError(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            unprocessableEntity.addValidationError(error.getObjectName(), error.getDefaultMessage());
        }
        log.info("[method:exception-handler][exception:MethodArgumentNotValidException][message:{}][error:{}][status:{}][cause:[{}]]", unprocessableEntity.getMessage(), unprocessableEntity.getError(), unprocessableEntity.getStatus(), unprocessableEntity.getValidationErrorsMessage());
        return new ResponseEntity<>(unprocessableEntity, new HttpHeaders(), unprocessableEntity.getStatusObj());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "bad_credentials", "".concat(ex.getMessage()), ex.getCause());
        log.info("[method:exception-handler][exception:BadCredentialsException][message:{}][error:{}][status:{}][cause:[{}]]", ex.getMessage(), "bad_credentials", HttpStatus.UNAUTHORIZED, ex.getCause());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatusObj());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        UnprocessableEntity unprocessableEntity = new UnprocessableEntity();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            unprocessableEntity.addValidationError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.info("[method:exception-handler][exception:ConstraintViolationException][message:{}][error:{}][status:{}][cause:[{}]]", unprocessableEntity.getMessage(), unprocessableEntity.getError(), unprocessableEntity.getStatus(), unprocessableEntity.getValidationErrorsMessage());
        return new ResponseEntity<>(unprocessableEntity, new HttpHeaders(), unprocessableEntity.getStatusObj());
    }

    @ExceptionHandler({BaseAPIException.class})
    public ResponseEntity<Object> handleBaseAPIException(BaseAPIException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getError(), ex.getMessage(), ex.getCause());
        log.info("[method:exception-handler][exception:BaseAPIException][message:{}][error:{}][status:{}]", ex.getMessage(), ex.getError(), ex.getStatus());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatusObj());
    }

    @ExceptionHandler({EnumConstantNotPresentException.class})
    public ResponseEntity<Object> handleEnumConstantNotPresentException(EnumConstantNotPresentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "invalid_enum_value", "Invalid enum value ".concat(ex.getMessage()), ex.getCause());
        log.info("[method:exception-handler][exception:BaseAPIException][message:{}][error:{}][status:{}]", ex.getMessage(), "invalid_enum_value", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatusObj());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex);
        log.error("[method:exception-handler][exception:Exception][message:{}][error:{}][status:{}]", errorResponse.getMessage(), errorResponse.getError(), errorResponse.getStatus());
        log.error("[method:exception-handler][exception:Exception]", ex);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatusObj());
    }
}

