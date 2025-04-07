package kr.genti.main

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.GenerateStatusModel.GenerateResponseModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.entity.response.ServerAvailableModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.usecase.generate.CheckServerAvailableUseCase
import kr.genti.domain.usecase.result.ForceGenerateInDebugUseCase
import kr.genti.domain.usecase.result.GetCurrentGenerateStatusUseCase
import kr.genti.domain.usecase.result.ResetGenerateStatusUseCase
import kr.genti.domain.usecase.verify.CheckUserVerifiedUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val checkUserVerifiedUseCase: CheckUserVerifiedUseCase = mockk(relaxed = true)
    private val checkServerAvailableUseCase: CheckServerAvailableUseCase = mockk(relaxed = true)
    private val getCurrentGenerateStatusUseCase: GetCurrentGenerateStatusUseCase = mockk(relaxed = true)
    private val resetGenerateStatusUseCase: ResetGenerateStatusUseCase = mockk(relaxed = true)
    private val forceGenerateInDebugUseCase: ForceGenerateInDebugUseCase = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // mock use case를 통해 뷰모델 구성
        viewModel = MainViewModel(
            checkUserVerifiedUseCase,
            checkServerAvailableUseCase,
            getCurrentGenerateStatusUseCase,
            resetGenerateStatusUseCase,
            forceGenerateInDebugUseCase
        )
        // AmplitudeManager 객체를 모킹하고, 기능을 stub 처리
        mockkObject(AmplitudeManager)
        every { AmplitudeManager.trackEvent(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Nested
    @DisplayName("GenerateBtnClick 인텐트 처리 시")
    inner class GenerateBtnClickIntentTest {

        private fun makeFakeResponse(status: GenerateStatus) = GenerateStatusModel(
            requestId = 1,
            status = status,
            response = GenerateResponseModel(
                responseId = 1,
                picture = ImageModel(1, "url1", PictureRatio.RATIO_GARO),
            ),
            paid = false
        )

        @Nested
        @DisplayName("NEW_REQUEST_AVAILABLE 경우")
        inner class NewRequestAvailableTest {

            @BeforeEach
            fun setUpNewRequestAvailable() {
                coEvery { getCurrentGenerateStatusUseCase() } returns Result.success(
                    makeFakeResponse(status = GenerateStatus.NEW_REQUEST_AVAILABLE)
                )
            }

            @Test
            fun `checkServerAvailableUseCase 결과가 false인 경우, isUnableDialogVisible이 True로 업데이트되어야 한다`() =
                runTest {
                    // given
                    coEvery { checkServerAvailableUseCase() } returns Result.success(
                        ServerAvailableModel(false, "Server is not available")
                    )

                    // when
                    viewModel.onIntent(MainIntent.GenerateBtnClick)
                    advanceUntilIdle()

                    // then
                    val state = viewModel.mainState.first()
                    assertTrue(state.isUnableDialogVisible)
                }

            @Test
            fun `checkUserVerifiedUseCase 결과가 false인 경우, NavigateToVerify side effect가 발생해야 한다`() =
                runTest {
                    // given
                    coEvery { checkServerAvailableUseCase() } returns Result.success(
                        ServerAvailableModel(true, null)
                    )
                    coEvery { checkUserVerifiedUseCase() } returns Result.success(false)
                    val sideEffectDeferred = async { viewModel.mainSideEffect.first() }

                    // when
                    viewModel.onIntent(MainIntent.GenerateBtnClick)
                    advanceUntilIdle()

                    // then
                    val sideEffect = sideEffectDeferred.await()
                    assertEquals(MainSideEffect.NavigateToVerify, sideEffect)
                }

            @Test
            fun `모든 조건 만족 시, isSelectDialogVisible이 True로 업데이트되어야 한다`() =
                runTest {
                    // given:
                    coEvery { checkServerAvailableUseCase() } returns Result.success(
                        ServerAvailableModel(true, null)
                    )
                    coEvery { checkUserVerifiedUseCase() } returns Result.success(true)

                    // when
                    viewModel.onIntent(MainIntent.GenerateBtnClick)
                    advanceUntilIdle()

                    // then
                    val state = viewModel.mainState.first()
                    assertTrue(state.isSelectDialogVisible)
                }
        }

        @Test
        fun `AWAIT_USER_VERIFICATION 경우, isFinishedDialogVisible이 True로 업데이트되어야 한다`() = runTest {
            // given
            coEvery { getCurrentGenerateStatusUseCase() } returns Result.success(
                makeFakeResponse(status = GenerateStatus.AWAIT_USER_VERIFICATION)
            )

            // when
            viewModel.onIntent(MainIntent.GenerateBtnClick)
            advanceUntilIdle()

            // then
            val state = viewModel.mainState.first()
            assertTrue(state.isFinishedDialogVisible)
        }

        @Test
        fun `IN_PROGRESS 경우, NavigateToWaiting side effect가 발생해야 한다`() = runTest {
            // given
            coEvery { getCurrentGenerateStatusUseCase() } returns Result.success(
                makeFakeResponse(status = GenerateStatus.IN_PROGRESS)
            )
            val sideEffectDeferred = async { viewModel.mainSideEffect.first() }

            // when
            viewModel.onIntent(MainIntent.GenerateBtnClick)
            advanceUntilIdle()

            // then
            val sideEffect = sideEffectDeferred.await()
            assertEquals(MainSideEffect.NavigateToWaiting(false), sideEffect)
        }

        @Test
        fun `CANCELED 경우, isErrorDialogVisible이 True로 업데이트되어야 한다`() = runTest {
            // given
            coEvery { getCurrentGenerateStatusUseCase() } returns Result.success(
                makeFakeResponse(status = GenerateStatus.CANCELED)
            )

            // when
            viewModel.onIntent(MainIntent.GenerateBtnClick)
            advanceUntilIdle()

            // then
            val state = viewModel.mainState.first()
            assertTrue(state.isErrorDialogVisible)
        }
    }
}