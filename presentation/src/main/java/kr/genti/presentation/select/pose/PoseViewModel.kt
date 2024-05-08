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

        fun selectAngle(itemId: Int) {
            selectedAngle.value = itemId
        }
    }
