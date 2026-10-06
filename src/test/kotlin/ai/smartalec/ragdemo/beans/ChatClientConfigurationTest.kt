package ai.smartalec.ragdemo.beans

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore

internal class ChatClientConfigurationTest {
    @Test
    internal fun testChatClient() {
        val mockVectorStore = mockk<VectorStore>()
        val mockChatMemory = mockk<ChatMemory>()
        val mockChatModel = mockk<ChatModel>()
        val testBean = ChatClientConfiguration(vectorStore = mockVectorStore, chatMemory = mockChatMemory)
        assertDoesNotThrow { testBean.chatClient(mockChatModel) }
    }
}
