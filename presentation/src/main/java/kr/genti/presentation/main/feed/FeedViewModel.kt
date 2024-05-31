package kr.genti.presentation.main.feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.entity.response.FeedItemModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
    @Inject
    constructor(
        // private val profileRepository: ProfileRepository,
    ) : ViewModel() {
        val mockItemList =
            listOf(
                FeedItemModel(
                    0,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
                FeedItemModel(
                    1,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
                FeedItemModel(
                    2,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
            )
    }
