package ai.smartalec.ragdemo.advisor

import ai.smartalec.ragdemo.model.exception.MissingPromptException
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.AdvisorChain
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.document.Document
import java.util.stream.Collectors

private const val DEFAULT_ORDER = 0
private val DEFAULT_PROMPT_TEMPLATE =
    PromptTemplate(
        """
        {query}

        Context information is below, surrounded by ---------------------

        ---------------------
        {question_answer_context}
        ---------------------

        Given the context and provided history information and not prior knowledge,
        reply to the user comment. If the answer is not in the context, inform
        the user that you can't answer the question.
        
        """.trimIndent(),
    )

abstract class AbstractQuestionAnswerAdvisor(
    private val promptTemplate: PromptTemplate = DEFAULT_PROMPT_TEMPLATE,
    private val order: Int = DEFAULT_ORDER,
) : BaseAdvisor {
    abstract fun retrieveDocuments(userQuery: String): List<Document>

    override fun before(
        chatClientRequest: ChatClientRequest,
        advisorChain: AdvisorChain,
    ): ChatClientRequest {
        val userQuery = chatClientRequest.prompt.userMessage.text ?: throw MissingPromptException()

        val documents = retrieveDocuments(userQuery = userQuery)

        // 2. Create the context from the documents.
        val context: MutableMap<String, Any?> = HashMap(chatClientRequest.context())
        context[QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS] = documents

        val documentContext =
            documents
                .stream()
                .map<String?> { obj: Document? -> obj!!.getText() }
                .collect(Collectors.joining(System.lineSeparator()))

        // 3. Augment the user prompt with the document context.
        val userMessage = chatClientRequest.prompt().getUserMessage()
        val augmentedUserText: String =
            this.promptTemplate
                .render(mutableMapOf<String, Any>(Pair("query", userMessage.text as Any), Pair("question_answer_context", documentContext)))

        // 4. Update ChatClientRequest with augmented prompt.
        return chatClientRequest
            .mutate()
            .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
            .context(context)
            .build()
    }

    override fun after(
        chatClientResponse: ChatClientResponse,
        advisorChain: AdvisorChain,
    ): ChatClientResponse {
        val chatResponseBuilder = ChatResponse.builder()
        if (chatClientResponse.chatResponse() != null) {
            chatResponseBuilder.from(chatClientResponse.chatResponse()!!)
        }
        if (chatClientResponse.context().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS) != null) {
            chatResponseBuilder.metadata(
                QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS,
                chatClientResponse.context()[QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS]!!,
            )
        }
        return ChatClientResponse
            .builder()
            .chatResponse(chatResponseBuilder.build())
            .context(chatClientResponse.context())
            .build()
    }

    override fun getOrder(): Int = this.order
}
