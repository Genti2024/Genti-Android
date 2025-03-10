plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.feature.generate"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)
}