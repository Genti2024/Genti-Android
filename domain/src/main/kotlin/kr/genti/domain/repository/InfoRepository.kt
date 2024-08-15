package kr.genti.domain.repository

import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel

interface InfoRepository {
    suspend fun postSignupData(request: SignupRequestModel): Result<SignUpUserModel>

    suspend fun postUserLogout(): Result<Boolean>

    suspend fun deleteUser(): Result<Boolean>
}
