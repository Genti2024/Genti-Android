plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.core.common"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.amplitude)
    implementation(libs.app.update)
}
