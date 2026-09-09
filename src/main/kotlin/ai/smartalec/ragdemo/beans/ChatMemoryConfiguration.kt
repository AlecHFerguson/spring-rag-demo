package ai.smartalec.ragdemo.beans

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
final class ChatMemoryConfiguration {
    @Bean
    fun createChatMemory(): ChatMemory =
        MessageWindowChatMemory
            .builder()
            .chatMemoryRepository(InMemoryChatMemoryRepository())
            .maxMessages(11)
            .build()
}
