package com.compose.sample.composeui.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

val items = listOf(
    NavigationItem(
        title = "Home",
        selectedImage = Icons.Filled.Home,
        unSelectedImage = Icons.Outlined.Home,
        hasNews = false,
    ),
    NavigationItem(
        title = "Email",
        selectedImage = Icons.Filled.Email,
        unSelectedImage = Icons.Outlined.Email,
        hasNews = false,
        badgeCount = 35
    ),
    NavigationItem(
        title = "Settings",
        selectedImage = Icons.Filled.Settings,
        unSelectedImage = Icons.Outlined.Settings,
        hasNews = true,
    ),
)

data class NavigationItem(
    val title: String,
    val selectedImage: ImageVector,
    val unSelectedImage: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)