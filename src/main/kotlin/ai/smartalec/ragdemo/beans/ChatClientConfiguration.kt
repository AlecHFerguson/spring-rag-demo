package ai.smartalec.ragdemo.beans

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
final class ChatClientConfiguration(
    private val vectorStore: VectorStore,
    private val chatMemory: ChatMemory,
) {
    @Bean
    fun chatClient(
        @Qualifier("ollamaChatModel") chatModel: ChatModel,
    ): ChatClient =
        ChatClient
            .builder(chatModel)
            .defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                QuestionAnswerAdvisor.builder(vectorStore).build(),
            ).build()
}
