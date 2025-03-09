package kr.genti.profile

import kr.genti.domain.entity.response.ImageModel

sealed class ProfileIntent {
    data object Init: ProfileIntent()
    data class ImageItemClick(val item: ImageModel): ProfileIntent()
    data object GenerateBtnClick: ProfileIntent()
    data object SettingBtnClick: ProfileIntent()
    data object LastColumnLoaded: ProfileIntent()
}