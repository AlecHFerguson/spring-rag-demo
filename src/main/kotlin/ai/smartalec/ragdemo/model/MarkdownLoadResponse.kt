package ai.smartalec.ragdemo.model

import java.time.ZonedDateTime

data class MarkdownLoadResponse(
    val message: String = "Markdown Load started",
    val startTime: ZonedDateTime = ZonedDateTime.now(),
)
