package kr.genti.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kr.genti.android.BuildConfig.AMPLITUDE_KEY
import kr.genti.android.BuildConfig.NATIVE_APP_KEY
import kr.genti.presentation.BuildConfig
import kr.genti.presentation.util.AmplitudeManager
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKakaoSDK()
        initAmplitude()
        setDayMode()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initKakaoSDK() {
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }

    private fun initAmplitude() {
        AmplitudeManager.init(this, AMPLITUDE_KEY)
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
