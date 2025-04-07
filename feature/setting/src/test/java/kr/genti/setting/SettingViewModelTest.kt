package kr.genti.setting

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.genti.domain.usecase.setting.DeleteUserUseCase
import kr.genti.domain.usecase.setting.LogoutUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val logoutUserUseCase: LogoutUserUseCase = mockk(relaxed = true)
    private val deleteUserUseCase: DeleteUserUseCase = mockk(relaxed = true)

    private lateinit var viewModel: SettingViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // mock use case를 통해 뷰모델 구성
        viewModel = SettingViewModel(logoutUserUseCase, deleteUserUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `logout 요청 성공 시, RestartApp side effect가 발생해야 한다`() =
        runTest {
            // given
            coEvery { logoutUserUseCase() } returns Result.success(true)
            val sideEffectDeferred = async { viewModel.settingSideEffect.first() }

            // when
            viewModel.onIntent(SettingIntent.LogoutRequest)

            // then
            val sideEffect = sideEffectDeferred.await()
            assertEquals(SettingSideEffect.RestartApp, sideEffect)
        }

    @Test
    fun `quit 요청 성공 시, RestartApp side effect가 발생해야 한다`() =
        runTest {
            // given
            coEvery { deleteUserUseCase() } returns Result.success(true)
            val sideEffectDeferred = async { viewModel.settingSideEffect.first() }

            // when
            viewModel.onIntent(SettingIntent.QuitRequest)

            // then
            val sideEffect = sideEffectDeferred.await()
            assertEquals(SettingSideEffect.RestartApp, sideEffect)
        }
}