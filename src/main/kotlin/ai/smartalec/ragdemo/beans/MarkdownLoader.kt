package ai.smartalec.ragdemo.beans

import ai.smartalec.ragdemo.scripts.MARKDOWN_SUFFIX
import ai.smartalec.ragdemo.scripts.OUTPUT_DIRECTORY
import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

private val directory = Paths.get(OUTPUT_DIRECTORY)
private const val FILE_PATTERN = "*.$MARKDOWN_SUFFIX"

@Component
class MarkdownLoader(
    private val vectorStore: VectorStore,
) : InitializingBean {
    override fun afterPropertiesSet() {
        Files.newDirectoryStream(directory, FILE_PATTERN).use { entryStream ->
            for (file in entryStream) {
                val fileSystemResource = FileSystemResource(file)
                val reader = MarkdownDocumentReader(fileSystemResource, MarkdownDocumentReaderConfig.defaultConfig())
                vectorStore.add(reader.read())
            }
        }
    }
}
