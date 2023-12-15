package com.compose.sample.composeui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Preview
@Composable
fun Particle() {
    val data = listOf(
        Particle(Offset(4f, 30f), Offset(6f, 3f)),
        Particle(Offset(15f, 5f), Offset(66f, 7f)),
        Particle(Offset(1f, 43f), Offset(69f, 3f))
    )
    ParticleSystem(particles = data)
}

data class Particle(
    val position: Offset,
    val velocity: Offset
)

@Composable
fun ParticleSystem(particles: List<Particle>) {
    val mutableParticles = remember {
        mutableStateListOf<Particle>()
    }
    mutableParticles.addAll(particles)

    var counter = 0
    LaunchedEffect(Unit) {
        while (true) {
            val particlesCopy = ArrayList(mutableParticles.map { it.copy() })
            particlesCopy.forEachIndexed { index, particle ->
                mutableParticles[index] =
                    particle.copy(position = particle.position + particle.velocity)
            }

            delay(16L)

            counter++
            if (counter > 3000) {
                break
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Yellow, Color.Red
                    )
                )
            )
    ) {
        mutableParticles.forEach { particle ->
            drawCircle(
                color = Color.Blue,
                center = particle.position,
                alpha = 0.6f,
                radius = 10f
            )
        }

    }

}