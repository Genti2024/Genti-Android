package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.CreateDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.CreateRequestDto
import kr.genti.data.dto.request.S3RequestDto
import kr.genti.data.dto.response.S3PresignedUrlDto
import kr.genti.data.service.CreateService
import javax.inject.Inject

data class CreateDataSourceImpl
    @Inject
    constructor(
        private val createService: CreateService,
    ) : CreateDataSource {
        override suspend fun getSingleS3Url(request: S3RequestDto): BaseResponse<S3PresignedUrlDto> = createService.getSingleS3Url(request)

        override suspend fun getMultiS3Url(request: List<S3RequestDto>): BaseResponse<List<S3PresignedUrlDto>> =
            createService.getMultiS3Url(request)

        override suspend fun postToCreate(request: CreateRequestDto): BaseResponse<Boolean> = createService.postToCreate(request)
    }
