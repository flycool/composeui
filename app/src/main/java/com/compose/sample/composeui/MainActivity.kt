package com.compose.sample.composeui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.compose.sample.composeui.annotationstring.BasicTextStyle
import com.compose.sample.composeui.canvas.BouncingBallGame
import com.compose.sample.composeui.containertransform.AddContentScreen
import com.compose.sample.composeui.containertransform.ContainerTransformScreen
import com.compose.sample.composeui.custommodifier.GreyscaleScene
import com.compose.sample.composeui.decorationtextfield.DecorationTextField
import com.compose.sample.composeui.draganddrop.HorizontalPagerContent
import com.compose.sample.composeui.dragfood.DragDropMainScreen
import com.compose.sample.composeui.dragfood.model.foodItems
import com.compose.sample.composeui.edgetoedge.NavEdgeToEdgeScreen
import com.compose.sample.composeui.emoji.FireEmoji
import com.compose.sample.composeui.indicator.AnimatedCircularProgressIndicator
import com.compose.sample.composeui.invitationcard.ThreadsInviteCard
import com.compose.sample.composeui.navigationbar.BottomNavBarScreen
import com.compose.sample.composeui.nestedscroll.SampleNetfixLazyScreen
import com.compose.sample.composeui.overlaprow.CustomOverlappingRow
import com.compose.sample.composeui.permission.OptionalSinglePermission
import com.compose.sample.composeui.polygonShapeASLoader.PolygonShapeASLoader
import com.compose.sample.composeui.tabrow.SwipeableTabRow
import com.compose.sample.composeui.timeline.DATA
import com.compose.sample.composeui.timeline.TimelineContent
import com.compose.sample.composeui.ui.theme.ComposeuiTheme
import com.compose.sample.composeui.visualTransformation.TextFieldVisualTransformation
import com.compose.sample.composeui.worker.ImageCompressWorker
import com.compose.sample.composeui.worker.ImageWorkerViewModel
import com.compose.sample.funui.splashscreen.SplashScreenViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val imageViewModel by viewModels<ImageWorkerViewModel>()
    private val splashScreenViewModel by viewModels<SplashScreenViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
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
                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") {
                        LandingScreen(naviate = {
                            navController.navigate(it.route)
                        })
                    }
                    Destination.entries.forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                Destination.annotationstring -> BasicTextStyle()
                                Destination.canvas -> BouncingBallGame()
                                Destination.containertransform -> ContainerTransformScreen()
                                Destination.decorationtextfield -> DecorationTextField()
                                Destination.dragfood -> DragDropMainScreen(foodItems)
                                Destination.edgetoedge -> NavEdgeToEdgeScreen()
                                Destination.emoji -> FireEmoji()
                                Destination.indicator -> AnimatedCircularProgressIndicator()
                                Destination.navigationbar -> BottomNavBarScreen()
                                Destination.netstedscroll -> SampleNetfixLazyScreen()
                                Destination.overlaprow -> CustomOverlappingRow()
                                Destination.permission -> OptionalSinglePermission()
                                Destination.polygonloader -> PolygonShapeASLoader()
                                Destination.tabrow -> SwipeableTabRow()
                                Destination.visualtransformation -> TextFieldVisualTransformation()
                                Destination.CustomModifier -> GreyscaleScene(isDisabled = true)
                                Destination.TimeLine -> TimelineContent(stages = DATA)
                                Destination.InvitationCard -> ThreadsInviteCard()
                            }
                        }
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

