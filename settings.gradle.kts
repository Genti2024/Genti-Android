enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {

    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // KakaoSDK repository
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "Genti-Android"

include(":app")
include(":domain")
include(":data")
include(":presentation")
include(":core:common")
include(":core:datastore")
include(":core:designsystem")
include(":core:navigation")
include(":core:network")
include(":feature:onboarding")
include(":feature:feed")
include(":feature:main")
include(":feature:profile")
include(":feature:generate")
include(":feature:result")
include(":feature:setting")
