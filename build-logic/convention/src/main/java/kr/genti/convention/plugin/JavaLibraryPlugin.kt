package kr.genti.convention.plugin

import kr.genti.convention.Constants
import kr.genti.convention.extension.applyKotlinCompilerOptions
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.javaLibraryExtension
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * 순수 Kotlin/Java  모듈을 위한 Gradle 플러그인
 *
 * - Gradle 플러그인을 적용하여 Kotlin 및 Java의 설정을 자동으로 구성합니다.
 * - Android 관련 속성이 없는 순수 Kotlin/Java 라이브러리 모듈 (ex. domain 모듈)에 적용됩니다.
 */
class JavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply {
                apply(libs.getPlugin("java-library"))
                apply(libs.getPlugin("kotlin-jvm"))
            }

            javaLibraryExtension.apply {
                sourceCompatibility = Constants.JAVA_VERSION
                targetCompatibility = Constants.JAVA_VERSION
            }

            applyKotlinCompilerOptions(Constants.JVM_VERSION)

            dependencies {
                implementation(libs.getBundle("kotlinx"))
                implementation(libs.getLibrary("kotlinx-coroutines-core"))
                implementation(libs.getLibrary("javax-inject"))
            }
        }
}