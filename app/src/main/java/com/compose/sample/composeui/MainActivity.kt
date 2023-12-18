package com.compose.sample.composeui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.compose.sample.composeui.ui.theme.ComposeuiTheme
import com.compose.sample.composeui.worker.ImageCompressWorker
import com.compose.sample.composeui.worker.ImageWorkerViewModel
import com.compose.sample.funui.pickmedia.PickVisualMedia

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val imageViewModel by viewModels<ImageWorkerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)
        setContent {
            ComposeuiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PickVisualMedia()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        } ?: return

        imageViewModel.updateUri(uri)

        val request = OneTimeWorkRequestBuilder<ImageCompressWorker>()
            .setInputData(
                workDataOf(
                    ImageCompressWorker.KEY_CONTENT_URI to uri.toString(),
                    ImageCompressWorker.KEY_COMPRESS_THRESHOLD to 1024 * 20L
                )
            )
            .setConstraints(Constraints(requiresStorageNotLow = true))
            .build()
        imageViewModel.updateWorkId(request.id)
        workManager.enqueue(request)
    }
}

