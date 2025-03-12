package kr.genti.generate.model

import kr.genti.domain.enums.PictureNumber

enum class GenerateType {
    FREE_ONE,
    PAID_ONE,
    PAID_TWO,
    NONE;

    companion object {
        fun getGenerateType(isParent: Boolean, pictureNumber: PictureNumber): GenerateType =
            when {
                isParent && pictureNumber == PictureNumber.TWO -> PAID_TWO
                isParent -> PAID_ONE
                else -> FREE_ONE
            }
    }
}