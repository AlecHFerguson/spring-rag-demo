package ai.smartalec.ragdemo.beans

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfiguration(
    private val vectorStore: VectorStore,
) {
    @Bean
    fun chatClient(
        @Qualifier("openAiChatModel") chatModel: ChatModel,
    ): ChatClient =
        ChatClient
            .builder(chatModel)
            .defaultAdvisors(
                QuestionAnswerAdvisor.builder(vectorStore).build(),
            ).build()
}
