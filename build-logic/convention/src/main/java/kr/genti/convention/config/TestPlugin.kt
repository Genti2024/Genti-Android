package kr.genti.convention.config

import kr.genti.convention.extension.androidTestImplementation
import kr.genti.convention.extension.getBundle
import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.libs
import kr.genti.convention.extension.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Test 관련 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        dependencies {
            testImplementation(libs.getLibrary("junit"))
            androidTestImplementation(libs.getBundle("androidx-test"))
        }
    }
}