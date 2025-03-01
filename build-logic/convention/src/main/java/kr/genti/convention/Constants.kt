package kr.genti.convention

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Constants {
    const val PACKAGE_NAME = "kr.genti.android"

    const val COMPILE_SDK = 35
    const val MIN_SDK = 28
    const val TARGET_SDK = 35

    const val VERSION_CODE = 25
    const val VERSION_NAME = "2.1.2"

    val JVM_VERSION = JvmTarget.JVM_11
    val JAVA_VERSION = JavaVersion.VERSION_11
}
