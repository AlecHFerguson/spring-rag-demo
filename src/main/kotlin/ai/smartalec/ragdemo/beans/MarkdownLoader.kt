package ai.smartalec.ragdemo.beans

import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class MarkdownLoader(
    private val vectorStore: VectorStore,
    @Value("classpath:data/TestMenu.md") private val markdownResource: Resource,
) : InitializingBean {
    override fun afterPropertiesSet() {
        val reader = MarkdownDocumentReader(markdownResource, MarkdownDocumentReaderConfig.defaultConfig())
        vectorStore.add(reader.read())
    }
}
