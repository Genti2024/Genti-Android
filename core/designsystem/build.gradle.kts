plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.core.designsystem"
}

dependencies {
    implementation(projects.core.common)
}