package kr.genti.onboarding.login

import com.kakao.sdk.auth.model.OAuthToken

sealed class LoginSideEffect {
    data object ShowErrorToast : LoginSideEffect()

    data object NavigateToSignup : LoginSideEffect()

    data object NavigateToFeed : LoginSideEffect()

    data class StartKakaoAppLogin(
        val appLoginCallback: (OAuthToken?, Throwable?) -> Unit
    ) : LoginSideEffect()

    data class StartKakaoWebLogin(
        val webLoginCallback: (OAuthToken?, Throwable?) -> Unit
    ) : LoginSideEffect()
}