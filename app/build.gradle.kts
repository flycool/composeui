plugins {
    id("composeui.android.application")
    id("composeui.android.hilt")
}

android {
    namespace = "com.compose.sample.composeui"

    defaultConfig {
        applicationId = "com.compose.sample.composeui"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

    implementation("androidx.compose.animation:animation:1.7.0-alpha04")

    // Accompanist permission
    // https://google.github.io/accompanist/permissions/
    implementation(libs.google.accompanist.permissions)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.ui.graphics)
    implementation(libs.compose.ui.ui.tooling.preview)
    implementation(libs.compose.material3.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ext.junit)
    androidTestImplementation(libs.androidx.espresso)
    //androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.ui.test.junit4)
    debugImplementation(libs.compose.ui.ui.tooling)
    debugImplementation(libs.compose.ui.ui.test.manifest)

    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.okhttp3)
    implementation(libs.io.coil.kt)
    implementation(libs.androidx.work)
    implementation(libs.compose.runtime.livedata)

    //lifecycle
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.splashscreen)
    implementation(libs.androidx.navigation.compose)

    implementation (libs.graphics.shapes)
}