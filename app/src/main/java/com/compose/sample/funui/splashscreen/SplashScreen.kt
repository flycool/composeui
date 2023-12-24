package com.compose.sample.funui.splashscreen

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreenViewProvider

fun onExitSplashScreenAnimation(screen: SplashScreenViewProvider) {
    val zoomX = ObjectAnimator.ofFloat(
        screen.iconView,
        View.SCALE_X,
        0.4f,
        0.0f
    )
    zoomX.interpolator = OvershootInterpolator()
    zoomX.duration = 500L
    zoomX.doOnEnd { screen.remove() }

    val zoomY = ObjectAnimator.ofFloat(
        screen.iconView,
        View.SCALE_X,
        0.4f,
        0.0f
    )
    zoomY.interpolator = OvershootInterpolator()
    zoomY.duration = 500L
    zoomY.doOnEnd { screen.remove() }

    zoomX.start()
    zoomY.start()

}