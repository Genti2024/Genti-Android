package kr.genti.convention.config

import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.getPlugin
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.ksp
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Hilt 관련 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply(libs.getPlugin("ksp"))
            apply(libs.getPlugin("hilt"))
        }

        dependencies {
            implementation(libs.getLibrary("hilt-android"))
            ksp(libs.getLibrary("hilt-android-compiler"))
        }
    }
}
