package kr.genti.presentation.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val serviceTermsList = listOf(NICKNAME, EMAIL)

        private val _isAppLoginAvailable = MutableStateFlow(true)
        val isAppLoginAvailable: StateFlow<Boolean> = _isAppLoginAvailable

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

        private fun changeTokenFromServer(
            accessToken: String,
            social: String = KAKAO,
        ) {
//        viewModelScope.launch {
//            onboardingRepository.postTokenToServiceToken(
//                AuthTokenRequestModel(accessToken, social, deviceToken),
//            )
//                .onSuccess {
//                    // 200(가입된 아이디): 온보딩 뷰 생략하고 바로 메인 화면으로 이동 위해 유저 정보 받기
//                    if (it == null) {
//                        _postChangeTokenResult.emit(false)
//                        return@launch
//                    }
//                    authRepository.setAutoLogin(it.accessToken, it.refreshToken)
//                    isResigned = it.isResigned
//                    getUserDataFromServer()
//                }
//                .onFailure {
//                    // 403, 404 : 온보딩 뷰로 이동 위해 카카오 유저 정보 얻기
//                    if (it is HttpException && (it.code() == 403 || it.code() == 404)) {
//                        getUserInfoFromKakao()
//                    } else {
//                        _postChangeTokenResult.emit(false)
//                    }
//                }
        }

        companion object {
            const val KAKAO = "KAKAO"
            const val NICKNAME = "profile_nickname"
            const val EMAIL = "account_email"
        }
    }
