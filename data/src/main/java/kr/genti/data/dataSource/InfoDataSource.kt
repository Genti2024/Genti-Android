package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.SignupRequestDto
import kr.genti.data.dto.response.SignUpUserDto

interface InfoDataSource {
    suspend fun postSignupData(request: SignupRequestDto): BaseResponse<SignUpUserDto>

    suspend fun postUserLogout(): BaseResponse<Boolean>

    suspend fun deleteUser(): BaseResponse<Boolean>
}
