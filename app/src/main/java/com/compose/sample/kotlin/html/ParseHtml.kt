package com.compose.sample.kotlin.html

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

@Composable
fun ParseHtml() {
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            TextField(value = text, onValueChange = {
                text = it
            })
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
//                        val cacheDir = context.externalCacheDir
//                        val filePath = "$cacheDir/medium.md"
//                        println("filePath===$filePath")
//                        parseMedium(text, filePath) { error ->
//
//                        }
//                        val url = "https://proandroiddev.com/mastering-android-viewmodels-essential-dos-and-donts-part-2-%EF%B8%8F-2b49281f0029"
                        val url = "https://www.qidian.com/"
//                        val result = getGistCode2(url)
//                        println("result====$result")

                        //getFrame(url)

                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "click")
            }
        }
    }
}