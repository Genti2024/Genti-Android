package kr.genti.presentation.result.finished

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.entity.response.ImageModel
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
    @Inject
    constructor(
        // private val profileRepository: ProfileRepository,
    ) : ViewModel() {
        val errorReport = MutableLiveData<String>()

        // TODO: 이미지 받아오기
        val finishedImage =
            ImageModel(
                0,
                "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
            )
    }
