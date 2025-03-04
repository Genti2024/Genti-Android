plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.core.common"
}

dependencies {
    implementation(libs.amplitude)
}
