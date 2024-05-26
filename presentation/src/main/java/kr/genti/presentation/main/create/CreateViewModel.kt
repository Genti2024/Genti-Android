package kr.genti.presentation.main.create

import android.net.Uri
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
        var currentPercent = 33
        val script = MutableLiveData<String>()
        val isWritten = MutableLiveData(false)
        var plusImage: Uri = Uri.EMPTY

        fun checkWritten() {
            isWritten.value = script.value?.isNotEmpty()
        }
    }
