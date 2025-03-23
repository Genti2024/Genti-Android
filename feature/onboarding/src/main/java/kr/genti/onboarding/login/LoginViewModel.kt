package kr.genti.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.usecase.auth.GetNewTokensFromOauthUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val getNewTokensFromOauthUseCase: GetNewTokensFromOauthUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginSideEffect = MutableSharedFlow<LoginSideEffect>()
    val loginSideEffect = _loginSideEffect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Init -> handleInit(intent.isAppLoginAvailable)
            is LoginIntent.LoginBtnClick -> handleLoginBtnClick()
        }
    }

    private fun handleInit(isAppLoginAvailable: Boolean) {
        _loginState.update {
            it.copy(isAppLoginAvailable = isAppLoginAvailable)
        }
    }

    private fun handleLoginBtnClick() {
        changeLoadingState(true)
        if (loginState.value.isAppLoginAvailable) {
            startKakaoAppLogin()
        } else {
            startKakaoWebLogin()
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _loginState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun emitSideEffect(effect: LoginSideEffect) {
        viewModelScope.launch {
            _loginSideEffect.emit(effect)
            if (effect == LoginSideEffect.ShowErrorToast) changeLoadingState(false)
        }
    }


    private fun startKakaoAppLogin() {
        val appLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> {
                    if (!(error is ClientError && error.reason == ClientErrorCause.Cancelled)) {
                        startKakaoWebLogin()
                    }
                }

                token != null -> {
                    Timber.tag("okhttp").d("KAKAO ACCESS TOKEN FROM APP : $token")
                    getDeviceToken(token.accessToken)
                }

                else -> emitSideEffect(LoginSideEffect.ShowErrorToast)
            }
        }
        emitSideEffect(LoginSideEffect.StartKakaoAppLogin(appLoginCallback))
    }

    private fun startKakaoWebLogin() {
        val webLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error == null && token != null) {
                Timber.tag("okhttp").d("KAKAO ACCESS TOKEN FROM WEB : $token")
                getDeviceToken(token.accessToken)
            } else {
                if (!(error is ClientError && error.reason == ClientErrorCause.Cancelled)) {
                    emitSideEffect(LoginSideEffect.ShowErrorToast)
                }
            }
        }
        emitSideEffect(LoginSideEffect.StartKakaoWebLogin(webLoginCallback))
    }


    private fun getDeviceToken(accessToken: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            viewModelScope.launch {
                if (task.isSuccessful) {
                    Timber.tag("okhttp").d("FCM TOKEN : ${task.result}")
                    changeTokenFromServer(accessToken, task.result)
                } else {
                    emitSideEffect(LoginSideEffect.ShowErrorToast)
                }
            }
        }
    }

    private fun changeTokenFromServer(accessToken: String, fcmToken: String) {
        viewModelScope.launch {
            getNewTokensFromOauthUseCase(
                newAccessToken = accessToken,
                fcmToken = fcmToken
            ).onSuccess { isAssigned ->
                if (isAssigned) {
                    emitSideEffect(LoginSideEffect.NavigateToFeed)
                } else {
                    AmplitudeManager.trackEvent("sign_in")
                    emitSideEffect(LoginSideEffect.NavigateToSignup)
                }
            }.onFailure {
                emitSideEffect(LoginSideEffect.ShowErrorToast)
            }
        }
    }
}