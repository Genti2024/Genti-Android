package kr.genti.convention.config

import kr.genti.convention.Constants
import kr.genti.convention.extension.applyKotlinCompilerOptions
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Kotlin & Coroutines 관련 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply(libs.getPlugin("kotlin-android"))
            apply(libs.getPlugin("kotlin-parcelize"))
            apply(libs.getPlugin("kotlin-serialization"))
        }

        applyKotlinCompilerOptions(Constants.JVM_VERSION)

        dependencies {
            implementation(libs.getBundle("kotlinx"))
            implementation(libs.getBundle("coroutines"))
        }
    }
}