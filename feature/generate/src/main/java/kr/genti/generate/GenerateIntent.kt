package kr.genti.generate

import android.net.Uri
import com.android.billingclient.api.Purchase
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio

sealed class GenerateIntent {
    data class Init(val isParentPic: Boolean) : GenerateIntent()
    data object BackBtnClick : GenerateIntent()
    data object NextBtnClick : GenerateIntent()
    data class NumberSelect(val pictureNumber: PictureNumber) : GenerateIntent()
    data object PromptExampleSwipe : GenerateIntent()
    data class PromptChange(val prompt: String) : GenerateIntent()
    data class RatioSelect(val pictureRatio: PictureRatio) : GenerateIntent()
    data class TextFieldFocused(val isFocused: Boolean) : GenerateIntent()
    data class ImageSelectBtnClick(val isExtra: Boolean) : GenerateIntent()
    data class ImageSelect(val uriList: List<Uri>) : GenerateIntent()
    data class PurchaseSuccess(val purchase: Purchase) : GenerateIntent()
    data object PurchaseFailure : GenerateIntent()
}