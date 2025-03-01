package kr.genti.convention.config

import com.android.build.gradle.AppExtension
import kr.genti.convention.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(extensions) {
            extraProperties["versionName"] = Constants.VERSION_NAME
            extraProperties["versionCode"] = Constants.VERSION_CODE
        }
    }
}