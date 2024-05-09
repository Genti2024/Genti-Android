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

        // TODO: false로 수정
        var isSelected = MutableLiveData(true)

        private fun checkSelected() {
        }
    }
