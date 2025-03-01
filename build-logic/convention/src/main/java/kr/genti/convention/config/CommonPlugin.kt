package kr.genti.convention.config

import kr.genti.convention.extension.getLibrary
import kr.genti.convention.extension.implementation
import kr.genti.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * 모든 Android 프로젝트에서 공통적으로 적용할 Gradle 플러그인을 정의하는 커스텀 플러그인입니다.
 */
class CommonPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            implementation(libs.getLibrary("timber"))
        }
    }
}
