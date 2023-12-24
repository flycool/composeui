package com.compose.sample.funui.splashscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel: ViewModel() {

    private var _ready = MutableStateFlow(false)
    val ready = _ready.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000L)
            _ready.value = true
        }
    }
}