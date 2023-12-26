package com.compose.sample.funui.multiplebackstacks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun MultilpleBackStacks() {
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route
                    val icon = if (isSelected) item.selectedIcon else item.unSelectedIcon
                    NavigationBarItem(
                        selected = isSelected,
                        label = { Text(text = item.title) },
                        icon = {
                            Icon(imageVector = icon, contentDescription = item.title)

                        },
                        onClick = {
                            rootNavController.navigate(item.title.lowercase()) {
                                popUpTo(rootNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = rootNavController, startDestination = "home") {
            composable("home") {
                HomeNavHost()
            }
            composable("chat") {
                ChatNavHost()
            }
            composable("settings") {
                SettingsNavHost()
            }
        }
    }
}


@Composable
fun GenericScreen(
    name: String,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = name)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNextClick) {
            Text(text = "Next")
        }
    }
}

@Composable
fun HomeNavHost() {
    val homeNavController = rememberNavController()
    NavHost(navController = homeNavController, startDestination = "Home1") {
        (1..5).forEach { i ->
            composable("Home$i") {
                GenericScreen(name = "Home$i") {
                    if (i < 5) {
                        homeNavController.navigate("Home${i + 1}")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatNavHost() {
    val chatNavController = rememberNavController()
    NavHost(navController = chatNavController, startDestination = "Chat1") {
        (1..5).forEach { i ->
            composable("Chat$i") {
                GenericScreen(name = "Chat$i") {
                    if (i < 5) {
                        chatNavController.navigate("Chat${i + 1}")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsNavHost() {
    val settingsNavController = rememberNavController()
    NavHost(navController = settingsNavController, startDestination = "Settings1") {
        (1..5).forEach { i ->
            composable("Settings$i") {
                GenericScreen(name = "Settings$i") {
                    if (i < 5) {
                        settingsNavController.navigate("Settings${i + 1}")
                    }
                }
            }
        }
    }
}


data class BottomItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
)

val items = listOf(
    BottomItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home
    ),
    BottomItem(
        title = "Chat",
        selectedIcon = Icons.Filled.Email,
        unSelectedIcon = Icons.Outlined.Email
    ),
    BottomItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings
    )
)