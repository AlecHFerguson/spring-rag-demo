package ai.smartalec.ragdemo.model.dto

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

data class SmartAlecErrorMessage(val statusCode: Int, val messages: List<String>)
