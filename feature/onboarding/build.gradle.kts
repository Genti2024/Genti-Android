plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.feature.onboarding"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)

    implementation(libs.kakao)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.phoenix)
}