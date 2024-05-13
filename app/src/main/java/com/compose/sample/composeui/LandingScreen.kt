package com.compose.sample.composeui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.nav.Route
import com.compose.sample.composeui.nav.routes
import com.compose.sample.composeui.ui.theme.ComposeuiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(naviate: (Route) -> Unit) {
    ComposeuiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Text(text = "UI Examples")
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    itemsIndexed(routes) { index, route ->
                        Text(
                            text = "$index $route",
                            modifier = Modifier
                                .clickable {
                                    naviate(route)
                                }
                                .defaultMinSize(minHeight = 48.dp)
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}