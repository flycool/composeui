package com.compose.sample.composeui.visualTransformation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TextFieldVisualTransformation() {
    var text by remember {
        mutableStateOf("15641")
    }
    TextField(
        value = text,
        onValueChange = {
            text = it.takeWhile { it.isDigit() }
        },
        visualTransformation = CreditCardVisualTransformation(),
    )
}

