package ai.smartalec.ragdemo.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatClient: ChatClient,
) {
    @PostMapping("/chat", consumes = [TEXT_PLAIN_VALUE], produces = [TEXT_PLAIN_VALUE])
    fun chat(
        @RequestBody chatMessage: String,
    ): String? {
        val conversationId = "007"
        return chatClient
            .prompt()
            .advisors { advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId) }
            .user(chatMessage)
            .call()
            .content()
    }
}
