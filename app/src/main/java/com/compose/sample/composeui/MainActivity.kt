package com.compose.sample.composeui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.compose.sample.composeui.annotationstring.BasicTextStyle
import com.compose.sample.composeui.canvas.BouncingBallGame
import com.compose.sample.composeui.containertransform.ContainerTransformScreen
import com.compose.sample.composeui.custommodifier.GreyscaleScene
import com.compose.sample.composeui.decorationtextfield.DecorationTextField
import com.compose.sample.composeui.draganddrop.indicator.Carousel
import com.compose.sample.composeui.dragfood.DragDropMainScreen
import com.compose.sample.composeui.edgetoedge.DrogBoxContentScreen
import com.compose.sample.composeui.edgetoedge.NavEdgeToEdgeScreen
import com.compose.sample.composeui.emoji.FireEmoji
import com.compose.sample.composeui.glovo.GlovoScreen
import com.compose.sample.composeui.indicator.AnimatedCircularProgressIndicator
import com.compose.sample.composeui.invitationcard.ThreadsInviteCard
import com.compose.sample.composeui.lookaheadscope.ContainerTransform
import com.compose.sample.composeui.lookaheadscope.LookAheadContent
import com.compose.sample.composeui.musicappui.playerprogressbar.ProgressBar
import com.compose.sample.composeui.nav.Route
import com.compose.sample.composeui.nav.mainGraph
import com.compose.sample.composeui.navigationbar.BottomNavBarScreen
import com.compose.sample.composeui.nestedscroll.SampleNetfixLazyScreen
import com.compose.sample.composeui.overlaprow.CustomOverlappingRow
import com.compose.sample.composeui.permission.OptionalSinglePermission
import com.compose.sample.composeui.polygonShapeASLoader.PolygonShapeASLoader
import com.compose.sample.composeui.slider.CustomSlider
import com.compose.sample.composeui.statdium.FootballGround
import com.compose.sample.composeui.tabrow.SwipeableTabRow
import com.compose.sample.composeui.timeline.DATA
import com.compose.sample.composeui.timeline.TimelineContent
import com.compose.sample.composeui.ui.theme.ComposeuiTheme
import com.compose.sample.composeui.visualTransformation.TextFieldVisualTransformation
import com.compose.sample.composeui.worker.ImageCompressWorker
import com.compose.sample.composeui.worker.ImageWorkerViewModel
import com.compose.sample.composeui2.biometriprompt.BiometricPromptScreen
import com.compose.sample.composeui2.pulltorefresh.PullToRefreshLazyColumnScreen
import com.compose.sample.composeui2.scanner.DocumentScannerScreen
import com.compose.sample.funui.splashscreen.SplashScreenViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private val imageViewModel by viewModels<ImageWorkerViewModel>()
    private val splashScreenViewModel by viewModels<SplashScreenViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)

//        installSplashScreen().apply {
//            setKeepOnScreenCondition {
//                !splashScreenViewModel.ready.value
//            }
//            setOnExitAnimationListener { screen ->
//                onExitSplashScreenAnimation(screen)
//            }
//        }

//        enableEdgeToEdge()

        setContent {
            ComposeuiTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.Landing) {
                    mainGraph(navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
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

