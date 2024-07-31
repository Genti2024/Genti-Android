package kr.genti.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val infoRepository: InfoRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _userLogoutState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val userLogoutState: StateFlow<UiState<Boolean>> = _userLogoutState

        private val _userDeleteState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val userDeleteState: StateFlow<UiState<Boolean>> = _userDeleteState

        fun logoutFromKakao() {
            _userLogoutState.value = UiState.Loading
            UserApiClient.instance.logout { error ->
                if (error == null) {
                    logoutFromServer()
                } else {
                    _userLogoutState.value = UiState.Failure(error.toString())
                }
            }
        }

        private fun logoutFromServer() {
            viewModelScope.launch {
                infoRepository.postUserLogout()
                    .onSuccess {
                        userRepository.clearInfo()
                        _userLogoutState.value = UiState.Success(it)
                    }.onFailure {
                        _userLogoutState.value = UiState.Failure(it.message.toString())
                    }
            }
        }

        fun quitFromKakao() {
            _userDeleteState.value = UiState.Loading
            UserApiClient.instance.unlink { error ->
                if (error == null) {
                    quitFromServer()
                } else {
                    _userDeleteState.value = UiState.Failure(error.toString())
                }
            }
        }

        private fun quitFromServer() {
            viewModelScope.launch {
                infoRepository.deleteUser()
                    .onSuccess {
                        userRepository.clearInfo()
                        _userDeleteState.value = UiState.Success(it)
                    }.onFailure {
                        _userDeleteState.value = UiState.Failure(it.message.toString())
                    }
            }
        }
    }
