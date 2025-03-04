package kr.genti.convention.extension

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

/**
 * Gradle의 Version Catalog(libs.versions.toml)를 간결하게 활용하기 위한 확장 함수 모음
 *
 * - 기존의 `findBundle(...)`, `findLibrary(...)`, `findPlugin(...)`을 더 직관적으로 사용할 수 있도록 개선합니다.
 * - Catalog에서 라이브러리, 번들, 플러그인을 조회할 때, 존재하지 않으면 예외를 던져 문제를 조기에 감지할 수 있습니다.
 */

fun VersionCatalog.getBundle(bundleName: String): Provider<ExternalModuleDependencyBundle> =
    findBundle(bundleName).orElseThrow {
        NoSuchElementException("Bundle with name $bundleName not found in the catalog")
    }

fun VersionCatalog.getLibrary(libraryName: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(libraryName).orElseThrow {
        NoSuchElementException("Library with name $libraryName not found in the catalog")
    }

fun VersionCatalog.getPlugin(pluginName: String): String =
    findPlugin(pluginName)
        .orElseThrow {
            NoSuchElementException("Plugin with name $pluginName not found in the catalog")
        }
        .get().pluginId