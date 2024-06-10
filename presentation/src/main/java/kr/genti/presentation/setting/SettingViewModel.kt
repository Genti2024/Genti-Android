package kr.genti.presentation.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository
    ) : ViewModel() {
        private fun clearLocalInfo() {
            // authRepository.clearLocalPref()
        }
    }
