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
                    "https://github.com/Marchbreeze/Edu-Public-Data/assets/97405341/3f995ad0-def2-479e-bed8-2f33deff684b",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
                FeedItemModel(
                    1,
                    "https://github.com/Marchbreeze/Edu-Public-Data/assets/97405341/3f995ad0-def2-479e-bed8-2f33deff684b",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
                FeedItemModel(
                    2,
                    "https://github.com/Marchbreeze/Edu-Public-Data/assets/97405341/3f995ad0-def2-479e-bed8-2f33deff684b",
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요.",
                ),
            )
    }
