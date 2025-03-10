plugins {
    id("kr.genti.androidCompose")
    id("kr.genti.version")
}

android {
    namespace = "kr.genti.feature.setting"

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${extra["versionName"]}\"")
        buildConfigField("String", "VERSION_CODE", "\"${extra["versionCode"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)

    implementation(libs.phoenix)
}
