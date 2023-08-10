package com.compose.sample.composeui.dragfood.model

import androidx.annotation.DrawableRes
import com.compose.sample.composeui.R

data class FoodItem(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int,
    val price: Double
)


val foodItems = listOf(
    FoodItem(
        id = 1,
        name = "Pizza",
        image = R.drawable.food_pizza,
        price = 20.0
    ),
    FoodItem(
        id = 2,
        name = "Pizza",
        image = R.drawable.food_cake,
        price = 10.5
    ),
    FoodItem(
        id = 3,
        name = "Pizza",
        image = R.drawable.food_toast,
        price = 12.99
    ),
)