package kr.genti.convention.config

import kr.genti.convention.extension.applyJUnitPlatform
import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.libs
import kr.genti.convention.extension.testImplementation
import kr.genti.convention.extension.testRuntimeOnly
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Test 관련 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        applyJUnitPlatform()

        dependencies {
            testRuntimeOnly(libs.getLibrary("junit-jupiter-engine"))
            testImplementation(libs.getLibrary("junit-jupiter-api"))
            testImplementation(libs.getLibrary("junit-jupiter-params"))
        }
    }
}