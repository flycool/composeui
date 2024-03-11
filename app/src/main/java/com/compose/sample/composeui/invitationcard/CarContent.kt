package com.compose.sample.composeui.invitationcard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.sample.composeui.R

@Preview
@Composable
fun CardFrontSide(
    user: User = User()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CardBlackHalfCircle(modifier = Modifier.align(Alignment.TopCenter))
        CardContent(user)
        CardBlackHalfCircle(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

private val spaceBetweenItems = 28.dp
private val framePadding = 24.dp

@Composable
fun CardContent(user: User) {
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardTitleText(title = "DATE", info = user.date)
            CardBrand(modifier = Modifier.align(Alignment.Bottom))
        }
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        CardTitleText(title = "TIME", info = user.time)
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        CardTitleText(title = "USERNAME", info = user.username)
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        CardUserQrCode(userQrCode = user.userQrCode, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        CardDashDivider()
        Spacer(modifier = Modifier.height(spaceBetweenItems))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardUserImage(userImage = user.userImage)
                CardUserInstagram(text = user.instagram)
            }
            CardUserId(text = user.userId)
        }
    }
}

@Composable
fun CardBackSide() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {

        CardBlackHalfCircle(modifier = Modifier.align(alignment = Alignment.TopCenter))
        Image(
            modifier = Modifier.size(size = 160.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )
        CardBlackHalfCircle(modifier = Modifier.align(alignment = Alignment.BottomCenter))
    }
}

@Composable
fun CardUserId(text: String) {
    Text(
        modifier = Modifier.padding(end = framePadding),
        text = text,
        color = Color.Black,
        fontWeight = FontWeight.Light,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            letterSpacing = 1.sp,
        )
    )
}

@Composable
fun CardUserInstagram(text: String) {
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = text,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        style = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily.Default,
            letterSpacing = 0.7.sp
        )
    )
}

@Composable
fun CardUserImage(userImage: Int) {
    Image(
        modifier = Modifier
            .padding(start = framePadding)
            .size(42.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = userImage),
        contentDescription = null
    )

}

@Composable
fun CardDashDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.DarkGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 14f), 0f)
        )
    }
}

@Composable
fun CardUserQrCode(userQrCode: Int, modifier: Modifier) {
    Image(
        modifier = modifier
            .padding(start = framePadding)
            .size(56.dp),
        painter = painterResource(id = userQrCode),
        contentDescription = null
    )
}

@Composable
fun CardBrand(modifier: Modifier) {
    Image(
        modifier = modifier
            .padding(end = framePadding)
            .size(42.dp),
        painter = painterResource(id = R.drawable.clownfish),
        contentDescription = null
    )
}

@Composable
fun CardTitleText(title: String, info: String) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = framePadding),
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        )
        Text(
            modifier = Modifier.padding(horizontal = framePadding),
            text = info,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace
            )
        )
    }

}

@Composable
fun CardBlackHalfCircle(modifier: Modifier) {
    Canvas(
        modifier = modifier.border(
            color = Color.Magenta, width = 2.dp
        )
    ) {
        drawCircle(
            color = Color.Black,
            radius = 24.dp.toPx()
        )
    }
}