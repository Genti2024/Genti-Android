package kr.genti.presentation.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.request.AuthRequestModel
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val serviceTermsList = listOf(NICKNAME, EMAIL)

        private val _isAppLoginAvailable = MutableStateFlow(true)
        val isAppLoginAvailable: StateFlow<Boolean> = _isAppLoginAvailable

        private val _changeTokenState = MutableStateFlow<UiState<String>>(UiState.Empty)
        val changeTokenState: StateFlow<UiState<String>> = _changeTokenState

        private val _kakaoBasicInfoState = MutableStateFlow<UiState<User?>>(UiState.Empty)
        val kakaoBasicInfoState: StateFlow<UiState<User?>> = _kakaoBasicInfoState

        private var appLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                if (!(error is ClientError && error.reason == ClientErrorCause.Cancelled)) {
                    _isAppLoginAvailable.value = false
                }
            } else if (token != null) {
                changeTokenFromServer(token.accessToken)
            }
        }

        private var webLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error == null && token != null) {
                changeTokenFromServer(token.accessToken)
            }
        }

        fun startLogInWithKakao(context: Context) {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context) && isAppLoginAvailable.value) {
                UserApiClient.instance.loginWithKakaoTalk(
                    context = context,
                    callback = appLoginCallback,
                    serviceTerms = serviceTermsList,
                )
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    context = context,
                    callback = webLoginCallback,
                    serviceTerms = serviceTermsList,
                )
            }
        }

        private fun changeTokenFromServer(accessToken: String) {
            viewModelScope.launch {
                authRepository.postOauthDataToGetToken(AuthRequestModel(accessToken, KAKAO))
                    .onSuccess {
                        userRepository.setTokens(it.accessToken, it.refreshToken)
                        _changeTokenState.value = UiState.Success(it.userRoleString)
                    }
                    .onFailure {
                        _changeTokenState.value = UiState.Failure(it.message.toString())
                    }
            }
        }

        fun getUserInfoFromKakao() {
            UserApiClient.instance.me { user, _ ->
                runCatching {
                    UiState.Success(user)
                }.onSuccess {
                    _kakaoBasicInfoState.value = it
                }.onFailure { e ->
                    _kakaoBasicInfoState.value = UiState.Failure(e.message.toString())
                }
            }
        }

        companion object {
            const val KAKAO = "KAKAO"
            const val NICKNAME = "profile_nickname"
            const val EMAIL = "account_email"
        }
    }
