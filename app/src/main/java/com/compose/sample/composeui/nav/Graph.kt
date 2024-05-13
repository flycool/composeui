package com.compose.sample.composeui.nav

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.compose.sample.composeui.LandingScreen
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
import com.compose.sample.composeui.visualTransformation.TextFieldVisualTransformation
import com.compose.sample.composeui2.biometriprompt.BiometricPromptScreen
import com.compose.sample.composeui2.pulltorefresh.PullToRefreshLazyColumnScreen
import com.compose.sample.composeui2.scanner.DocumentScannerScreen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

fun NavGraphBuilder.mainGraph(navController: NavController) {
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
    composable<Route.Dragbox> { DrogBoxContentScreen() }
    composable<Route.Lookaheadscope> { LookAheadContent() }
    composable<Route.Containertransform2> { ContainerTransform() }
    composable<Route.Draggableindicator> { Carousel() }
    composable<Route.Footballground> { FootballGround() }
    composable<Route.GlovoScreen> { GlovoScreen() }
    composable<Route.DocumentScanner> { DocumentScannerScreen() }
    composable<Route.PullToRefresh> { PullToRefreshLazyColumnScreen() }
    composable<Route.BiometricPrompt> { BiometricPromptScreen() }
}

class CustomNavType<T : Parcelable>(
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>
) : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz) as T
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key) // for backwards compatibility
        }

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)

    override fun parseValue(value: String): T = Json.decodeFromString(serializer, value)

    override fun serializeAsValue(value: T): String = Json.encodeToString(serializer, value)

    override val name: String = clazz.name
}

//use
/**
 * composable<Routes.SecondScreen>(
 *     typeMap = mapOf(typeOf<ScreenInfo>() to CustomNavType(ScreenInfo::class.java, ScreenInfo.serializer()))
 * ) { backStackEntry ->
 *     val parameters = backStackEntry.toRoute<Routes.SecondScreen>()
 *     // use the parameters
 * }
 */