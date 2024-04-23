package com.compose.sample.kotlin.html

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun Sequence<String>.convert(content: String, text: String) =
    map { word ->
        if (word == text) {
            content
        } else if (word.contains(text)
            && (word.length == text.length + 1) && !word.last().isLetter()
        ) {
            content + word.last()
        } else {
            word
        }
    }

fun parseParagraph(sb: StringBuilder, e: Element, content: String): StringBuilder {
    var sequence = content.splitToSequence(" ")
    var ptext = sequence.joinToString(" ")

    val children = e.children()
    for (child in children) {
        val tName = child.tagName()
        val childText = child.text()
        when (tName) {
            "a" -> {
                val link = child.attr("href")
                val a = a(childText, link)
                sequence = sequence.convert(a, childText)
                ptext = sequence.joinToString(" ")
            }

            "code" -> {
                val codeText = code(childText)
                sequence = sequence.convert(codeText, childText)
                ptext = sequence.joinToString(" ")

                val atag = child.getElementsByTag("a")
                if (atag != null) {
                    val text = atag.text()
                    if (text.isNotEmpty()) {
                        val link = atag.attr("href")
                        val alink = a(codeText, link)

                        sequence = sequence.convert(alink, codeText)
                        ptext = sequence.joinToString(" ")
                    }
                }
            }

            "strong" -> {
                val strongText = bold(childText)
                ptext = ptext.replace(childText, strongText)
                sequence = ptext.splitToSequence(" ")
            }
        }
    }
    return sb.append(ptext)
}

suspend fun getGistCode(url: String): String {
    return suspendCancellableCoroutine { cont ->
        try {
            val connect = URL(url).openConnection() as HttpsURLConnection
            val content = connect.inputStream.bufferedReader().use {
                it.readText()
            }
            println("content===done===${content}")
            cont.resumeWith(Result.success(content))
            cont.invokeOnCancellation {
            }
        } catch (e: Exception) {
            println("exception======${e.message}")
            cont.resumeWith(Result.failure(e))
        }
    }
}

suspend fun getGistCode2(url: String): String? {
    return try {

        withContext(Dispatchers.IO) {
            val connect = URL(url).openConnection() as HttpsURLConnection
            val content = connect.inputStream.bufferedReader().use {
                it.readText()
            }
            content
        }
    } catch (e: Exception) {
        e.message
    }
}
//
//fun getFrame(url:String) {
//    try {
//        val webClient = WebClient(BrowserVersion.CHROME).apply {
//            options.isThrowExceptionOnScriptError = false
//        }
//        val page = webClient.getPage<HtmlPage>(url)
//        val iframe = page.getElementsById("tcaptcha_transform_dy")
//        val div = page.querySelectorAll("div")
//        val title = page.getElementsByTagName("title")
//        println("title==${title}")
//        println("div==${div}")
//        println("iframe==${iframe}")
//        page.frames.forEach { frame->
//            val src = frame.frameElement.srcAttribute
//            println("frame src===$src")
//        }
//        println("done======")
//    } catch (e: Exception) {
//        println("htmlUnit error===${e.message}")
//    }
//}

