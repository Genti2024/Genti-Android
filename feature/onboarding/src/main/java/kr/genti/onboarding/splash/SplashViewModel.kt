package kr.genti.onboarding.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AppUpdateManager
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import kr.genti.domain.usecase.auth.ReissueOldTokensUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val reissueOldTokensUseCase: ReissueOldTokensUseCase
) : ViewModel() {
    private val _splashState = MutableStateFlow(SplashState())
    val splashState = _splashState.asStateFlow()

    private val _splashSideEffect = MutableSharedFlow<SplashSideEffect>()
    val splashSideEffect = _splashSideEffect.asSharedFlow()

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.Init -> handleInit()
            is SplashIntent.LottiePlayFinish -> handleLottiePlayFinish()
            is SplashIntent.AppUpdateFinish -> handleAppUpdateFinish(intent.isAppUpdateSuccess)
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            if (AppUpdateManager.isAppUpdateAvailable()) {
                _splashSideEffect.emit(SplashSideEffect.StartAppUpdate)
            } else {
                val isUserSigned = getIsUserSigned()
                _splashState.update { it.copy(isCheckFinished = true, isUserSigned = isUserSigned) }
                checkAllFinishedAndNavigate()
            }
        }
    }

    private fun handleLottiePlayFinish() {
        viewModelScope.launch {
            _splashState.update { it.copy(isLottieFinished = true) }
            checkAllFinishedAndNavigate()
        }
    }

    private fun handleAppUpdateFinish(isAppUpdateSuccess: Boolean) {
        viewModelScope.launch {
            if (isAppUpdateSuccess) {
                _splashSideEffect.emit(SplashSideEffect.RestartApp)
            } else {
                _splashSideEffect.emit(SplashSideEffect.FinishApp)
            }
        }
    }

    private suspend fun getIsUserSigned(): Boolean =
        if (userRepository.getUserRole() == ROLE_USER) {
            reissueOldTokensUseCase().isSuccess
        } else {
            false
        }

    private suspend fun checkAllFinishedAndNavigate() {
        if (splashState.value.isLottieFinished && splashState.value.isCheckFinished) {
            if (splashState.value.isUserSigned) {
                _splashSideEffect.emit(SplashSideEffect.NavigateToFeed)
            } else {
                _splashSideEffect.emit(SplashSideEffect.NavigateToLogin)
            }
        }
    }

    companion object {
        private const val ROLE_USER = "USER"
    }
}