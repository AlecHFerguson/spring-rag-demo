package ai.smartalec.ragdemo.controller

import ai.smartalec.ragdemo.model.dto.SmartAlecErrorMessage
import ai.smartalec.ragdemo.model.exception.OffTopicQueryException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(OffTopicQueryException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOffTopicException(exception: OffTopicQueryException): ResponseEntity<SmartAlecErrorMessage> {
        return ResponseEntity.badRequest().body(SmartAlecErrorMessage(statusCode = HttpStatus.BAD_REQUEST.value(), messages = listOf(exception.message)))
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(exception: Exception): ResponseEntity<SmartAlecErrorMessage> {
        println(exception.message)
        return ResponseEntity.internalServerError().body(
            SmartAlecErrorMessage(statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(), messages = listOf("Something went wrong, please try again later"))
        )
    }
}
