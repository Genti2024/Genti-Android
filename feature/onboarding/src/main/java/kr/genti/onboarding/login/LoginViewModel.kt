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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
        }
    }

    private fun startKakaoAppLogin() {
        val appLoginCallback = setLoginCallback(
            onSuccess = { accessToken -> startDeviceLoginToServer(accessToken) },
            onFailure = { startKakaoWebLogin() }
        )
        emitSideEffect(LoginSideEffect.StartKakaoAppLogin(appLoginCallback))
    }

    private fun startKakaoWebLogin() {
        val webLoginCallback = setLoginCallback(
            onSuccess = { accessToken -> startDeviceLoginToServer(accessToken) },
            onFailure = { emitSideEffect(LoginSideEffect.ShowErrorToast) }
        )
        emitSideEffect(LoginSideEffect.StartKakaoWebLogin(webLoginCallback))
    }

    private fun setLoginCallback(
        onSuccess: (accessToken: String) -> Unit,
        onFailure: () -> Unit,
    ): (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error == null && token != null) {
            Timber.tag("okhttp").d("KAKAO ACCESS TOKEN : $token")
            onSuccess(token.accessToken)
        } else if (!(error is ClientError && error.reason == ClientErrorCause.Cancelled)) {
            changeLoadingState(false)
            onFailure()
        }
    }

    private fun startDeviceLoginToServer(accessToken: String) {
        viewModelScope.launch {
            runCatching {
                val deviceToken = getDeviceToken()
                changeTokenFromServer(accessToken, deviceToken)
            }.onSuccess { isAssigned ->
                if (isAssigned) {
                    _loginSideEffect.emit(LoginSideEffect.NavigateToFeed)
                } else {
                    AmplitudeManager.trackEvent("sign_in")
                    _loginSideEffect.emit(LoginSideEffect.NavigateToSignup)
                }
            }.onFailure {
                _loginSideEffect.emit(LoginSideEffect.ShowErrorToast)
            }
        }
        changeLoadingState(false)
    }

    private suspend fun getDeviceToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.tag("okhttp").d("FCM TOKEN : ${task.result}")
                continuation.resume(task.result)
            } else {
                continuation.resumeWithException(task.exception ?: Exception())
            }
        }
    }

    private suspend fun changeTokenFromServer(accessToken: String, fcmToken: String): Boolean =
        getNewTokensFromOauthUseCase(
            newAccessToken = accessToken,
            fcmToken = fcmToken
        ).getOrThrow()
}