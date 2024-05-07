package com.compose.sample.composeui.edgetoedge

import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import com.compose.sample.composeui.ui.theme.ComposeuiTheme

@Composable
fun DrogBoxContentScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current as AppCompatActivity
    val systemBarStyle by remember {
        val defaultSystemBarColor = android.graphics.Color.TRANSPARENT
        mutableStateOf(
            SystemBarStyle.auto(
                lightScrim = defaultSystemBarColor,
                darkScrim = defaultSystemBarColor
            )
        )
    }
    LaunchedEffect(systemBarStyle) {
        context.enableEdgeToEdge(
            statusBarStyle = systemBarStyle,
            navigationBarStyle = systemBarStyle
        )
    }
    DragBoxContent (
        changeSystemBarStyle = {
        }
    )
}

@Composable
fun DragBoxContent(
    modifier: Modifier = Modifier,
    changeSystemBarStyle: (SystemBarStyle) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { paddingValues ->

        LaunchedEffect(Unit) {
            changeSystemBarStyle(SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
        }

        val layoutDirection = LocalLayoutDirection.current
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(layoutDirection),
                    end = paddingValues.calculateEndPadding(layoutDirection),
                    bottom = paddingValues.calculateBottomPadding(),
                )
        ) {
            Column(modifier.fillMaxSize()) {
                var alpha by remember { mutableFloatStateOf(0f) }
                val color by remember(alpha) {
                    mutableStateOf(Color.Red.copy(alpha = alpha))
                }

                val animatedColor by animateColorAsState(targetValue = color, label = "color")

                Spacer(
                    modifier = Modifier
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .fillMaxWidth()
                        .background(animatedColor)
                )

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val miniBoxHeight = 200.dp
                    val maxBoxHeight = maxHeight
                    var boxHeight by remember {
                        mutableStateOf(miniBoxHeight)
                    }

                    LaunchedEffect(boxHeight) {
                        alpha = (boxHeight / maxBoxHeight).coerceIn(0f, 1f)
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(boxHeight)
                        .background(Color.White)
                        .align(Alignment.BottomCenter)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { change, dragAmount ->
                                if (dragAmount < 0) { //dragging up
                                    if (boxHeight >= maxBoxHeight) {
                                        boxHeight = maxBoxHeight
                                    } else {
                                        boxHeight += 6.dp
                                    }
                                } else if (dragAmount > 0) {
                                    if (boxHeight <= miniBoxHeight) {
                                        boxHeight = miniBoxHeight
                                    } else {
                                        boxHeight -= 6.dp
                                    }
                                }
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clownfish),
                            contentDescription = null,
                            modifier = Modifier
                                .size(boxHeight)
                                .align(Alignment.Center)
                        )
                    }

                }

            }
        }
    }
}

@Preview
@Composable
private fun DragBoxContentPreview() {
    ComposeuiTheme {
        DragBoxContent {

        }
    }
}