package ai.smartalec.ragdemo.controller

import ai.smartalec.ragdemo.beans.MarkdownLoaderService
import ai.smartalec.ragdemo.model.MarkdownLoadResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/admin")
class AdminController(
    private val markdownLoaderService: MarkdownLoaderService,
) {
    @PostMapping("/load-markdown")
    fun loadMarkdown(): ResponseEntity<MarkdownLoadResponse> {
        markdownLoaderService.initLoadMarkdown()

        return ResponseEntity.ok(MarkdownLoadResponse())
    }
}
