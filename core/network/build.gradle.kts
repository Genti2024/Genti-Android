import kr.genti.convention.extension.implementation
import java.util.Properties

plugins {
    id("kr.genti.androidLibrary")
}

android {
    namespace = "kr.genti.core.network"

    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", properties["test.base.url"].toString())
        }
        release {
            buildConfigField("String", "BASE_URL", properties["base.url"].toString())
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.domain)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(platform(libs.retrofit.bom))
    implementation(libs.bundles.retrofit)
    implementation(libs.phoenix)
}