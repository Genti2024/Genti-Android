package kr.genti.presentation.select.pose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PoseViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        val selectedAngle = MutableLiveData<Int>(-1)
        val selectedFrame = MutableLiveData<Int>(-1)
        val isSelected = MutableLiveData(false)

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
