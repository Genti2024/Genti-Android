package kr.genti.presentation.main.create

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        val script = MutableLiveData<String>()
        val isWritten = MutableLiveData(false)
        var plusImage: Uri = Uri.EMPTY
        val selectedAngle = MutableLiveData<Int>(-1)
        val selectedFrame = MutableLiveData<Int>(-1)
        val isSelected = MutableLiveData(false)

        private val _currentPercent = MutableStateFlow<Int>(33)
        val currentPercent: StateFlow<Int> = _currentPercent

        fun modCurrentPercent(amount: Int) {
            _currentPercent.value += amount
        }

        fun checkWritten() {
            isWritten.value = script.value?.isNotEmpty()
        }

        fun selectAngle(itemId: Int) {
            selectedAngle.value = itemId
            checkSelected()
        }

        fun selectFrame(itemId: Int) {
            selectedFrame.value = itemId
            checkSelected()
        }

        private fun checkSelected() {
            isSelected.value = selectedAngle.value != -1 && selectedFrame.value != -1
        }
    }
