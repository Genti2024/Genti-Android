plugins {
    id("kr.genti.androidCompose")
}

android {
    namespace = "kr.genti.feature.main"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)
    implementation(projects.feature.feed)
    implementation(projects.feature.profile)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.generate)
    implementation(projects.feature.result)
    implementation(projects.feature.setting)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
