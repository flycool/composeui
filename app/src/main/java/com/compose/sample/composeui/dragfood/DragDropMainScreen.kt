package com.compose.sample.composeui.dragfood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.compose.sample.composeui.dragfood.function.LongPressDraggable
import com.compose.sample.composeui.dragfood.model.FoodItem
import com.compose.sample.composeui.dragfood.model.foodItems
import com.compose.sample.composeui.dragfood.model.persons


@Stable
data class FoodItemListWrapper(
    val list: List<FoodItem>
)

@Composable
fun DragDropMainScreen() {
    DragDropMainScreen(listWrapper = FoodItemListWrapper(foodItems))
}

@Composable
fun DragDropMainScreen(
    listWrapper: FoodItemListWrapper
) {
    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(listWrapper.list) { food ->
                FoodItemCard(foodItem = food)
            }
        }
        PersonListContainer()
    }
}

@Composable
fun BoxScope.PersonListContainer() {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
            .padding(vertical = 10.dp)
            .align(Alignment.BottomCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(persons) { person ->
            PersonCard(person = person)
        }
    }
}