suspend fun parseMedium(
    url: String,
    des: String,
    error: (String?) -> Unit
): String {
    try {
        // val fileurl = "G:/resource/桌面/2.mhtml"
//        val fileurl = "G:/resource/桌面/3.html"
//        val fileurl = "G:/resource/桌面/4.html"
//        val fileurl = "G:/resource/桌面/spancode.html"
//        val input = File(fileurl)
//        var doc = Jsoup.parse(input)

//        val u = "https://gist.github.com/LloydBlv/22d2a02330a2c831fab81f84c357bc66/raw/32e50b4b3bd1d133017d8069c407e8f979e71641/MutableStateExpose.kt"
//        val code = getGistCode(u)
//        println("code===$code")

        val connect = Jsoup.connect(url)
        var doc = connect.get()

        val hSet = HashSet<String>()
        val sb = StringBuilder()

        var allElements = doc.allElements
        // get the first article element then parse to doc
        for (e in allElements) {
            val tagName = e.tagName()
            when (tagName) {
                "article" -> {
                    val articleHtml = e.html()
                    doc = Jsoup.parse(articleHtml)
                    break;
                }
            }
        }

        allElements = doc.allElements
        for (e in allElements) {
            val tagName = e.tagName()
            when (tagName) {
                "div" -> {
                    val attr = e.attr("role")
                    if (attr != null && attr.equals("separator")) {
                        sb.append(separator()).br().br()
                    }
//                    val className = e.className()
//                    println("gist classname====$className")
//                    if (className.equals("gist-meta")) {
//                        println("getGistCode========")
//                        for (child in e.children()) {
//                            val codeUrl = child.attr("href")
//                            val gistCode = getGistCode(codeUrl)
//                            println("gistcode=== $gistCode")
//                            val formatCode = formatCode(gistCode)
//                            sb.append(formatCode).br().br()
////
////                            val gistLink = a("view raw", codeUrl)
////                            sb.append(gistLink).br().br()
//
//                            break;
//                        }
//                    }

                    if (e.className() == EXTERNALURL_DIVCLASS) {
                        val ae = e.getElementsByTag("a")
                        val link = ae.attr("href")
                        val a = a(link, link)
                        sb.append(a).br().br()

                        val h2e = e.getElementsByTag("h2")
                        val h2Text = h2e.text()
                        hSet.add(h2Text)

                        val h3e = e.getElementsByTag("h3")
                        val h3Text = h3e.text()
                        hSet.add(h3Text)

                        val pe = e.getElementsByTag("p")
                        val pText = pe.text()
                        hSet.add(pText)
                    }
                }

                "h1" -> {
                    val h1 = h1(e.text())
                    sb.append(h1).br().br()
                }

                "h2" -> {
                    if (hSet.contains(e.text())) {
                        continue
                    }
                    val h2 = h2(e.text())
                    sb.append(h2).br().br()
                }

                "h3" -> {
                    if (hSet.contains(e.text())) {
                        continue
                    }
                    val h3 = h3(e.text())
                    sb.append(h3).br().br()
                }

                "p" -> {
                    if (e.parent()?.tagName().equals("blockquote") ||
                        hSet.contains(e.text())
                    ) {
                        continue
                    }
                    parseParagraph(sb, e, e.text()).br().br()
                }

                "pre" -> {
                    val html = e.html()
                    val result = parseSpanCode(html)
                    sb.append(formatCode(result)).br().br()
                }

                "img" -> {
//                val parent = e.parent()
//                val ptagname = parent.tagName()
//                if (ptagname.equals("picture")) {
//                    val imgUrl = e.attr("src")
//                    val imgText = img("", imgUrl)
//                    sb.append(imgText).br().br()
//                }
                }

                "blockquote" -> {
                    val quoteText = e.text()
                    sb.append(blockquote(quoteText)).br().br()
                }

                "ol" -> {
                    e.children().forEachIndexed { index, li ->
                        val litext = li.text()
                        val mdliString = ol(index + 1, litext)
                        parseParagraph(sb, li, mdliString).br().br()
                    }
                }

                "ul" -> {
                    e.children().forEachIndexed { index, li ->
                        val litext = li.text()
                        val mdliString = li(litext)
                        parseParagraph(sb, li, mdliString).br().br()
                    }
                }

                "figure" -> {

                    val fhtml = e.html()
                    println("fhtml===$fhtml")

                    val imgElement = e.getElementsByTag("img")
                    val imgUrl = imgElement.attr("src")
                    val imgText = img("", imgUrl)
                    sb.append(imgText).br()

                    sb.append(e.text()).br().br()
                }
                "iframe" -> {

                    val iframeHtml = e.html()
                    println("iframeHtml===$iframeHtml")

                    println("iframe==========")
                    for (f in e.children()) {
                        println("iframe========111==")
                        val className = f.className()
                        println("iframe===className=====$className==")
                        if (className == "gist-meta") {
                            println("getGistCode==========")
                            val a = f.getElementsByTag("a")
                            val codeUrl = a.attr("href")
                            val gistCode = getGistCode(codeUrl)
                            println("gistcode=== $gistCode")
                            val formatCode = formatCode(gistCode)
                            sb.append(formatCode).br().br()
                        }
                    }
                }
                "body" -> {

                    println("body========${e.html()}==")
                }
            }
        }

        //println(sb.toString())
        writeToFile(sb.toString(), des)
        return sb.toString()
    } catch (e: Exception) {
        error(e.message)
        return ""
    }
}

private const val EXTERNALURL_DIVCLASS = "rj rk rl rm rn ro"

fun parseSpanCode(html: String): String {
    val spanList = html.split("<br>")
    val sb = StringBuilder()
    spanList.forEach { s ->
        s.split("</span>").forEach { ss ->
            val code = removeSpan(ss)
            sb.append(code)
        }
        sb.br()
    }
    return sb.toString()
}

private fun removeSpan(s: String): String {
    val arrayIndex = ArrayList<Array<Int>>()
    var indexArray = Array(2) { -1 }
    s.forEachIndexed { index, c ->
        if (c == '<') {
            indexArray = Array(2) { -1 }
            indexArray[0] = index
        } else if (c == '>') {
            indexArray[1] = index
            arrayIndex.add(indexArray)
        }
    }
    var result = s
    arrayIndex.forEach { intArray ->
        val slice = s.slice(IntRange(intArray[0], intArray[1]))
        result = result.replace(slice, "")
    }
    return result
}

fun writeToFile(content: String, des: String) {
    println("des===$des")
    val file = File(des)
    if (!file.exists()) {
        file.createNewFile()
    }
    val bufferWriter = OutputStreamWriter(FileOutputStream(file), "utf-8")

    bufferWriter.write(content)
    bufferWriter.close()
}
