package kr.genti.convention.config

import kr.genti.convention.extension.androidLibraryExtension
import kr.genti.convention.extension.debugImplementation
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Jetpack Compose, Navigation 등 화면 관련 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply(libs.getPlugin("kotlin-compose"))
        }

        androidLibraryExtension.apply {
            buildFeatures {
                compose = true
                // TODO : 추후 제거
                viewBinding = true
                dataBinding = true
            }
        }

        dependencies {
            // TODO : 추후 제거
            implementation(libs.getBundle("androidxXml"))
            implementation(platform(libs.getLibrary("androidx-compose-bom")))
            implementation(libs.getBundle("compose"))
            implementation(libs.getBundle("navigation"))
            implementation(libs.getBundle("ui"))
            debugImplementation(libs.getBundle("androidx-ui-test"))
        }
    }
}