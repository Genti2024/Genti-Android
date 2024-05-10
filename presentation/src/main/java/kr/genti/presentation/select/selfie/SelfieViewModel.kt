package kr.genti.presentation.select.selfie

import android.net.Uri
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
        var plusImage = Uri.EMPTY
        var angle = -1
        var frame = -1
        var uriList = listOf<Uri>()
        var isSelected = MutableLiveData(false)
    }
