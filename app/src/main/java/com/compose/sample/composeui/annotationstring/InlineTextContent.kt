package com.compose.sample.composeui.annotationstring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun Social() {
    val dividerId = "inlineDividerId"
    val text = buildAnnotatedString {
        append(AnnotatedString("weibo ", spanStyle = SpanStyle(Color.Blue)))
        appendInlineContent(dividerId, "[divider]")
        append(AnnotatedString(" LinkedIn ", spanStyle = SpanStyle(Color.Blue)))
        appendInlineContent(dividerId, "[divider]")
        append(AnnotatedString(" Twitter", spanStyle = SpanStyle(Color.Blue)))
    }

    val inlineDividerContent = mapOf(
        Pair(
            dividerId,
            InlineTextContent(
                placeholder = Placeholder(
                    width = 0.15.em,
                    height = 0.90.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Box(
                    modifier = Modifier
                        .rotate(15f)
                        .fillMaxSize()
                        .clip(RectangleShape)
                        .background(Color.DarkGray)
                )
            }
        )
    )

    BasicText(
        text = text,
        inlineContent = inlineDividerContent,
        style = TextStyle(fontSize = 17.sp)
    )
}