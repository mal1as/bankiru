package com.itmo.blps.bankiru.exception

import com.itmo.blps.bankiru.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ValidationException

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message, HttpStatus.BAD_REQUEST.name, e.stackTraceToString()))
    }

    @ExceptionHandler
    fun handleSecurityException(e: AuthenticationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message, HttpStatus.UNAUTHORIZED.name, e.stackTraceToString()))
    }

    @ExceptionHandler
    fun handleInternalServerError(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(e.message, HttpStatus.INTERNAL_SERVER_ERROR.name, e.stackTraceToString()))
    }
}