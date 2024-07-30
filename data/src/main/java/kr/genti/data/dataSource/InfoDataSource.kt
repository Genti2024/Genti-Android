package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.SignupRequestDto

interface InfoDataSource {
    suspend fun postSignupData(request: SignupRequestDto): BaseResponse<Boolean>
}
