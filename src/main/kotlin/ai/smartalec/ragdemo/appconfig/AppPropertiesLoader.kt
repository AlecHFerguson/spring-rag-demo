package ai.smartalec.ragdemo.appconfig

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths

@Component
data class AppPropertiesLoader(
    @Value("ai.smartalec.markdown.data-directory") private val dataDirectoryRaw: String,
) {
    val dataDirectory: Path by lazy { Paths.get(dataDirectoryRaw) }
}
