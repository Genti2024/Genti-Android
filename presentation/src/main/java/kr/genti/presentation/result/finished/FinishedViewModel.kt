package kr.genti.presentation.result.finished

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
    @Inject
    constructor(
        // private val profileRepository: ProfileRepository,
    ) : ViewModel() {
        val errorReport = MutableLiveData<String>()

        var finishedImage =
            ImageModel(
                -1,
                "",
                "",
                null,
                null,
            )

        var isRatio23 = true

        fun setPictureRatio() {
            isRatio23 = finishedImage.pictureRatio?.name == PictureRatio.RATIO_2_3.name
        }
    }
