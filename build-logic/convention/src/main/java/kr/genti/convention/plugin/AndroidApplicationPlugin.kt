package kr.genti.convention.plugin

import kr.genti.convention.Constants
import kr.genti.convention.config.CommonPlugin
import kr.genti.convention.config.HiltPlugin
import kr.genti.convention.config.KotlinPlugin
import kr.genti.convention.config.TestPlugin
import kr.genti.convention.extension.androidApplicationExtension
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Android App 모듈을 위한 Gradle 플러그인
 *
 * - Gradle 플러그인을 적용하여 Android 애플리케이션의 설정을 자동으로 구성합니다.
 * - `android {}` 블록을 활용해 네임스페이스, SDK 버전, 빌드 타입 등을 설정합니다.
 * - AndroidX 번들과, Kotlin, Hilt, Test 관련 플러그인을 함께 적용합니다.
 */
class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply(libs.getPlugin("android-application"))
            apply<KotlinPlugin>()
            apply<HiltPlugin>()
            apply<CommonPlugin>()
            apply<TestPlugin>()
        }

        androidApplicationExtension.apply {
            namespace = Constants.PACKAGE_NAME
            compileSdk = Constants.COMPILE_SDK

            defaultConfig {
                applicationId = Constants.PACKAGE_NAME
                targetSdk = Constants.TARGET_SDK
                minSdk = Constants.MIN_SDK
                versionCode = Constants.VERSION_CODE
                versionName = Constants.VERSION_NAME
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = Constants.JAVA_VERSION
                targetCompatibility = Constants.JAVA_VERSION
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro",
                    )
                }
            }

            buildFeatures {
                buildConfig = true
            }
        }

        dependencies {
            implementation(libs.getBundle("androidx"))
        }
    }
}