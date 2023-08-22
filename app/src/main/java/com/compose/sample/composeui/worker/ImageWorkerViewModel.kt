package com.compose.sample.composeui.worker

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID

class ImageWorkerViewModel : ViewModel() {

    var uri: Uri? by mutableStateOf(null)
        private set

    var bitmap: Bitmap? by mutableStateOf(null)
        private set

    var workId: UUID? by mutableStateOf(null)
        private set

    fun updateUri(uri: Uri) {
        this.uri = uri
    }

    fun updateBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    fun updateWorkId(id: UUID) {
        workId = id
    }
}