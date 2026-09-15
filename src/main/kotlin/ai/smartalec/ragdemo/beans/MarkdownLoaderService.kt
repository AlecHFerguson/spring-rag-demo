package ai.smartalec.ragdemo.beans

import ai.smartalec.ragdemo.appconfig.AppPropertiesLoader
import ai.smartalec.ragdemo.model.Constants.MARKDOWN_SUFFIX
import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.FileSystemResource
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.nio.file.Files

private const val LOWER_CASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz"
private const val FILE_PATTERN = "*$MARKDOWN_SUFFIX"

@Service
class MarkdownLoaderService(
    private val vectorStore: VectorStore,
    private val appPropertiesLoader: AppPropertiesLoader,
) {
    @Async
    fun initLoadMarkdown() {
        var fileCount = 0
        truncateTable()
        Files.newDirectoryStream(appPropertiesLoader.dataDirectory, FILE_PATTERN).use { entryStream ->
            for (file in entryStream) {
                fileCount++
                val fileSystemResource = FileSystemResource(file)
                val reader = MarkdownDocumentReader(fileSystemResource, MarkdownDocumentReaderConfig.defaultConfig())
                vectorStore.add(reader.read())
            }
        }
        println("Loaded $fileCount markdown files")
    }

    private fun truncateTable() {
        var documentsFound = true
        var deleteCount = 0
        while (documentsFound) {
            val searchRequest =
                SearchRequest
                    .builder()
                    .query(LOWER_CASE_ALPHABET)
                    .similarityThreshold(SIMILARITY_THRESHOLD_ACCEPT_ALL)
                    .topK(1000)
                    .build()
            val documentsToDelete = vectorStore.similaritySearch(searchRequest)
            documentsFound = documentsToDelete.isNotEmpty()
            val idsToDelete = documentsToDelete.map { it.id }
            deleteCount += idsToDelete.size
            vectorStore.delete(idsToDelete)
        }
        println("Deleted $deleteCount markdown files")
    }
}
