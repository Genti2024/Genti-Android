import kr.genti.convention.extension.implementation

plugins {
    id("kr.genti.androidLibrary")
}

android {
    namespace = "kr.genti.data"
}

dependencies {
    implementation(projects.domain)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(platform(libs.retrofit.bom))
    implementation(libs.bundles.retrofit)
}
