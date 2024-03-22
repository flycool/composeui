package com.compose.sample.composeui.lookaheadscope

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ContainerTransform() {
    var isExpanded by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isExpanded = !isExpanded },
        color = Color.White
    ) {
        LookaheadScope {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(if (isExpanded) 1f else 4f),
                    contentAlignment = if (isExpanded) Alignment.TopStart else Alignment.Center
                ) {
                    HeaderLayout(
                        LookaheadScopeWrapper(this@LookaheadScope),
                        isExpanded = isExpanded
                    )

                }

                Box(
                    modifier = Modifier
                        .weight(if (isExpanded) 4f else 1f)
                        .padding(horizontal = 8.dp)
                ) {
                    AttributeLayout(
                        LookaheadScopeWrapper(this@LookaheadScope),
                        isExpanded = isExpanded
                    )
                }
            }
        }
    }
}

@Stable
data class LookaheadScopeWrapper(
    val lookaheadScope: LookaheadScope
)

@Stable
@Composable
fun HeaderLayout(
    lookaheadScopeWrapper: LookaheadScopeWrapper,
    isExpanded: Boolean
) {
    val headerImage = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(modifier = Modifier
                .animateBounds()
                .then(modifier)
                .drawBehind { drawCircle(Color(0xFF949494)) }
            )
        }
    }
    val headerTitle = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(modifier = Modifier
                .animateBounds()
                .then(modifier)
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(100)
                }
                .background(Color(0xFFACACAC))
            )
        }
    }
    val headerSubTitle = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(modifier = Modifier
                .animateBounds()
                .then(modifier)
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(100)
                }
                .background(Color(0xFFC2C2C2))
            )
        }
    }

    val headerContainer = remember {
        movableContentWithReceiverOf<LookaheadScope, @Composable () -> Unit> { content ->
            Box(
                modifier = Modifier
                    .animateBounds()
                    .padding(16.dp)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(10)
                    }
                    .background(Color(0xFFE7E7E7))
            ) {
                content()
            }
        }
    }

    if (isExpanded) {
        lookaheadScopeWrapper.lookaheadScope.headerContainer {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                lookaheadScopeWrapper.lookaheadScope.headerImage(Modifier.size(48.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    lookaheadScopeWrapper.lookaheadScope.headerTitle(
                        Modifier
                            .height(20.dp)
                            .width(280.dp)
                    )
                    lookaheadScopeWrapper.lookaheadScope.headerSubTitle(
                        Modifier
                            .height(20.dp)
                            .width(172.dp)
                    )
                }
            }
        }
    } else {
        lookaheadScopeWrapper.lookaheadScope.headerContainer {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                lookaheadScopeWrapper.lookaheadScope.headerImage(Modifier.size(120.dp))
                Spacer(Modifier.height(32.dp))
                lookaheadScopeWrapper.lookaheadScope.headerTitle(
                    Modifier
                        .height(24.dp)
                        .width(280.dp)
                )
                Spacer(Modifier.height(16.dp))
                lookaheadScopeWrapper.lookaheadScope.headerSubTitle(
                    Modifier
                        .height(24.dp)
                        .width(172.dp)
                )
            }
        }
    }
}

@Composable
fun AttributeLayout(
    lookaheadScopeWrapper: LookaheadScopeWrapper,
    isExpanded: Boolean
) {
    val attributeColors = listOf(
        Color(0xFFFF928D),
        Color(0xFFFFDB8D),
        Color(0xFFA7E5FF),
        Color(0xFFB6E67F),
    )
    val attributes = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier, @Composable () -> Unit> { modifier, content ->
            attributeColors.forEach { color ->
                Box(
                    Modifier
                        .animateBounds()
                        .graphicsLayer {
                            clip = true
                            shape = RoundedCornerShape(10)
                        }
                        .background(color)
                        .then(modifier)
                ) {
                    content()
                }
            }
        }
    }
    val attributeImage = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(
                modifier = Modifier
                    .animateBounds()
                    .then(modifier)
                    .drawBehind {
                        drawCircle(Color(0x99FFFFFF))
                    }
            )
        }
    }
    val attributeTitle = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(
                modifier = Modifier
                    .animateBounds()
                    .then(modifier)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(100)
                    }
                    .background(Color(0x99FFFFFF))
            )
        }
    }
    val attributeSubtitle = remember {
        movableContentWithReceiverOf<LookaheadScope, Modifier> { modifier ->
            Box(
                modifier = Modifier
                    .animateBounds()
                    .then(modifier)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(100)
                    }
                    .background(Color(0x99FFFFFF))
            )
        }
    }


    if (isExpanded) {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lookaheadScopeWrapper.lookaheadScope.attributes(
                Modifier
                    .height(140.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    lookaheadScopeWrapper.lookaheadScope.attributeImage(Modifier.size(64.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        lookaheadScopeWrapper.lookaheadScope.attributeTitle(
                            Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        lookaheadScopeWrapper.lookaheadScope.attributeSubtitle(
                            Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lookaheadScopeWrapper.lookaheadScope.attributes(
                Modifier.size(width = 64.dp, height = 80.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    lookaheadScopeWrapper.lookaheadScope.attributeImage(Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    lookaheadScopeWrapper.lookaheadScope.attributeTitle(
                        Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

}


@Preview
@Composable
private fun ContainerTransformPreview() {
    ContainerTransform()
}