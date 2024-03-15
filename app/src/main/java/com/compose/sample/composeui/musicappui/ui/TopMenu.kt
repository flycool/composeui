package com.compose.sample.composeui.musicappui.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.compose.sample.composeui.R
import com.compose.sample.composeui.ui.theme.ComposeuiTheme

@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    startIcon: @Composable RowScope.() -> Unit,
    title: @Composable (RowScope.() -> Unit)? = null,
    endIcon: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon()

        if (title == null) {
            Spacer(modifier = Modifier.weight(1f))
        } else {
            title()
        }

        if (endIcon != null) {
            endIcon()
        }
    }
}

@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    iconsTint: Color = MaterialTheme.colorScheme.onSurface,
    endIconRes: ImageVector = Icons.Default.Share,
    onStartIconClick: () -> Unit = {},
    onEndIconClick: () -> Unit = {},
) {
    TopMenu(
        modifier = modifier,
        startIcon = {
            IconButton(onClick = onStartIconClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    colorFilter = ColorFilter.tint(iconsTint)
                )
            }
        },
        title = if (title.isNotEmpty()) {
            {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                )
            }
        } else null,
        endIcon = {
            IconButton(onClick = onEndIconClick) {
                Image(
                    imageVector = endIconRes,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(iconsTint),
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewTopMenu() {
    ComposeuiTheme(darkTheme = false) {
        TopMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            iconsTint = MaterialTheme.colorScheme.onSurface,
            title = "Now Playing",
        )
    }
}