plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("AndroidApplicationPlugin") {
            id = "kr.genti.androidApplication"
            implementationClass = "kr.genti.convention.plugin.AndroidApplicationPlugin"
        }
        register("AndroidLibraryPlugin") {
            id = "kr.genti.androidLibrary"
            implementationClass = "kr.genti.convention.plugin.AndroidLibraryPlugin"
        }
        register("AndroidComposePlugin") {
            id = "kr.genti.androidCompose"
            implementationClass = "kr.genti.convention.plugin.AndroidComposePlugin"
        }
        register("JavaLibraryPlugin") {
            id = "kr.genti.javaLibrary"
            implementationClass = "kr.genti.convention.plugin.JavaLibraryPlugin"
        }

        register("CommonPlugin") {
            id = "kr.genti.common"
            implementationClass = "kr.genti.convention.config.CommonPlugin"
        }
        register("ComposePlugin") {
            id = "kr.genti.compose"
            implementationClass = "kr.genti.convention.config.ComposePlugin"
        }
        register("KotlinPlugin") {
            id = "kr.genti.kotlin"
            implementationClass = "kr.genti.convention.config.KotlinPlugin"
        }
        register("HiltPlugin") {
            id = "kr.genti.hilt"
            implementationClass = "kr.genti.convention.config.HiltPlugin"
        }
        register("TestPlugin") {
            id = "kr.genti.test"
            implementationClass = "kr.genti.convention.config.TestPlugin"
        }
        register("versionPlugin") {
            id = "kr.genti.version"
            implementationClass = "kr.genti.convention.config.VersionPlugin"
        }
    }
}