package com.compose.sample.composeui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.compose.sample.composeui.edgetoedge.DragBoxContent
import com.compose.sample.composeui.edgetoedge.NavEdgeToEdgeScreen
import com.compose.sample.composeui.emoji.FireEmoji
import com.compose.sample.composeui.glovo.GlovoScreen
import com.compose.sample.composeui.indicator.AnimatedCircularProgressIndicator
import com.compose.sample.composeui.invitationcard.ThreadsInviteCard
import com.compose.sample.composeui.lookaheadscope.ContainerTransform
import com.compose.sample.composeui.lookaheadscope.LookAheadContent
import com.compose.sample.composeui.musicappui.playerprogressbar.ProgressBar
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
                NavHost(navController = navController, startDestination = Route.Landing) {
                    composable<Route.Landing> {
                        LandingScreen(naviate = {
                            navController.navigate(it)
                        })
                    }
                    composable<Route.AnnotationString> { BasicTextStyle() }
                    composable<Route.Canvas> { BouncingBallGame() }
                    composable<Route.Containertransform> { ContainerTransformScreen() }
                    composable<Route.DecorationTextfield> { DecorationTextField() }
                    composable<Route.Dragfood> { DragDropMainScreen() }
                    composable<Route.EdgeToEdge> { NavEdgeToEdgeScreen() }
                    composable<Route.Emoji> { FireEmoji() }
                    composable<Route.Indicator> { AnimatedCircularProgressIndicator() }
                    composable<Route.Navigationbar> { BottomNavBarScreen() }
                    composable<Route.Netstedscroll> { SampleNetfixLazyScreen() }
                    composable<Route.Overlaprow> { CustomOverlappingRow() }
                    composable<Route.Permission> { OptionalSinglePermission() }
                    composable<Route.Polygonloader> { PolygonShapeASLoader() }
                    composable<Route.Tabrow> { SwipeableTabRow() }
                    composable<Route.Visualtransformation> { TextFieldVisualTransformation() }
                    composable<Route.CustomModifier> { GreyscaleScene(isDisabled = true) }
                    composable<Route.TimeLine> { TimelineContent(stages = DATA) }
                    composable<Route.InvitationCard> { ThreadsInviteCard() }
                    composable<Route.Slider> { CustomSlider() }
                    composable<Route.Playerprogressbar> { ProgressBar(songDuration = "5.23") }
                    composable<Route.Dragbox> {
                        val systemBarStyle by remember {
                            val defaultSystemBarColor = Color.TRANSPARENT
                            mutableStateOf(
                                SystemBarStyle.auto(
                                    lightScrim = defaultSystemBarColor,
                                    darkScrim = defaultSystemBarColor
                                )
                            )
                        }
                        LaunchedEffect(systemBarStyle) {
                            enableEdgeToEdge(
                                statusBarStyle = systemBarStyle,
                                navigationBarStyle = systemBarStyle
                            )
                        }
                        DragBoxContent (
                            changeSystemBarStyle = {
                            }
                        )
                    }
                    composable<Route.Lookaheadscope> { LookAheadContent() }
                    composable<Route.Containertransform2> { ContainerTransform() }
                    composable<Route.Draggableindicator> { Carousel() }
                    composable<Route.Footballground> { FootballGround() }
                    composable<Route.GlovoScreen> { GlovoScreen() }
                    composable<Route.DocumentScanner> { DocumentScannerScreen() }
                    composable<Route.PullToRefresh> { PullToRefreshLazyColumnScreen() }
                    composable<Route.BiometricPrompt> { BiometricPromptScreen() }

                    /*Destination.entries.forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                Destination.annotationstring -> BasicTextStyle()
                                Destination.canvas -> BouncingBallGame()
                                Destination.containertransform -> ContainerTransformScreen()
                                Destination.decorationtextfield -> DecorationTextField()
                                Destination.dragfood -> DragDropMainScreen()
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
                                Destination.slider -> CustomSlider()
                                Destination.playerprogressbar -> ProgressBar(songDuration = "5.23")
                                Destination.dragbox -> {
                                    val systemBarStyle by remember {
                                        val defaultSystemBarColor = Color.TRANSPARENT
                                        mutableStateOf(
                                            SystemBarStyle.auto(
                                                lightScrim = defaultSystemBarColor,
                                                darkScrim = defaultSystemBarColor
                                            )
                                        )
                                    }
                                    LaunchedEffect(systemBarStyle) {
                                        enableEdgeToEdge(
                                            statusBarStyle = systemBarStyle,
                                            navigationBarStyle = systemBarStyle
                                        )
                                    }
                                    DragBoxContent (
                                        changeSystemBarStyle = {
                                        }
                                    )
                                }

                                Destination.lookaheadscope -> LookAheadContent()
                                Destination.containertransrom -> ContainerTransform()
                                Destination.draggableindicator -> Carousel()
                                Destination.footballground -> FootballGround()
                                Destination.GlovoScreen -> GlovoScreen()
                                Destination.documentScanner -> DocumentScannerScreen()
                                Destination.pullToRefresh -> PullToRefreshLazyColumnScreen()
                                Destination.BiometricPrompt -> BiometricPromptScreen()
                                
                            }
                        }
                    }*/
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

