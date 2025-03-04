package kr.genti.convention.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Gradle 프로젝트 설정을 간결하게 만들기 위한 확장 프로퍼티 및 함수 모음
 *
 * - Project의 확장 프로퍼티를 활용하여 Gradle 설정을 더 직관적으로 작성할 수 있습니다.
 * - 기존의 `extensions.configure<...>()` 형태를 프로퍼티 호출만으로 사용할 수 있도록 개선합니다.
 * - Kotlin 컴파일러 옵션 및 Android/Java 설정을 간결하게 적용할 수 있습니다.
 */

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.androidApplicationExtension: ApplicationExtension
    get() = extensions.getByType()

val Project.androidLibraryExtension: LibraryExtension
    get() = extensions.getByType()

val Project.javaLibraryExtension: JavaPluginExtension
    get() = extensions.getByType()

fun Project.applyKotlinCompilerOptions(jvmVersion: JvmTarget) {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }
}