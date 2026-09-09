package ai.smartalec.ragdemo.beans

import ai.smartalec.ragdemo.appconfig.AppPropertiesLoader
import ai.smartalec.ragdemo.scripts.MARKDOWN_SUFFIX
import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.FileSystemResource
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.nio.file.Files

private const val FILE_PATTERN = "*.$MARKDOWN_SUFFIX"

@Service
class MarkdownLoaderService(
    private val vectorStore: VectorStore,
    private val appPropertiesLoader: AppPropertiesLoader,
) {
    @Async
    fun initLoadMarkdown() {
        var fileCount = 0
        vectorStore.delete("")
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
}
