package com.compose.sample.composeui.emoji

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.util.UUID
import kotlin.random.Random

@Preview
@Composable
fun FireEmoji() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val configuration = LocalConfiguration.current
        val width = configuration.screenWidthDp
        val height = configuration.screenHeightDp
        val targetX = Random.nextInt(0, width)
        val targetY = Random.nextInt(0, (height * 0.2).toInt())
        val rotation = Random.nextInt(-90, 90).toFloat()

        val emojis = remember {
            mutableStateListOf<MyEmoji>()
        }

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                emojis.add(
                    MyEmoji(
                        id = UUID.randomUUID().toString(),
                        rotation = rotation,
                        offsetX = targetX,
                        offsetY = targetY

                    )
                )

            }) {
            Text(text = "FIRE!")
        }

        emojis.forEach { emoji ->
            key(emoji.id) {
                SingleEmojiContainer(
                    emoji = emoji,
                    onAnimationFinished = { emojis.remove(emoji) }
                )
            }
        }

    }
}

@Composable
fun SingleEmojiContainer(
    emoji: MyEmoji,
    onAnimationFinished: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    println("width: ${configuration.screenWidthDp}")

    val startPoint = IntOffset(
        x = configuration.screenWidthDp / 2,
        y = (configuration.screenHeightDp * 0.9f).toInt()
    )
    val rotation = Random.nextInt(-90, 90).toFloat()

    val opacityAnimatable = remember { Animatable(0f) }
    val offsetXAnimatable = remember { Animatable(startPoint.x, Int.VectorConverter) }
    val offsetYAnimatable = remember { Animatable(startPoint.y, Int.VectorConverter) }
    val rotationAnimatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val opacity =
            async { opacityAnimatable.animateTo(1f, animationSpec = tween((500))) }
        val offsetX =
            async { offsetXAnimatable.animateTo(emoji.offsetX, animationSpec = tween((1000))) }
        val offsetY =
            async { offsetYAnimatable.animateTo(emoji.offsetY, animationSpec = tween((1000))) }
        val rotation1 =
            async { rotationAnimatable.animateTo(1f, animationSpec = tween(1000)) }
        awaitAll(offsetX, offsetY, rotation1, opacity)
        opacityAnimatable.animateTo(0f, animationSpec = tween((2000)))
        onAnimationFinished()
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .offset {
            IntOffset(
                x = offsetXAnimatable.value.dp.roundToPx(),
                y = offsetYAnimatable.value.dp.roundToPx()
            )
        }
        .graphicsLayer {
            rotationZ = rotationAnimatable.value.times(rotation)
            alpha = opacityAnimatable.value
        }
    ) {
        Text(text = emoji.unicode, fontSize = 40.sp)
    }
}

data class MyEmoji(
    val id: String,
    val rotation: Float,
    val offsetX: Int,
    val offsetY: Int,
    val unicode: String = emojiCodes.random()
)


val emojiCodes = listOf(
    "\uD83D\uDE00",
    "\uD83D\uDE01",
    "\uD83D\uDE02",
    "\uD83D\uDE03",
    "\uD83D\uDE04",
    "\uD83D\uDE05",
    "\uD83D\uDE06",
    "\uD83D\uDE07",
    "\uD83D\uDE08",
    "\uD83D\uDE09",
    "\uD83D\uDE10",
    "\uD83D\uDE11",
    "\uD83D\uDE12",
    "\uD83D\uDE13",
    "\uD83D\uDE14",
)