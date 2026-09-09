package ai.smartalec.ragdemo.model.dto

data class ChatRequest(
    val message: String,
    val conversationId: String?,
)
