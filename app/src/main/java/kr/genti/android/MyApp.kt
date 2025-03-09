package kr.genti.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kr.genti.android.BuildConfig.AMPLITUDE_KEY
import kr.genti.android.BuildConfig.NATIVE_APP_KEY
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.AppUpdateManager
import kr.genti.common.manager.ImageManager
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKakaoSDK()
        initManagers()
        setDayMode()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initKakaoSDK() {
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }

    private fun initManagers() {
        AmplitudeManager.init(this, AMPLITUDE_KEY)
        AppUpdateManager.init(this)
        ImageManager.init(this)
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
