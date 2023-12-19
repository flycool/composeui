package com.compose.sample.funui.touchevent

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatAnimationSpec
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@Composable
fun Modifier.onTouchHeld(
    pollyDelay: Duration,
    onTouchHeld: (timeElapsed: Duration) -> Unit
) = composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialDownTime = System.nanoTime()
            val initialTouchHeldJob = scope.launch {
                while (initialDown.pressed) {
                    val timeElapsed = System.nanoTime() - initialDownTime
                    onTouchHeld(timeElapsed.nanoseconds)
                    delay(pollyDelay)
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}

@Composable
fun Modifier.onTouchHeldAnimated(
    easing: Easing = FastOutSlowInEasing,
    pollDelay: Duration = 500.milliseconds,
    targetPollDelay: Duration = pollDelay,
    animationDuration: Duration = 5.seconds,
    onTouchHeld: () -> Unit
) = composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        val animationSpec = FloatTweenSpec(
            duration = animationDuration.inWholeMilliseconds.toInt(),
            delay = 0,
            easing = easing
        )
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialTouchHeldJob = scope.launch {
                var currentPlayTime = 0.milliseconds
                var delay = pollDelay
                while (initialDown.pressed) {
                    onTouchHeld()
                    delay(delay.inWholeMilliseconds)
                    currentPlayTime += delay
                    //获取动画的值，赋给delay
                    delay = animationSpec.getValueFromNanos(
                        playTimeNanos = currentPlayTime.inWholeNanoseconds,
                        initialValue = pollDelay.inWholeMilliseconds.toFloat(),
                        targetValue = 0f,
                        initialVelocity = 0f
                    ).toInt().milliseconds

                    println("delay==$delay")
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}

@Preview
@Composable
fun TouchHeldButton() {
    var count by remember { mutableIntStateOf(0) }
    var text by remember { mutableStateOf("") }
    Row {
        Button(
            onClick = { },
            modifier = Modifier.onTouchHeld(500.milliseconds) { timeElapsed ->
                count--
            }

        ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "add count")
        }
        Text(text = "Counter: $count")
        Button(
            onClick = { },
            modifier = Modifier.onTouchHeldAnimated {
                count++
            }

        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add count")
        }
        Text(text = text)
    }
}