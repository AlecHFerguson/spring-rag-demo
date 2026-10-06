package ai.smartalec.ragdemo.advisor

import ai.smartalec.ragdemo.model.exception.OFF_TOPIC_EXCEPTION_MESSAGE
import ai.smartalec.ragdemo.model.exception.OffTopicQueryException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import kotlin.test.assertEquals

private const val DOCUMENT_ONE_CONTENT = "The meaning of life is ..."
private const val DOCUMENT_TWO_CONTENT = "Just a shot away"
private const val TEST_PROMPT = "What is the meaning of life?"

private const val EXPECTED_PROMPT = """What is the meaning of life?

Context information is below, surrounded by ---------------------

---------------------
The meaning of life is ...
Just a shot away
---------------------

Given the context and provided history information and not prior knowledge,
reply to the user comment. If the answer is not in the context, inform
the user that you can't answer the question.
"""

internal class TopicClassificationGuardAdvisorTest {
    @Test
    fun testDocumentsFound() {
        val vectorStore = mockk<VectorStore>()
        val documentOne = Document(DOCUMENT_ONE_CONTENT, mapOf<String, Any>())
        val documentTwo = Document(DOCUMENT_TWO_CONTENT, mapOf<String, Any>())

        every { vectorStore.similaritySearch(any<SearchRequest>()) } returns listOf(documentOne, documentTwo)
        val subject = TopicClassificationGuardAdvisor(vectorStore)

        val request = ChatClientRequest.builder().prompt(Prompt(TEST_PROMPT)).build()
        val result = subject.before(request, mockk())

        val expectedPrompt = Prompt(EXPECTED_PROMPT)
        assertEquals(expectedPrompt, result.prompt)
    }

    @Test
    fun testEmptyResultsThrow() {
        val vectorStore = mockk<VectorStore>()
        every { vectorStore.similaritySearch(any<SearchRequest>()) } returns emptyList()
        val subject = TopicClassificationGuardAdvisor(vectorStore)

        val chatClientRequest = ChatClientRequest(Prompt("Run some query"), mapOf<String, Any>())
        val thrownException =
            assertThrows<OffTopicQueryException>("Expected exception") {
                subject.before(chatClientRequest, mockk())
            }
        assertEquals(OFF_TOPIC_EXCEPTION_MESSAGE, thrownException.message)
    }
}
