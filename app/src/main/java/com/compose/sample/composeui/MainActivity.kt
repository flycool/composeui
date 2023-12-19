package com.compose.sample.composeui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.compose.sample.PROJECT_GITHUB_URL
import com.compose.sample.composeui.ui.theme.ComposeuiTheme
import com.compose.sample.composeui.worker.ImageCompressWorker
import com.compose.sample.composeui.worker.ImageWorkerViewModel
import com.compose.sample.funui.pickmedia.PickVisualMedia
import com.compose.sample.funui.touchevent.TouchHeldButton
import com.compose.sample.goToScourceCode

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val imageViewModel by viewModels<ImageWorkerViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    val context = LocalContext.current
                    Scaffold(
                        bottomBar = {
                            BottomAppBar {
                                Text(text = "source code ->", modifier = Modifier.clickable {
                                    context.goToScourceCode()
                                })
                            }
                        }
                    ) {
                        TouchHeldButton()
                    }
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

