package com.compose.sample.composeui.bookmarkbutton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

sealed interface BookmarkState {
    data object Bookmarked : BookmarkState
    data object NotBookmarked : BookmarkState
    data object Toggling : BookmarkState
}

@Composable
fun BookmarkButton(
    state: BookmarkState,
    modifier: Modifier = Modifier,
    onBookmarkClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onBookmarkClick,
        enabled = state != BookmarkState.Toggling,
    ) {
        when (state) {
            is BookmarkState.Toggling -> CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            BookmarkState.Bookmarked -> Text(text = "Bookmarked")
            BookmarkState.NotBookmarked -> Text(text = "Bookmark")
        }
    }
}

@Preview
@Composable
fun BookmarkButtonScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var bookmarked by rememberSaveable { mutableStateOf(false) }
        var toggling by rememberSaveable { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        BookmarkButton(
            modifier = Modifier,
            state = when {
                toggling -> BookmarkState.Toggling
                bookmarked -> BookmarkState.Bookmarked
                else -> BookmarkState.NotBookmarked
            },
        ) {
            scope.launch {
                toggling = true
                delay(1.seconds)
                toggling = false
                bookmarked = !bookmarked
            }
        }
    }
}