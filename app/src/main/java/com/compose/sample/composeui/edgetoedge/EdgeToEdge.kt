package com.compose.sample.composeui.edgetoedge

import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.sample.composeui.R

@Composable
fun NavEdgeToEdgeScreen(
    window: Window
) {
    val navController = rememberNavController()
    var showDialog by remember {
        mutableIntStateOf(0)
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            EdgeToEdgeScreen(
                facts = facts,
                onDetailClick = {
                    //showDialog = facts.indexOf(it)
                    navController.navigate("detail/${facts.indexOf(it)}")
                },
            )
        }
        dialog(
            route = "detail/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType }),
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = false
            )
        ) { backStackEntry ->


            SetDialogDestinationToEdgeToEdge()

            val index = backStackEntry.arguments?.getInt("index")
            index?.let {
                FactDetail(
                    fact = facts[it],
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun EdgeToEdgeScreen(
    facts: List<ReefFact>,
    onDetailClick: (ReefFact) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Box(modifier = modifier) {
                Image(
                    painter = painterResource(id = R.drawable.clownfish),
                    contentDescription = "clown fish",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }
            items(facts) { fact ->
                FactItem(fact = fact, onClick = onDetailClick)
            }
            item { Spacer(modifier = Modifier.height(0.dp)) }
        }

    }
}

@Preview
@Composable
fun ScreenPreview() {
    EdgeToEdgeScreen(facts = facts, onDetailClick = {})
}

@Composable
fun FactItem(
    fact: ReefFact,
    onClick: (ReefFact) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { onClick(fact) },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = fact.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FactDetail(
    modifier: Modifier = Modifier,
    fact: ReefFact
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(fact.imageRes),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = fact.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = fact.fact,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
























