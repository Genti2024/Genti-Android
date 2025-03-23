package kr.genti.domain.repository

import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel

interface InfoRepository {
    suspend fun postSignupData(request: SignupRequestModel): SignUpUserModel

    suspend fun postUserLogout(): Boolean

    suspend fun deleteUser(): Boolean
}
