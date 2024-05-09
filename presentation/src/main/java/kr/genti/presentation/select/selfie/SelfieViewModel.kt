package kr.genti.presentation.select.selfie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelfieViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        var script = ""
        var angle = -1
        var frame = -1
        var isSelected = MutableLiveData(false)

        private fun checkSelected() {
        }
    }
