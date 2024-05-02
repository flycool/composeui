// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    //alias(libs.plugins.compose.compiler) apply false
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        compilerOptions {
            freeCompilerArgs.addAll("-P","plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                    project.layout.buildDirectory + "/compose_metrics")
        }
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.layout.buildDirectory + "/compose_metrics"
            )
        }
//        kotlinOptions.freeCompilerArgs += listOf(
//            "-P",
//            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
//                    project.buildDir.absolutePath + "/compose_metrics"
//        )
//        kotlinOptions.freeCompilerArgs += listOf(
//            "-P",
//            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
//                    project.buildDir.absolutePath + "/compose_metrics"
//        )
    }
}