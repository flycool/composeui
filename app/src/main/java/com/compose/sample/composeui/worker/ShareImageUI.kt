package com.compose.sample.composeui.worker

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import coil.compose.AsyncImage

@Composable
fun ShareImageUI(
    imageViewModel: ImageWorkerViewModel
) {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context.applicationContext)
    val workerResult = imageViewModel.workId?.let { id ->
        workManager.getWorkInfoByIdLiveData(id).observeAsState().value
    }
    LaunchedEffect(key1 = workerResult) {
        if (workerResult?.outputData != null) {
            val filePath =
                workerResult.outputData.getString(ImageCompressWorker.KEY_RESULT_PATH)
            filePath?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                imageViewModel.updateBitmap(bitmap)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageViewModel.uri?.let {
            Text(text = "Uncompressed photo:")
            AsyncImage(model = it, contentDescription = null)
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageViewModel.bitmap?.let {
            Text(text = "compressed photo:${it.byteCount} bytes")
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
        }
    }

}