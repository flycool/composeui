package com.compose.sample.composeui.invitationcard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R
import kotlin.math.abs

data class User(
    val username: String = "canerkaseler",
    val instagram: String = "canerkaseler",
    val userId: String = "07325201",
    val date: String = "2024-3-11",
    val time: String = "03:30 pm",
    val userImage: Int = R.drawable.user_one,
    val userQrCode: Int = R.drawable.logo
)

@Composable
fun ThreadsInviteCardHolder(
    modifier: Modifier = Modifier,
    positionAxisY: Float,
    frontSide: @Composable () -> Unit = {},
    backSide: @Composable () -> Unit = {},
) {
    Card(
        modifier = modifier.graphicsLayer {
            rotationY = positionAxisY
            cameraDistance = 14f * density
        }
    ) {
        if (abs(positionAxisY.toInt()) % 360 <= 90) {
            Box(modifier = Modifier.fillMaxSize()) {
                frontSide()
            }
        } else if (abs(positionAxisY.toInt()) % 360 in 91..270) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f // Important to avoid mirror effect.
                    },
            ) {
                backSide()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                frontSide()
            }
        }
    }
}

@Preview
@Composable
fun ThreadsInviteCard() {
    var axisY by remember { mutableFloatStateOf(0f) }
    var isAutomaticAnimationActive by remember { mutableStateOf(true) }
    var isCompletingAnimationActive by remember { mutableStateOf(false) }

    var animationDragAmount by remember { mutableFloatStateOf(0f) }
    var isQuickDragAnimationActive by remember { mutableStateOf(false) }

    val pY = if (isAutomaticAnimationActive) {
        val automaticTurningAnimation = remember {
            Animatable(axisY)
        }
        LaunchedEffect(isAutomaticAnimationActive) {
            if (isAutomaticAnimationActive) {
                automaticTurningAnimation.animateTo(
                    targetValue = if (axisY >= 0) {
                        axisY + 360f
                    } else {
                        axisY - 360f
                    },
                    animationSpec = infiniteRepeatable(
                        tween(5000, easing = FastOutLinearInEasing)
                    )
                )
            }
        }
        axisY = automaticTurningAnimation.value
        automaticTurningAnimation.value
    } else if (isCompletingAnimationActive) {

        val completeTurningAnimation = remember { Animatable(axisY) }

        LaunchedEffect(isCompletingAnimationActive) {
            if (isCompletingAnimationActive) {
                completeTurningAnimation.animateTo(
                    targetValue = if (abs(axisY.toInt()) % 360 <= 90) {
                        0f
                    } else if (abs(axisY.toInt()) % 360 in 91..270) {
                        if (abs(axisY.toInt()) % 360 <= 270f) {
                            if (axisY > 0) 180f else -180f
                        } else {
                            if (axisY > 0) 360f else -360f
                        }
                    } else {
                        if (axisY > 0) 360f else -360f
                    },
                    animationSpec = tween(500, easing = FastOutLinearInEasing)
                ).endState
            }
        }
        axisY = completeTurningAnimation.value
        completeTurningAnimation.value

    } else if (isQuickDragAnimationActive) {

        val completeQuickDragAnimation = remember { Animatable(axisY) }

        LaunchedEffect(isQuickDragAnimationActive) {
            if (isQuickDragAnimationActive) {

                val completeTurningAnimationState = completeQuickDragAnimation.animateTo(
                    targetValue = if (animationDragAmount > 0) {
                        360f * 2
                    } else {
                        -360f * 2
                    },
                    animationSpec = tween(1250, easing = LinearEasing)
                ).endState

                if (!completeTurningAnimationState.isRunning) {
                    isQuickDragAnimationActive = false
                    isAutomaticAnimationActive = true
                }
            }
        }
        axisY = completeQuickDragAnimation.value // Update axisY value.
        completeQuickDragAnimation.value // Return needed value.

    } else {
        axisY
    }


    ThreadsInviteCardHolder(
        frontSide = { CardFrontSide() },
        backSide = { CardBackSide() },
        positionAxisY = pY,
        modifier = Modifier
            .background(Color.Black)
            .padding(horizontal = 48.dp, vertical = 150.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        isAutomaticAnimationActive = false
                        isCompletingAnimationActive = false
                    },
                    onDragEnd = {
                        isAutomaticAnimationActive = false
                        isCompletingAnimationActive = false
                        isQuickDragAnimationActive = false

                        // If user did not drag enough, just show completing animation.
                        if (abs(animationDragAmount) > 12f) {
                            isQuickDragAnimationActive = true
                        } else {
                            isCompletingAnimationActive = true
                        }
                    },
                    onDragCancel = {},
                    onHorizontalDrag = { change, dragAmount ->
                        axisY = if (dragAmount < 0) {
                            (axisY - abs(dragAmount)) % 360
                        } else {
                            (axisY + abs(dragAmount)) % 360
                        }
                        animationDragAmount = dragAmount
                    }
                )
            }
    )
}




































