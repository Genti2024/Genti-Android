package kr.genti.convention.extension

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Gradle 종속성(Dependency) 관리 확장 함수 모음
 *
 * - DependencyHandlerScope를 확장하여 Gradle의 의존성 관리 코드를 간결하게 만들기 위한 확장 함수를 제공합니다.
 * - 기존의 `dependencies { implementation(...) }` 형태를 " "을 제거하고 작성할 수 있습니다.
 */

fun DependencyHandlerScope.implementation(project: Project) {
    "implementation"(project)
}

fun DependencyHandlerScope.implementation(provider: Provider<*>) {
    "implementation"(provider)
}

fun DependencyHandlerScope.implementation(fileTree: ConfigurableFileTree) {
    "implementation"(fileTree)
}

fun DependencyHandlerScope.implementation(fileCollection: ConfigurableFileCollection) {
    "implementation"(fileCollection)
}

fun DependencyHandlerScope.debugImplementation(provider: Provider<*>) {
    "debugImplementation"(provider)
}

fun DependencyHandlerScope.releaseImplementation(provider: Provider<*>) {
    "releaseImplementation"(provider)
}

fun DependencyHandlerScope.ksp(provider: Provider<*>) {
    "ksp"(provider)
}

fun DependencyHandlerScope.kspTest(provider: Provider<*>) {
    "kspTest"(provider)
}

fun DependencyHandlerScope.androidTestImplementation(provider: Provider<*>) {
    "androidTestImplementation"(provider)
}

fun DependencyHandlerScope.testImplementation(provider: Provider<*>) {
    "testImplementation"(provider)
}

fun DependencyHandlerScope.testRuntimeOnly(provider: Provider<*>) {
    "testRuntimeOnly"(provider)
}