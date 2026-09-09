package ai.smartalec.ragdemo.controller

import ai.smartalec.ragdemo.model.dto.ChatRequest
import ai.smartalec.ragdemo.model.dto.ChatResponse
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@RestController
final class ChatController(
    private val chatClient: ChatClient,
) {
    @PostMapping("/chat", consumes = [TEXT_PLAIN_VALUE], produces = [TEXT_PLAIN_VALUE])
    final fun chat(
        @RequestBody chatRequest: ChatRequest,
    ): ChatResponse {
        @OptIn(ExperimentalUuidApi::class)
        val conversationId = chatRequest.conversationId ?: Uuid.random().toString()
        val callResponse =
            chatClient
                .prompt()
                .advisors { advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId) }
                .user(chatRequest.message)
                .call()

        return ChatResponse(message = callResponse.content(), conversationId = conversationId)
    }
}
