package com.compose.sample.composeui.annotationstring

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
@Composable
fun BasicTextStyle() {

    val boldText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Bold text")
        }
        append("\n\n")
        append("Regular Text")
    }
    val colorText = buildAnnotatedString {
        withStyle(style = SpanStyle(background = Color.Yellow)) {
            append("Background Color text")
        }
        append("\n\n")
        append("Regular Text")
    }

    val annotationLinkString = linkString()
    val uriHandler = LocalUriHandler.current




    Column {
        Text(text = boldText)
        Text(text = colorText)
        ClickableText(text = annotationLinkString, onClick = {
            annotationLinkString.getUrlAnnotations(it, it)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item.url)
                }
        })
        ExpandableText()

        Social()
    }
}

@OptIn(ExperimentalTextApi::class)
private fun linkString(): AnnotatedString {
    return buildAnnotatedString {
        val str = "Let's open baidu"
        val startIndex = str.indexOf("baidu")
        val endIndex = startIndex + "baidu".length
        append(str)
        addStyle(
            style = SpanStyle(
                color = Color.Red,
                textDecoration = TextDecoration.Underline
            ),
            start = startIndex,
            end = endIndex
        )
        addUrlAnnotation(
            UrlAnnotation("https://www.baidu.com"),
            start = startIndex,
            end = endIndex
        )
    }
}

@Composable
private fun ExpandableText() {
    val tagExpanded = "expanded_text"
    val tagMinified = "minified_text"
    val textToggleState = remember {
        mutableStateOf(Pair(tagMinified, "Read more..."))
    }

    val expandableTextString = buildAnnotatedString {
        val toggleString = textToggleState.value.second
        val snippet = "In a groundbreaking discovery, scientists " +
                "have identified a new species of marine life " +
                "in the deep sea. $toggleString"
        val startIndex = snippet.indexOf(toggleString)
        val endIndex = startIndex + toggleString.length

        withStyle(style = SpanStyle(fontSize = 24.sp)) {
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(snippet)
            }
            if (textToggleState.value.first == tagExpanded) {
                append(
                    "\n\nThis new species, tentatively named " +
                            "'DeepSea Marvel,' was found at a depth " +
                            "of 4,000 meters beneath the ocean's surface."
                )
            }
        }

        addStyle(
            style = SpanStyle(
                color = Color.Magenta,
                textDecoration = TextDecoration.Underline
            ),
            start = startIndex,
            end = endIndex
        )

        if (textToggleState.value.first == tagExpanded) {
            addStringAnnotation(
                tagMinified,
                "Read again...",
                start = startIndex,
                end = endIndex
            )
        } else {
            addStringAnnotation(
                tagExpanded,
                "Show less...",
                start = startIndex,
                end = endIndex
            )
        }
    }
    ClickableText(text = expandableTextString, onClick = {
        expandableTextString.getStringAnnotations(it, it)
            .firstOrNull()?.let { annotation ->
                if (annotation.tag == tagExpanded) {
                    textToggleState.value = Pair(tagExpanded, annotation.item)
                } else {
                    textToggleState.value = Pair(tagMinified, annotation.item)
                }
            }
    })
}

@Preview
@Composable
fun BasicTextStylePreview() {
    BasicTextStyle()
}