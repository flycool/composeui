package com.compose.sample.composeui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBarScreen() {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (item.badgeCount != null) {
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                } else if (item.hasNews) {
                                    Badge()
                                }
                            }) {
                                Icon(
                                    imageVector = if (selectedIndex == index) {
                                        item.selectedImage
                                    } else item.unSelectedImage,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
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

val items = listOf(
    BottomNavBarItem(
        title = "Home",
        selectedImage = Icons.Filled.Home,
        unSelectedImage = Icons.Outlined.Home,
        hasNews = false,
    ),
    BottomNavBarItem(
        title = "Email",
        selectedImage = Icons.Filled.Email,
        unSelectedImage = Icons.Outlined.Email,
        hasNews = false,
        badgeCount = 35
    ),
    BottomNavBarItem(
        title = "Settings",
        selectedImage = Icons.Filled.Settings,
        unSelectedImage = Icons.Outlined.Settings,
        hasNews = true,
    ),
)

data class BottomNavBarItem(
    val title: String,
    val selectedImage: ImageVector,
    val unSelectedImage: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Preview
@Composable
fun BottomNavBarScreenPreview() {
    BottomNavBarScreen()
}