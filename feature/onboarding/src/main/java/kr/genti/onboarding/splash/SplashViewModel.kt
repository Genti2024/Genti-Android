package kr.genti.onboarding.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.genti.common.manager.AppUpdateManager
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

    private val _splashState = MutableStateFlow(SplashState())
    val splashState = _splashState.asStateFlow()

    private val _splashSideEffect = MutableSharedFlow<SplashSideEffect>()
    val splashSideEffect = _splashSideEffect.asSharedFlow()

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.Init -> handleInit()
            is SplashIntent.LottiePlayFinish -> handleLottiePlayFinish()
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            if (AppUpdateManager.isAppUpdateAvailable()) {
                _splashSideEffect.emit(SplashSideEffect.StartAppUpdate)
            } else {
                getIsUserSigned()
            }
        }
    }

    private fun handleLottiePlayFinish() {
        viewModelScope.launch {
            _splashSideEffect.emit(SplashSideEffect.NavigateToFeed)
        }
    }

    private fun getIsUserSigned() {

    }
}