package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.InfoDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.SignupRequestDto
import kr.genti.data.dto.response.SignUpUserDto
import kr.genti.data.service.InfoService
import javax.inject.Inject

data class InfoDataSourceImpl
    @Inject
    constructor(
        private val infoService: InfoService,
    ) : InfoDataSource {
        override suspend fun postSignupData(request: SignupRequestDto): BaseResponse<SignUpUserDto> = infoService.postSignupData(request)

        override suspend fun postUserLogout(): BaseResponse<Boolean> = infoService.postUserLogout()

        override suspend fun deleteUser(): BaseResponse<Boolean> = infoService.deleteUser()
    }
