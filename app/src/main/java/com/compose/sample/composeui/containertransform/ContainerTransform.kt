package com.compose.sample.composeui.containertransform

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        HotContent()
        FabContainer(modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun FabContainer(
    modifier: Modifier = Modifier
) {
    var containerState by remember {
        mutableStateOf(ContainerState.FAB)
    }
    val transition = updateTransition(
        targetState = containerState,
        label = "container transform"
    )
    val animatedColor by transition.animateColor(label = "color") { state ->
        when (state) {
            ContainerState.FAB -> MaterialTheme.colorScheme.primaryContainer
            ContainerState.FULLSCREEN -> MaterialTheme.colorScheme.surface
        }
    }
    val cornerRadius by transition.animateDp(
        label = "corner radius",
        transitionSpec = {
            when (targetState) {
                ContainerState.FAB -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic
                )

                ContainerState.FULLSCREEN -> tween(
                    durationMillis = 200,
                    easing = EaseInCubic
                )
            }
        }
    ) { state ->
        when (state) {
            ContainerState.FAB -> 22.dp
            ContainerState.FULLSCREEN -> 0.dp
        }
    }
    val elevation by transition.animateDp(
        label = "elevation",
        transitionSpec = {
            when (targetState) {
                ContainerState.FAB -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic
                )

                ContainerState.FULLSCREEN -> tween(
                    durationMillis = 200,
                    easing = EaseInCubic
                )
            }
        }
    ) { state ->
        when (state) {
            ContainerState.FAB -> 6.dp
            ContainerState.FULLSCREEN -> 0.dp
        }
    }
    val padding by transition.animateDp(label = "padding") { state ->
        when (state) {
            ContainerState.FAB -> 16.dp
            ContainerState.FULLSCREEN -> 0.dp
        }
    }

    transition.AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(end = padding, bottom = padding)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind { drawRect(animatedColor) },
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                animationSpec = tween(220, delayMillis = 90),
                                initialScale = 0.92f
                            )
                    )
                .togetherWith(fadeOut(animationSpec = tween(90)))
                .using(sizeTransform = SizeTransform(
                    clip = false,
                    sizeAnimationSpec = { _, _ ->
                        tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    }
                ))
        }
    ) { state ->
        when (state) {
            ContainerState.FAB -> {
                Fab(
                    modifier = Modifier,
                    onClick = { containerState = ContainerState.FULLSCREEN }
                )
            }

            ContainerState.FULLSCREEN -> {
                AddContentScreen(
                    modifier = Modifier,
                    onBack = { containerState = ContainerState.FAB }
                )
            }
        }
    }
}


@Composable
private fun HotContent() {
    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        HotTakes()
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        },
        placeholder = { Text(text = "Search your hot takes") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(50)
    )

}

@Composable
private fun HotTakes() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(hotTakes) {
            HotTake(hotTake = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotTake(hotTake: HotTake) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        onClick = {}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            hotTake.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = hotTake.take,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun Fab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(
                minWidth = 76.dp,
                minHeight = 76.dp
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Filled.Add),
            contentDescription = null
        )
    }
}

enum class ContainerState {
    FAB, FULLSCREEN
}

@Preview
@Composable
fun ContainerTransformPreview() {
    HomeScreen()
}