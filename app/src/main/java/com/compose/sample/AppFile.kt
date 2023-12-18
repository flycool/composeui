package com.compose.sample

import android.content.Context
import android.content.Intent
import android.net.Uri

val PROJECT_GITHUB_URL = "https://github.com/flycool/composeui/tree/master/app/src/main/java/com/compose/sample"

fun Context.openUrl(url: String) {
    val uri = Uri.parse(url)
    startActivity(Intent(Intent.ACTION_VIEW, uri) )
}

fun Context.goToScourceCode() {
    openUrl(PROJECT_GITHUB_URL)
}