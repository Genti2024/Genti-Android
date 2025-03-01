import java.util.Properties

plugins {
    id("kr.genti.androidApplication")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    defaultConfig {
        buildConfigField("String", "NATIVE_APP_KEY", properties["native.app.key"].toString())
        manifestPlaceholders["NATIVE_APP_KEY"] = properties["nativeAppKey"].toString()

        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        if (keystorePropertiesFile.exists()) {
            keystoreProperties.load(keystorePropertiesFile.inputStream())
        }
        signingConfigs {
            create("release") {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", properties["test.base.url"].toString())
            buildConfigField("String", "AMPLITUDE_KEY", properties["amplitude.test.key"].toString())
        }
        release {
            buildConfigField("String", "BASE_URL", properties["base.url"].toString())
            buildConfigField("String", "AMPLITUDE_KEY", properties["amplitude.api.key"].toString())
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.kakao)
}
