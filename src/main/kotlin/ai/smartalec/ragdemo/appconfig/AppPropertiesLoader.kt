package ai.smartalec.ragdemo.appconfig

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path
import java.nio.file.Paths

@ConfigurationProperties("ai.smartalec")
data class AppPropertiesLoader(
    private val markdownDirectory: String,
) {
    val dataDirectory: Path by lazy { Paths.get(markdownDirectory) }
}
