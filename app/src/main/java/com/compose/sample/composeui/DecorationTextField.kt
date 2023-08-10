package com.compose.sample.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DecorationTextField(
    text: String,
    onTextChanged: (String) -> Unit
) {


    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                text.forEachIndexed { index, c ->
                    DuckieTextFieldCharContainer(
                        text = c,
                        isFoucused = index == text.lastIndex
                    )
                }
                repeat(5 - text.length) {
                    DuckieTextFieldCharContainer(
                        text = ' ',
                        isFoucused = false
                    )
                }
            }
        }
    )
}

@Composable
fun DuckieTextFieldCharContainer(
    modifier: Modifier = Modifier,
    text: Char,
    isFoucused: Boolean
) {
    val shape = remember { RoundedCornerShape(4.dp) }

    Box(
        modifier = modifier
            .size(width = 29.dp, height = 40.dp)
            .background(color = Color(0xFFF6F6F6), shape = shape)
            .run {
                if (isFoucused) {
                    border(
                        color = Color(0xFFFF8300),
                        width = 1.dp,
                        shape = shape
                    )
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text.toString())
    }
}

@Preview
@Composable
fun DecorationTextFieldPreview() {
    DecorationTextField(
        "hell",
        {}
    )
}