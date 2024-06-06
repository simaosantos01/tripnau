package com.desofs.backend.controllers;

import com.desofs.backend.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @Data
    public static class ErrorPayloadMessage {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return "";
    }

    private ErrorPayloadMessage createPayload(String message, HttpStatus status, WebRequest request) {
        ErrorPayloadMessage errorResponse = new ErrorPayloadMessage();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(message);
        errorResponse.setPath(this.getPath(request));
        return errorResponse;
    }

    private ErrorPayloadMessage createPayload(Exception exception, HttpStatus status, WebRequest request) {
        return createPayload(exception.getMessage(), status, request);
    }

    private ErrorPayloadMessage createPayload(Exception exception, WebRequest request, HttpStatus status, String msg) {
        ErrorPayloadMessage errorResponse = this.createPayload(exception, status, request);
        errorResponse.setMessage(msg);
        return errorResponse;
    }

    private static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append(", ");
        }

        if (!map.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidsArguments(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createPayload(exception, request, HttpStatus.BAD_REQUEST, mapToString(errorMap)));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.createPayload(exception, HttpStatus.NOT_FOUND, request));
    }

    @ExceptionHandler({IllegalArgumentException.class, DatabaseException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createPayload(exception, HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler({BadCredentialsException.class, NotAuthorizedException.class})
    public ResponseEntity<Object> handleForbiddenExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(this.createPayload(exception, HttpStatus.FORBIDDEN, request));
    }

    @ExceptionHandler({UnavailableTimeInterval.class})
    public ResponseEntity<Object> handleUnavailableTimeIntervalExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(this.createPayload(exception, HttpStatus.CONFLICT, request));
    }

    @ExceptionHandler(UpdatePasswordException.class)
    public ResponseEntity<Object> handleWrongPasswordExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createPayload(exception, HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericExceptions(Exception exception, WebRequest request) {
        log.error("Not caught exception: {}", exception.getMessage());
        // BEGIN-NOSCAN
        exception.printStackTrace();
        // END-NOSCAN
        String genericMsg = "Some generic error occurred. Contact administrator.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createPayload(genericMsg, HttpStatus.BAD_REQUEST, request));
    }
}
