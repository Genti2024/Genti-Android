package kr.genti.presentation.main.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.entity.response.ImageModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        // private val profileRepository: ProfileRepository,
    ) : ViewModel() {
        val mockItemList =
            listOf(
                ImageModel(
                    0,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                ),
                ImageModel(
                    0,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
                ImageModel(
                    1,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
                ImageModel(
                    2,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                ),
                ImageModel(
                    3,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                ),
                ImageModel(
                    4,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
                ImageModel(
                    5,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
                ImageModel(
                    6,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
                ImageModel(
                    7,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                ),
            )
    }
