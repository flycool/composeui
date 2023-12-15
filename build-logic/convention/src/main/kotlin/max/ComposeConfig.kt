package max

import com.android.build.api.dsl.CommonExtension
import max.dev.ext.libs
import org.gradle.api.Project

internal fun Project.configureCompose(commonExtension: CommonExtension<*, *, *, *, *>) {

    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("kotlinCompilerExtensionVersion").get().toString()
        }
    }
}