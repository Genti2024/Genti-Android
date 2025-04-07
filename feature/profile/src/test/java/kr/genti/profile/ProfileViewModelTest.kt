package kr.genti.profile

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.entity.response.PicturePagedListModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.usecase.profile.GetGeneratedPictureListUseCase
import kr.genti.domain.usecase.result.GetCurrentGenerateStatusUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getGeneratedPictureListUseCase: GetGeneratedPictureListUseCase =
        mockk(relaxed = true)
    private val getCurrentGenerateStatusUseCase: GetCurrentGenerateStatusUseCase =
        mockk(relaxed = true)

    private lateinit var viewModel: ProfileViewModel

    private val fakeImageItemList = listOf(
        ImageModel(1, "url1", PictureRatio.RATIO_GARO),
        ImageModel(2, "url2", PictureRatio.RATIO_SERO),
        ImageModel(3, "url3", PictureRatio.RATIO_GARO),
        ImageModel(4, "url4", PictureRatio.RATIO_SERO),
        ImageModel(5, "url5", PictureRatio.RATIO_GARO),
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // mock use case를 통해 뷰모델 구성
        viewModel = ProfileViewModel(
            getGeneratedPictureListUseCase = getGeneratedPictureListUseCase,
            getCurrentGenerateStatusUseCase = getCurrentGenerateStatusUseCase
        )
        // AmplitudeManager 객체를 모킹하고, 기능을 stub 처리
        mockkObject(AmplitudeManager)
        every { AmplitudeManager.trackEvent(any()) } just Runs
        every { AmplitudeManager.trackEvent(any(), any(), any()) } just Runs

        coEvery { getGeneratedPictureListUseCase(any()) } returns
                Result.success(PicturePagedListModel(5, fakeImageItemList))
        coEvery { getCurrentGenerateStatusUseCase() } returns
                Result.success(GenerateStatusModel(1, GenerateStatus.EMPTY, null, null))
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Init 인텐트 처리 시, 프로필 리스트를 업데이트한다`() = runTest {
        // when
        viewModel.onIntent(ProfileIntent.Init)
        advanceUntilIdle()

        // then
        val state = viewModel.profileState.first()
        assertEquals(0, state.currentPage)
        assertEquals(fakeImageItemList, state.itemList)
    }

    @Test
    fun `LastColumnLoaded 인텐트 처리 시, 프로필 리스트에 다음 페이지를 추가한다`() = runTest {
        // when
        viewModel.onIntent(ProfileIntent.Init)
        advanceUntilIdle()

        viewModel.onIntent(ProfileIntent.LastColumnLoaded)
        advanceUntilIdle()

        // then
        val state = viewModel.profileState.first()
        assertEquals(1, state.currentPage)
        assertEquals(10, state.itemList.size)
    }
}