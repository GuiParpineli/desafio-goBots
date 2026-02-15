package com.gobots.marketplaceapi.adapter.configuration.exception

import com.gobots.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map {
            FieldError(
                field = it.field,
                rejected = it.rejectedValue,
                message = it.defaultMessage,
            )
        }

        log.warn("Validation failed: {}", errors)

        return ResponseEntity.badRequest().body(
            ValidationErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Validation failed",
                errors = errors,
            )
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ValidationErrorResponse> {
        return ResponseEntity.badRequest().body(
            ValidationErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = ex.message ?: "Invalid argument",
                errors = emptyList(),
            )
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundException(ex: NotFoundException): ResponseEntity<ValidationErrorResponse> {
        return ResponseEntity.notFound().build()
    }
}