package ai.smartalec.ragdemo.scripts

import io.xberg.htmltomarkdown.HtmlToMarkdown
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

private const val BASE_URL = "https://training.caltopo.com"
const val OUTPUT_DIRECTORY = "/Users/alecferguson/scratch/caltopo-help"

// TODO: md
const val MARKDOWN_SUFFIX = "csv"
const val MARKDOWN_FILE_PATH = "$OUTPUT_DIRECTORY/*.$MARKDOWN_SUFFIX"

private val pipeRegexp = Regex("[|]")
private val spaceRegexp = Regex("\\s+")

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
    val underscoreTitle = html.title().replace(pipeRegexp, "").replace(spaceRegexp, "_")
    val file = File("$OUTPUT_DIRECTORY/$underscoreTitle.$MARKDOWN_SUFFIX")
    file.writeText(markdown.content!!)
}
