package kr.genti.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
            is SettingIntent.Init -> handleInit()
            is SettingIntent.BackButtonClick -> handleBackButtonClick()
            is SettingIntent.TermButtonClick -> handleSettingButtonClick(WEB_TERMS_OF_SERVICE)
            is SettingIntent.PrivacyButtonClick -> handleSettingButtonClick(WEB_PRIVACY_POLICY)
            is SettingIntent.CompanyButtonClick -> handleSettingButtonClick(WEB_COMPANY_INFO)
            is SettingIntent.QuestionButtonClick -> handleSettingButtonClick(WEB_QUESTION)
            is SettingIntent.LogoutButtonClick -> handleLogoutButtonClick()
            is SettingIntent.QuitButtonClick -> handleQuitButtonClick()
        }
    }

    private fun handleInit() {

    }

    private fun handleBackButtonClick() {
        
    }

    private fun handleSettingButtonClick(url: String) {

    }

    private fun handleLogoutButtonClick() {

    }

    private fun handleQuitButtonClick() {

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