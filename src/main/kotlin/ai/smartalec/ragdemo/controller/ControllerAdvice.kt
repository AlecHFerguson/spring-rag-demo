package ai.smartalec.ragdemo.controller

import ai.smartalec.ragdemo.model.dto.SmartAlecErrorMessage
import ai.smartalec.ragdemo.model.exception.MissingPromptException
import ai.smartalec.ragdemo.model.exception.OffTopicQueryException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

private val BAD_REQUEST_STATUS_CODE = HttpStatus.BAD_REQUEST.value()

@ControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(OffTopicQueryException::class, MissingPromptException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOffTopicException(exception: OffTopicQueryException): ResponseEntity<SmartAlecErrorMessage> =
        ResponseEntity.badRequest().body(
            SmartAlecErrorMessage(statusCode = BAD_REQUEST_STATUS_CODE, messages = listOf(exception.message)),
        )

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(exception: Exception): ResponseEntity<SmartAlecErrorMessage> {
        println(exception.message)
        return ResponseEntity.internalServerError().body(
            SmartAlecErrorMessage(
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messages = listOf("Something went wrong, please try again later"),
            ),
        )
    }
}
