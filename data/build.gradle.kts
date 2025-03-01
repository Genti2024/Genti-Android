plugins {
    id("kr.genti.androidLibrary")
}

android {
    namespace = "kr.genti.data"
}

dependencies {
    implementation(projects.domain)
}
