package kr.genti.presentation.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.genti.domain.entity.request.ReissueRequestModel
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _isAutoLogined = MutableSharedFlow<Boolean>()
        val isAutoLogined: SharedFlow<Boolean> = _isAutoLogined

        private val _reissueTokenResult = MutableSharedFlow<Boolean>()
        val reissueTokenResult: SharedFlow<Boolean> = _reissueTokenResult

        fun getAutoLoginState() {
            viewModelScope.launch {
                delay(DELAY_SPLASH)
                if (userRepository.getUserRole() == ROLE_USER) {
                    _isAutoLogined.emit(true)
                } else {
                    _isAutoLogined.emit(false)
                }
            }
        }

        fun postToReissueToken() {
            viewModelScope.launch {
                authRepository.postReissueTokens(
                    ReissueRequestModel(
                        userRepository.getAccessToken(),
                        userRepository.getRefreshToken(),
                    ),
                ).onSuccess {
                    userRepository.setTokens(it.accessToken, it.refreshToken)
                    _reissueTokenResult.emit(true)
                }.onFailure {
                    _reissueTokenResult.emit(false)
                }
            }
        }

        companion object {
            private const val DELAY_SPLASH = 1800L
            private const val ROLE_USER = "USER"
        }
    }
