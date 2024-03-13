package com.compose.sample.composeui.musicappui.playerprogressbar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.sample.composeui.R
import com.compose.sample.composeui.musicappui.lerpF
import com.compose.sample.composeui.musicappui.toPxf
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random


class ProgressBarState(duration: String) {
    var isPlaying by mutableStateOf(false)
    var elapsedTime by mutableStateOf("00:00")
    var timeLeft by mutableStateOf(duration)
}

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    songDuration: String
) {
    val progressBarState = remember { ProgressBarState(songDuration) }
    var currentTime by remember { mutableLongStateOf(0L) }

    if (progressBarState.isPlaying) {
        LaunchedEffect(progressBarState.isPlaying) {
            val songTime = 200L
            while (isActive) {
                progressBarState.elapsedTime = PlayTimeFormatter.format(currentTime)
                progressBarState.timeLeft = PlayTimeFormatter.format(songTime - currentTime)

                delay(1000)

                currentTime += 1
                if (currentTime > songTime) {
                    currentTime = 0
                }
            }
        }
    }

    ProgressBar(modifier = modifier, state = progressBarState) {
        progressBarState.isPlaying = !progressBarState.isPlaying
    }
}

@Composable
fun ProgressBar(
    modifier: Modifier,
    state: ProgressBarState,
    onButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayTimeText(text = state.elapsedTime)

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVolumeLevelBar(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(48.dp),
                barWidth = 3.dp,
                gapWidth = 2.dp,
                isAnimating = state.isPlaying
            )
            PlayPauseButton(isPlaying = state.isPlaying) {
                onButtonClick()
            }
        }

        PlayTimeText(text = state.timeLeft)
    }
}

@Composable
fun PlayTimeText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 11.sp,
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .background(color = MaterialTheme.colorScheme.onSecondary)
    ) {
        val resId =
            if (isPlaying) painterResource(id = R.drawable.ic_play) else painterResource(id = R.drawable.ic_pause)
        Icon(painter = resId, contentDescription = null)
    }
}

@Composable
fun AnimatedVolumeLevelBar(
    modifier: Modifier = Modifier,
    barWidth: Dp = 2.dp,
    gapWidth: Dp = 2.dp,
    barColor: Color = MaterialTheme.colorScheme.primary,
    isAnimating: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition()
    val random = remember { Random(System.currentTimeMillis()) }
    val animations = mutableListOf<State<Float>>()

    repeat(15) {
        val durationMillis = random.nextInt(500, 2000)
        animations += infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis),
                repeatMode = RepeatMode.Reverse
            ), label = "animation"
        )
    }

    val barWidthFloat by rememberUpdatedState(newValue = barWidth.toPxf())
    val gapWidthFloat by rememberUpdatedState(newValue = gapWidth.toPxf())

    val initialMultipliers = remember {
        mutableListOf<Float>().apply {
            repeat(MaxLinesCount) {
                this += random.nextFloat()
            }
        }
    }

    val heightDivider by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 6f,
        animationSpec = tween(1000, easing = LinearEasing)
    )

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val canvasCenterY = canvasHeight / 2

        val count =
            (canvasWidth / (barWidthFloat + gapWidthFloat)).toInt().coerceAtMost(MaxLinesCount)
        val animateVolumeWidth = count * (barWidthFloat + gapWidthFloat)
        var startOffset = (canvasWidth - animateVolumeWidth) / 2

        val barMinHeight = 0f
        val barMaxHeight = canvasHeight / 2 / heightDivider

        repeat(count) { index ->
            val currentSize = animations[index % animations.size].value
            var barHeightPercent = initialMultipliers[index] + currentSize
            if (barHeightPercent > 1.0f) {
                val diff = barHeightPercent - 1.0f
                barHeightPercent = 1.0f - diff
            }
            val barHeight = lerpF(barMinHeight, barMaxHeight, barHeightPercent)

            drawLine(
                color = barColor,
                start = Offset(startOffset, canvasCenterY - barHeight / 2),
                end = Offset(startOffset, canvasCenterY + barHeight / 2),
                strokeWidth = barWidthFloat,
                cap = StrokeCap.Round,
            )
            startOffset += barWidthFloat + gapWidthFloat
        }
    }
}

const val MaxLinesCount = 100


@Preview
@Composable
private fun ProgressBarPreview() {
    ProgressBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray),
        songDuration = "4.32"
    )
}






