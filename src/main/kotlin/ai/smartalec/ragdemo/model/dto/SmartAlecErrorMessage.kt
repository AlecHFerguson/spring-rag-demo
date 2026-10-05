package ai.smartalec.ragdemo.model.dto

data class SmartAlecErrorMessage(
    val statusCode: Int,
    val messages: List<String>,
)
