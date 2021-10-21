package me.david.lengthconversionservice.web

import org.slf4j.Logger
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@ControllerAdvice
class ExceptionConverter(private val logger: Logger) {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        logger.warn("Error processing $request: ${ex.message}")
        val responseBody = mapOf(
                "timestamp" to Instant.now(),
                "message" to ex.message,
        )
        return ResponseEntity(responseBody, BAD_REQUEST)
    }
}