package kr.genti.presentation.util

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration

object AmplitudeManager {
    private lateinit var amplitude: Amplitude

    fun init(
        context: Context,
        apiKey: String,
    ) {
        amplitude =
            Amplitude(
                Configuration(
                    apiKey = apiKey,
                    context = context.applicationContext,
                ),
            )
    }

    fun logEvent(
        eventName: String,
        properties: Map<String, Any> = emptyMap(),
    ) {
        if (::amplitude.isInitialized) {
            amplitude.logEvent(eventName, properties)
        } else {
            throw IllegalStateException("AmplitudeManager is not initialized. Call init() first.")
        }
    }
}
