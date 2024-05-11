plugins {
    id("composeui.android.application")
    id("composeui.android.hilt")
    // alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "1.9.22"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

//composeCompiler {
//    enableExperimentalStrongSkippingMode = false
//    reportsDestination = layout.buildDirectory.dir("compose_compiler")
//    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
//}

//kotlin {
//    sourceSets.all {
//        languageSettings {
//            languageVersion = "2.0"
//        }
//    }
//}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

    implementation("androidx.compose.animation:animation:1.7.0-alpha04")

    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.seleniumhq.selenium:selenium-java:4.0.0-alpha-3")

    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

//    implementation("io.coil-kt:coil-gif:2.5.0")

    implementation("androidx.biometric:biometric:1.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


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
    androidTestImplementation(platform(libs.compose.bom))
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

    implementation(libs.graphics.shapes)
}
