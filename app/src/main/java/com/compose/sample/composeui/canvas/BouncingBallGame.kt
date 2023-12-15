package com.compose.sample.composeui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Preview
@Composable
fun BouncingBallGame() {
    val position = remember { mutableStateOf(Offset(300f, 0f)) }
    val velocity = remember { mutableStateOf(Offset(3f, 3f)) }

    LaunchedEffect(Unit) {
        while (true) {
            position.value += velocity.value
            if (position.value.x < 0f || position.value.x > 1000f) {
                velocity.value = Offset(-velocity.value.x, velocity.value.y)
            }
            if (position.value.y < 0f || position.value.y > 1200f) {
                velocity.value = Offset(velocity.value.x, -velocity.value.y)
            }
            delay(16L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Red, Color.Blue
                    )
                )
            )
    ) {
        Canvas(
            modifier = Modifier
                .height(460.dp)
                .fillMaxWidth()
        ) {
            drawCircle(
                brush = Brush.horizontalGradient(listOf(Color.Blue, Color.Red)),
                center = position.value,
                radius = 60f
            )
        }
        Divider(thickness = 5.dp, color = Color.Red)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    val random = (-1..1).random()
                    if (random != 0) {
                        velocity.value += Offset(random * 10f, random * 10f)
                    }
                }) {
                Text(text = "Change Bounce")
            }
            Text(
                text = "Velocity: ${velocity.value.x}, ${velocity.value.y}",
                color = Color.White
            )
        }
    }
}