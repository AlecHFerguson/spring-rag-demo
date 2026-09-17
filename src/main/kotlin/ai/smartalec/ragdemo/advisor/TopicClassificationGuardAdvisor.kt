package ai.smartalec.ragdemo.advisor

import ai.smartalec.ragdemo.model.exception.OffTopicQueryException
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered

class TopicClassificationGuardAdvisor(
    val vectorStore: VectorStore,
) : CallAdvisor {
    override fun adviseCall(
        chatClientRequest: ChatClientRequest,
        callAdvisorChain: CallAdvisorChain,
    ): ChatClientResponse {
        val userQuery = chatClientRequest.prompt.contents
        val searchRequest =
            SearchRequest
                .builder()
                .query(userQuery)
                .topK(1)
                .similarityThreshold(0.75)
                .build()
        val vectorResult = vectorStore.similaritySearch(searchRequest)
        if (vectorResult.isEmpty()) {
            throw OffTopicQueryException()
        }
        return callAdvisorChain.nextCall(chatClientRequest)
    }

    override fun getName(): String = this::class.simpleName!!

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
