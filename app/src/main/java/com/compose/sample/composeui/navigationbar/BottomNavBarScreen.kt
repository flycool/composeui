package com.compose.sample.composeui.navigationbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavBarScreen() {
    Scaffold(
        bottomBar = {
            NavigationBottomBar(items = items)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBottomBar(
    items: List<NavigationItem>,
) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    NavigationIcon(
                        item = item,
                        selected = selectedIndex == index
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarScreenPreview() {
    BottomNavBarScreen()
}