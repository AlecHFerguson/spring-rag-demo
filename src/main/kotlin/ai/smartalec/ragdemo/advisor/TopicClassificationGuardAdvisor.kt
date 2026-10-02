package ai.smartalec.ragdemo.advisor

import ai.smartalec.ragdemo.model.exception.OffTopicQueryException
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore

private const val DEFAULT_SIMILARITY_THRESHOLD = 0.35

class TopicClassificationGuardAdvisor(
    private val vectorStore: VectorStore,
    private val similarityThreshold: Double = DEFAULT_SIMILARITY_THRESHOLD,
) : AbstractQuestionAnswerAdvisor() {
    private val name = this::class.simpleName!!

    override fun getName(): String = name

    override fun retrieveDocuments(userQuery: String): List<Document> {
        val searchRequest =
            SearchRequest
                .builder()
                .query(userQuery)
                .similarityThreshold(similarityThreshold)
                .build()
        val vectorResult = vectorStore.similaritySearch(searchRequest)
        if (vectorResult.isEmpty()) {
            throw OffTopicQueryException()
        }
        return vectorResult
    }
}
