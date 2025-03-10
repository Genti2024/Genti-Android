package kr.genti.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()

    private val _settingSideEffect = MutableSharedFlow<SettingSideEffect>()
    val settingSideEffect = _settingSideEffect.asSharedFlow()

    fun onIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.BackButtonClick -> handleBackButtonClick()
            is SettingIntent.TermButtonClick -> handleSettingButtonClick(WEB_TERMS_OF_SERVICE)
            is SettingIntent.PrivacyButtonClick -> handleSettingButtonClick(WEB_PRIVACY_POLICY)
            is SettingIntent.CompanyButtonClick -> handleSettingButtonClick(WEB_COMPANY_INFO)
            is SettingIntent.QuestionButtonClick -> handleSettingButtonClick(WEB_QUESTION)
            is SettingIntent.LogoutButtonClick -> handleLogoutDialog(true)
            is SettingIntent.QuitButtonClick -> handleQuitDialog(true)
            is SettingIntent.LogoutDialogDismiss -> handleLogoutDialog(false)
            is SettingIntent.QuitDialogDismiss -> handleQuitDialog(false)
            is SettingIntent.LogoutRequest -> handleLogoutRequest()
            is SettingIntent.QuitRequest -> handleQuitRequest()
        }
    }

    private fun handleBackButtonClick() {
        viewModelScope.launch {
            _settingSideEffect.emit(SettingSideEffect.NavigateToBack)
        }
    }

    private fun handleSettingButtonClick(url: String) {
        viewModelScope.launch {
            _settingSideEffect.emit(SettingSideEffect.NavigateToWeb(url))
        }
    }

    private fun handleLogoutDialog(isVisible: Boolean) {
        _settingState.update {
            it.copy(isLogoutDialogVisible = isVisible)
        }
    }

    private fun handleQuitDialog(isVisible: Boolean) {
        _settingState.update {
            it.copy(isQuitDialogVisible = isVisible)
        }
    }

    private fun handleLogoutRequest() {
        viewModelScope.launch {
            logoutFromServer()
        }
    }

    private fun handleQuitRequest() {
        viewModelScope.launch {
            quitFromServer()
        }
    }

    private suspend fun logoutFromServer() {
        infoRepository.postUserLogout()
            .onSuccess {
                userRepository.clearInfo()
                _settingSideEffect.emit(SettingSideEffect.RestartApp)
            }.onFailure {
                _settingState.update {
                    it.copy(isLogoutDialogVisible = false)
                }
                _settingSideEffect.emit(SettingSideEffect.ShowErrorToast)
            }
    }

    private suspend fun quitFromServer() {
        infoRepository.deleteUser()
            .onSuccess {
                userRepository.clearInfo()
                _settingSideEffect.emit(SettingSideEffect.RestartApp)
            }.onFailure {
                _settingState.update {
                    it.copy(isQuitDialogVisible = false)
                }
                _settingSideEffect.emit(SettingSideEffect.ShowErrorToast)
            }
    }

    companion object {
        private const val WEB_TERMS_OF_SERVICE =
            "https://stealth-goose-156.notion.site/5e84488cbf874b8f91e779ea4dc8f08a?pvs=4"
        private const val WEB_PRIVACY_POLICY =
            "https://stealth-goose-156.notion.site/e0f2e17a3a60437b8e62423f61cca2a9?pvs=4"
        private const val WEB_COMPANY_INFO =
            "https://stealth-goose-156.notion.site/39d39ae82a3a436fa053e5287ff9742c?pvs=4"
        private const val WEB_QUESTION =
            "https://stealth-goose-156.notion.site/14537d728f968060b2ffd1c19aff6298?pvs=4"
    }
}