buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        ClassPathPlugins.run {
            classpath(gradle)
            classpath(kotlinGradlePlugin)
            classpath(hiltGradlePlugin)
            classpath(googleServices)
            classpath(crashlyticsGradle)
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
