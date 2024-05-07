package com.compose.sample.composeui2.receivecontent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


// use implementation("androidx.compose.ui:ui:1.7.0-alpha01")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModifierReceiveContent() {
    Box(modifier = Modifier.fillMaxSize()) {

        var text by remember {
            mutableStateOf("")
        }

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth()
        )
    }

}