package kr.genti.presentation.main.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        val script = MutableLiveData<String>()
        val isWritten = MutableLiveData(false)

        fun checkWritten() {
            isWritten.value = script.value?.isNotEmpty()
        }
    }