plugins {
    id("kr.genti.androidCompose")
    id("kr.genti.version")
}

android {
    namespace = "kr.genti.presentation"

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${extra["versionName"]}\"")
        buildConfigField("String", "VERSION_CODE", "\"${extra["versionCode"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.domain)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.ui)
    implementation(libs.kakao)
    implementation(libs.app.update)
    implementation(libs.amplitude)
    implementation(libs.billing.client)
}
