package com.compose.sample.composeui.nestedscroll

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val DEFAULT_TOOLBAR_HEIGHT = 48.dp

data class NetflixLazyScreenSetting(
    val appBarHeight: Dp = DEFAULT_TOOLBAR_HEIGHT
)

@Composable
fun NetflixLazyScreen(
    modifier: Modifier = Modifier,
    settings: NetflixLazyScreenSetting = NetflixLazyScreenSetting(),
    primaryAppBar: @Composable () -> Unit,
    secondaryAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val toolbarHeightPx = with(LocalDensity.current) {
        settings.appBarHeight.roundToPx().toFloat()
    }
    val toolbarOffsetHeightPx = remember {
        mutableFloatStateOf(0f)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        content()
        Box(modifier = Modifier.padding(top = settings.appBarHeight)) {
            Box(
                modifier = Modifier
                    .alpha(0.8f)
                    .offset {
                        IntOffset(x = 0, y = toolbarOffsetHeightPx.floatValue.roundToInt())
                    }
            ) {
                secondaryAppBar()
            }
        }
        primaryAppBar()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SampleNetfixLazyScreen() {
    NetflixLazyScreen(
        primaryAppBar = { PrimaryAppBar(title = { Text(text = "Primary App Bar") }) },
        secondaryAppBar = {
            SecondaryAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                items(5) {
                    Button(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 5.dp),
                        onClick = { }
                    ) {
                        Text(text = "Button ${it + 1}")
                    }
                }
            }
        }
    ) {
        MainContent {
            items(30) {
                Button(onClick = {}) {
                    Text(
                        text = "Vertical Button ${it + 1}",
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null

) {
    TopAppBar(
        title = title,
        modifier = modifier
            .alpha(0.8f)
            .height(DEFAULT_TOOLBAR_HEIGHT),
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun SecondaryAppBar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPaddingStart: Dp = 0.dp,
    contentPaddingEnd: Dp = 0.dp,
    contentPaddingBottom: Dp = 0.dp,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier,
        state,
        contentPadding = PaddingValues(
            top = DEFAULT_TOOLBAR_HEIGHT + DEFAULT_TOOLBAR_HEIGHT,
            start = contentPaddingStart,
            end = contentPaddingEnd,
            bottom = contentPaddingBottom
        ),
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        userScrollEnabled,
        content
    )
}
