package ai.smartalec.ragdemo.beans

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.messages.UserMessage

private const val CONVERSATION_ID = "conversant"

internal class ChatMemoryConfigurationTest {
    @Test
    internal fun testCreateChatMemory() {
        val subject = ChatMemoryConfiguration()
        val result = subject.createChatMemory()

        val testMessage = UserMessage("I like to eat bananas")
        result.add(CONVERSATION_ID, testMessage)
        assertEquals(listOf(testMessage), result.get(CONVERSATION_ID))
    }
}
