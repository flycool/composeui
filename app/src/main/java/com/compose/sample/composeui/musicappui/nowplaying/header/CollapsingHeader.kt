package com.compose.sample.composeui.musicappui.nowplaying.header

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import com.compose.sample.composeui.musicappui.nowplaying.SharedElementParams
import com.compose.sample.composeui.ui.theme.ComposeuiTheme

@Stable
@Immutable
class HeaderParams(
    val sharedElementParams: SharedElementParams,
    @DrawableRes val coverId: Int,
    val title: String,
    val author: String
)

fun Modifier.collapsingHeaderController(
    maxOffsetPx: Float,
    firstVisibleItemIndexProvider: () -> Int,
    onScroll: (currentOffsetY: Float) -> Unit,
): Modifier = composed {
    val scrollListener by rememberUpdatedState(newValue = onScroll)

    val connection = remember {
        object : NestedScrollConnection {
            var lastNotifiedValue = 0f
            var currentOffsetPx = 0f

            fun maybeNotify(value: Float) {
                if (lastNotifiedValue != value) {
                    lastNotifiedValue = value
                    scrollListener(value)
                }
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val firstVisibleIndex = firstVisibleItemIndexProvider()
                currentOffsetPx = (currentOffsetPx + delta).coerceAtMost(0f)

                val isOffsetInAllowedLimits = currentOffsetPx >= -maxOffsetPx

                fun setCurrentOffsetAndNotify() {
                    currentOffsetPx = currentOffsetPx.coerceAtLeast(-maxOffsetPx)
                    maybeNotify(currentOffsetPx)
                }

                fun calculateOffsetAndNotify(): Offset =
                    if (isOffsetInAllowedLimits) {
                        setCurrentOffsetAndNotify()
                        Offset(0f, delta)
                    } else {
                        maybeNotify(currentOffsetPx)
                        Offset.Zero
                    }

                val isScrollingUpWhenHeaderIsDecreased = delta < 0 && firstVisibleIndex == 0
                val isScrollingUpWhenHeaderIsIncreased = delta > 0 && firstVisibleIndex == 0

                return when {
                    isScrollingUpWhenHeaderIsDecreased || isScrollingUpWhenHeaderIsIncreased -> {
                        calculateOffsetAndNotify()
                    }

                    else -> Offset.Zero
                }
            }
        }
    }

    nestedScroll(connection)
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    params: HeaderParams,
    isAppearing: Boolean,
    contentAlphaProvider: State<Float>,
    backgroundColorProvider: State<Color>,
    elevationProvider: () -> Dp = { 0.dp },
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    SharedElementContainer(
        modifier = modifier
            .shadow(elevationProvider())
            .background(backgroundColorProvider.value),
        params = params.sharedElementParams,
        isForward = isAppearing,
        title = {

        },
        labels = {},
        sharedElement = {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                painter = painterResource(id = params.coverId),
                contentDescription = null
            )
        }
    )
}

const val TOP_MENU_TITLE = "Now Playing"

@Preview
@Composable
fun PreviewCollapsingHeader() {
    val params = HeaderParams(
        sharedElementParams = SharedElementParams(
            initialOffset = Offset.Zero,
            targetOffset = Offset(230f, 20f),
            initialSize = 0.dp,
            targetSize = 220.dp,
            initialCornerRadius = 0.dp,
            targetCornerRadius = 110.dp,
        ),
        coverId = R.drawable.user_one,
        title = "It Happened Quiet",
        author = "Aurora",
    )
    ComposeuiTheme(darkTheme = false) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                params = params,
                isAppearing = false,
                contentAlphaProvider = remember { mutableStateOf(1f) },
                elevationProvider = { 0.dp },
                backgroundColorProvider = remember { mutableStateOf(Color.White) }
            )
        }
    }
}
































