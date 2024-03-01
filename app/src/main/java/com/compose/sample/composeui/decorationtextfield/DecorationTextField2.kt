package com.compose.sample.composeui.decorationtextfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DecorationTextField2(
    text: String,
    onTextChanged: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier.drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color(0xFFEFEFEF),
                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFA8A8A8)
                    )
                    innerTextField()
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    )
}


@Preview
@Composable
fun DecorationTextField2Preview() {
    DecorationTextField2(
        "hell",
        {}
    )
}