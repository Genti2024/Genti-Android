package kr.genti.convention.plugin

import kr.genti.convention.Constants
import kr.genti.convention.config.CommonPlugin
import kr.genti.convention.config.HiltPlugin
import kr.genti.convention.config.KotlinPlugin
import kr.genti.convention.config.TestPlugin
import kr.genti.convention.extension.androidLibraryExtension
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Android 라이브러리 모듈을 위한 Gradle 플러그인
 *
 * - Gradle 플러그인을 적용하여 Android 라이브러리 모듈의 설정을 자동으로 구성합니다.
 * - AndroidX 번들과, Kotlin, Hilt, Test 관련 플러그인을 함께 적용합니다.
 * - Jetpack Compose를 사용하지 않는 Android 라이브러리 (ex. data 모듈)에 적용됩니다.
 */
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply {
                apply(libs.getPlugin("android-library"))
                apply<KotlinPlugin>()
                apply<HiltPlugin>()
                apply<CommonPlugin>()
                apply<TestPlugin>()
            }

            androidLibraryExtension.apply {
                compileSdk = Constants.COMPILE_SDK

                defaultConfig {
                    minSdk = Constants.MIN_SDK
                    consumerProguardFiles("consumer-rules.pro")
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = Constants.JAVA_VERSION
                    targetCompatibility = Constants.JAVA_VERSION
                }
            }

            dependencies {
                implementation(libs.getBundle("androidx"))
            }
        }
}