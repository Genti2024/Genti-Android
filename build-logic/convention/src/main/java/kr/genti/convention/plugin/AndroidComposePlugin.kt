package kr.genti.convention.plugin

import kr.genti.convention.config.ComposePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * Jetpack Compose 설정이 적용된 Android 라이브러리 모듈 Gradle 플러그인
 *
 * - 기존 AndroidLibraryPlugin에 ComposePlugin을 추가적으로 적용합니다.
 * - Jetpack Compose를 사용하는 Android 라이브러리 (ex. presentation 모듈)에 적용됩니다.
 */
class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply<AndroidLibraryPlugin>()
                apply<ComposePlugin>()
            }
        }
    }
}