// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    `kotlin-dsl`
}

group = "com.compose.sample.composeui.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "composeui.android.application"
            implementationClass = "max.AndroidApplicationConventionPlugin"
        }
        register("androidHilt") {
            id = "composeui.android.hilt"
            implementationClass = "max.AndroidHiltConventionPlugin"
        }
    }
}