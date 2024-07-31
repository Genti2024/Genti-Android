package kr.genti.domain.repository

import kr.genti.domain.entity.request.SignupRequestModel

interface InfoRepository {
    suspend fun postSignupData(request: SignupRequestModel): Result<Boolean>

    suspend fun postUserLogout(): Result<Boolean>

    suspend fun deleteUser(): Result<Boolean>
}
