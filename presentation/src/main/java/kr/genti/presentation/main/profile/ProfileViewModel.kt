package kr.genti.presentation.main.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        // private val profileRepository: ProfileRepository,
    ) : ViewModel() {
        val mockItemList = listOf("")
    }
