package com.compose.sample.composeui.custommodifier

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.R

fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        return this
    }
}

fun <T> Modifier.nullConditional(
    argument: T?,
    ifNotNull: Modifier.(T) -> Modifier,
    ifNull: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (argument != null) {
        then(ifNotNull(Modifier, argument))
    } else if (ifNull != null) {
        then(ifNull(Modifier))
    } else {
        return this
    }
}

@Composable
fun ComplexModifierScene(
    nighttime: Boolean,
    torchon: Boolean,
    backgroundColor: Color?,
    modifier: Modifier = Modifier
) {
    val boxModifier = modifier
        .conditional(nighttime && torchon, ifTrue = {
            clip(CircleShape)
        })
//        .fillMaxSize()
        .aspectRatio(1f)
        .nullConditional(backgroundColor, ifNotNull = {
            background(it)
        })
        .conditional(nighttime, ifTrue = {
            conditional(
                condition = torchon,
                ifTrue = {
                    shadow(elevation = 4.dp, shape = CircleShape)
                },
                ifFalse = {
                    blur(20.dp)
                }
            )
        })
    Box(modifier = boxModifier) {
        Image(
            painter = painterResource(id = R.drawable.user_one),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

class GreyScaleModifier : DrawModifier {
    override fun ContentDrawScope.draw() {
        val saturationMatrix = ColorMatrix().apply { setToSaturation(0f) }
        val saturationFilter = ColorFilter.colorMatrix(saturationMatrix)
        val paint = Paint().apply {
            colorFilter = saturationFilter
        }
        drawIntoCanvas {
            it.saveLayer(
                bounds = Rect(0f, 0f, size.width, size.height),
                paint = paint
            )
            drawContent()
            it.restore()
        }
    }
}

fun Modifier.greyScale() = then(GreyScaleModifier())

fun Modifier.disabled() = this
    .then(greyScale())
    .then(alpha(0.4f))

@Composable
fun GreyscaleScene(
    isDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = modifier.conditional(isDisabled, {
            disabled()
        })) {
            Image(
                painter = painterResource(id = R.drawable.user_one),
                contentDescription = null,
            )
        }
        ComplexModifierScene(
            nighttime = true,
            torchon = true,
            backgroundColor = null
        )
    }
}

@Preview
@Composable
private fun GreyscaleScenePreview() {
    GreyscaleScene(isDisabled = true)
}


fun main() {
    println("heello")
}




























