package kr.pincoin.durian.home.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice // REST API only
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    // Not implemented yet
    // 405 handleHttpRequestMethodNotSupported
    // 415 handleHttpMediaTypeNotSupported
    // 406 handleHttpMediaTypeNotAcceptable
    // 500 handleMissingPathVariable
    // 400 handleMissingServletRequestParameter
    // 400 handleMissingServletRequestPart
    // 400 handleServletRequestBindingException
    // 503 handleAsyncRequestTimeoutException
    // 500 handleConversionNotSupported
    // 400 handleTypeMismatch
    // 400 handleHttpMessageNotReadable
    // 500 handleHttpMessageNotWritable

    @Override
    protected ResponseEntity<Object>
    handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                  @NonNull HttpHeaders headers,
                                  @NonNull HttpStatusCode status,
                                  @NonNull WebRequest request) {
        // applications.yaml
        //  spring.web.resources.add-mappings=false
        //  spring.mvc.throw-exception-if-no-handler-found=true
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND,
                                                         "resource not found",
                                                         new ArrayList<>());

        return handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                 @NonNull HttpHeaders headers,
                                 @NonNull HttpStatusCode status,
                                 @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        log.error(ex.getLocalizedMessage());

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                                                         "invalid parameters",
                                                         errors);

        return handleExceptionInternal(ex, response, headers, status, request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object>
    handleAccessDeniedException(AccessDeniedException ignored) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(HttpStatus.FORBIDDEN,
                                           "Resource access denied",
                                           new ArrayList<>()));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ApiErrorResponse>
    handleConstraintException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();

        ex.getConstraintViolations()
                .forEach(violation -> errors.add(String.format("%s %s",
                                                               violation.getPropertyPath().toString(),
                                                               violation.getMessage())));

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT,
                                           "Constraint violation",
                                           errors));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<ApiErrorResponse>
    handleDataException(DataIntegrityViolationException ex) {
        String constraintName = ((org.hibernate.exception.ConstraintViolationException) ex.getCause()).getConstraintName();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT,
                                           "Data integrity violation",
                                           List.of(String.format("Duplicate entry: %s", constraintName))));
    }

    @ExceptionHandler(value = {ApiException.class})
    protected ResponseEntity<ApiErrorResponse>
    handleApiException(ApiException ex) {
        // 204 No Content
        // Response has nothing even if response body was sent
        return ResponseEntity
                .status(ex.getResponse().getStatus())
                .body(new ApiErrorResponse(ex.getResponse().getStatus(),
                                           ex.getResponse().getMessage(),
                                           ex.getResponse().getErrors()));
    }
}
