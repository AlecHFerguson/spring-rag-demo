package ai.smartalec.ragdemo.scripts

import io.xberg.htmltomarkdown.HtmlToMarkdown
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

const val OUTPUT_DIRECTORY = "/Users/alecferguson/scratch/caltopo-help"
const val BASE_URL = "https://training.caltopo.com"

fun main() {
    val topLevelResponse = makeRequestAndParse(BASE_URL)

    val links = parseUrlsFromTopLevelResponse(topLevelResponse)
    for (link in links) {
        val response = makeRequestAndParse(link)
        writeMarkdownFile(response)
    }
}

private fun makeRequestAndParse(url: String): Document = Jsoup.connect(url).timeout(4321).get()

private fun parseUrlsFromTopLevelResponse(document: Document): List<String> {
    val elements = document.select("li[data-nav-id]")
    return elements.mapNotNull { it.attr("data-nav-id") }.map { "$BASE_URL/$it" }
}

private fun writeMarkdownFile(html: Document) {
    val markdown = HtmlToMarkdown.convert(html.html())
    val file = File("$OUTPUT_DIRECTORY/${html.title()}")
    file.writeText(markdown.content!!)
}
