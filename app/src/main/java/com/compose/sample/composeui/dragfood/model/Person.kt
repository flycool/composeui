package com.compose.sample.composeui.dragfood.model

import androidx.annotation.DrawableRes
import com.compose.sample.composeui.R

data class Person(
    val id: Int,
    val name: String,
    @DrawableRes val profile: Int,
)


val persons = listOf(
    Person(
        id = 1,
        name = "Jhone",
        profile = R.drawable.user_one
    ),
    Person(
        id = 2,
        name = "Eyle",
        profile = R.drawable.user_two
    ),
    Person(
        id = 3,
        name = "Tommy",
        profile = R.drawable.user_three
    )
)