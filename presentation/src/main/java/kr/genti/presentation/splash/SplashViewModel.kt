package kr.genti.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        // private val userRepository: UserRepository
    ) : ViewModel() {
        private val _isAutoLogined = MutableSharedFlow<Boolean>()
        val isAutoLogined: SharedFlow<Boolean> = _isAutoLogined

        init {
            getAutoLoginState()
        }

        private fun getAutoLoginState() {
            viewModelScope.launch {
            }
        }
    }
