package kr.genti.generate

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.genti.common.extension.getFileName
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.ImageManager
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.usecase.generate.CheckPurchaseValidUseCase
import kr.genti.domain.usecase.generate.GetPromptExampleListUseCase
import kr.genti.domain.usecase.generate.GetThreeImageBucketUseCase
import kr.genti.domain.usecase.generate.SendGenerateRequestUseCase
import kr.genti.domain.usecase.upload.UploadImageToBucketUseCase
import kr.genti.generate.model.GenerateStage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getPromptExampleListUseCase: GetPromptExampleListUseCase = mockk(relaxed = true)
    private val getThreeImageBucketUseCase: GetThreeImageBucketUseCase = mockk(relaxed = true)
    private val uploadImageToBucketUseCase: UploadImageToBucketUseCase = mockk(relaxed = true)
    private val sendGenerateRequestUseCase: SendGenerateRequestUseCase = mockk(relaxed = true)
    private val checkPurchaseValidUseCase: CheckPurchaseValidUseCase = mockk(relaxed = true)

    private lateinit var viewModel: GenerateViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // mock use case를 통해 뷰모델 구성
        viewModel = GenerateViewModel(
            getPromptExampleListUseCase = getPromptExampleListUseCase,
            getThreeImageBucketUseCase = getThreeImageBucketUseCase,
            uploadImageToBucketUseCase = uploadImageToBucketUseCase,
            sendGenerateRequestUseCase = sendGenerateRequestUseCase,
            checkPurchaseValidUseCase = checkPurchaseValidUseCase
        )
        // AmplitudeManager 객체를 모킹하고, 기능을 stub 처리
        mockkObject(AmplitudeManager)
        every { AmplitudeManager.trackEvent(any()) } just Runs
        every { AmplitudeManager.trackEvent(any(), any()) } just Runs
        every { AmplitudeManager.trackEvent(any(), any(), any()) } just Runs
        every { AmplitudeManager.plusIntProperties(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Init 인텐트 처리 시, 상태가 올바르게 초기화되어야 한다`() = runTest {
        // given
        val exampleList =
            listOf(PromptExampleModel("url1", "prompt1"), PromptExampleModel("url2", "prompt2"))
        coEvery { getPromptExampleListUseCase(any()) } returns Result.success(exampleList)

        // when
        viewModel.onIntent(GenerateIntent.Init(isParentPic = true))

        // then
        val state = viewModel.generateState.first()
        assertEquals(1, state.currentStep)
        assertEquals(GenerateStage.NUMBER_SELECT, state.currentStage)
        assertEquals(3, state.exampleList.size)
    }

    @Test
    fun `PromptChange 인텐트 처리 시, 상태의 프롬프트가 변경되어야 한다`() =
        runTest {
            // given
            val newPrompt = "새로운 프롬프트"

            // when
            viewModel.onIntent(GenerateIntent.PromptChange(newPrompt))

            // then
            val state = viewModel.generateState.first()
            assertEquals(newPrompt, state.prompt)
        }

    @Test
    fun `ImageSelectBtnClick 인텐트 처리 시, isSelectingExtra 상태 변경 및 StartImageSelect side effect 발생해야 한다`() =
        runTest {
            // when
            viewModel.onIntent(GenerateIntent.ImageSelectBtnClick(isExtra = true))

            // then
            val state = viewModel.generateState.first()
            assertTrue(state.isSelectingExtra)

            val sideEffect = viewModel.generateSideEffect.firstOrNull()
            assertEquals(GenerateSideEffect.StartImageSelect, sideEffect)
        }

    @Nested
    @DisplayName("ImageSelect 인텐트 처리 시")
    inner class ImageSelectIntentTest {

        private lateinit var uriList: List<Uri>
        private val testResolver: ContentResolver = mockk(relaxed = true)

        @BeforeEach
        fun setUpImageSelect() {
            // content resolver 모킹
            val mockContext = mockk<Context>(relaxed = true)
            every { mockContext.contentResolver } returns testResolver
            ImageManager.init(mockContext)

            // Uri 모킹
            val mockUri = mockk<Uri>(relaxed = true)
            every { mockUri.hashCode() } returns 1
            every { mockUri.getFileName(testResolver).toString() } returns "image.jpg"
            every { mockUri.toString() } returns "content://dummy/1"
            uriList = listOf(mockUri, mockUri, mockUri)
        }

        @Test
        fun `isSelectingExtra가 false일 때 imageList가 업데이트되어야 한다`() =
            runTest {
                // when
                viewModel.onIntent(GenerateIntent.ImageSelect(uriList))

                // then
                val state = viewModel.generateState.first()
                assertEquals(3, state.imageList.size)
                assertTrue(state.extraImageList.isEmpty())
            }

        @Test
        fun `isSelectingExtra가 true일 때 extraImageList가 업데이트되어야 한다`() =
            runTest {
                // when
                viewModel.onIntent(GenerateIntent.ImageSelectBtnClick(isExtra = true))
                viewModel.onIntent(GenerateIntent.ImageSelect(uriList))

                // then
                val state = viewModel.generateState.first()
                assertEquals(3, state.extraImageList.size)
                assertTrue(state.imageList.isEmpty())
            }
    }

    @Nested
    @DisplayName("GenerateStage가 RESULT인 경우")
    inner class NextBtnIntentTest {

        private val fakeImageFileList = listOf(
            ImageFileModel(1, "image1", "url1"),
            ImageFileModel(2, "image2", "url2"),
            ImageFileModel(3, "image3", "url3")
        )

        private val fakeImageBucketList = listOf(
            ImageBucketModel(s3Key = "key1", fileName = "image1", presignedUrl = "url1"),
            ImageBucketModel(s3Key = "key2", fileName = "image2", presignedUrl = "url2"),
            ImageBucketModel(s3Key = "key3", fileName = "image3", presignedUrl = "url3")
        )

        @BeforeEach
        fun setUpNextStage() {
            viewModel.setStageForTest(GenerateStage.RESULT)
            viewModel.setImageFileModelListForTest(fakeImageFileList)

            // sendGenerateRequestUseCase 모킹
            coEvery {
                sendGenerateRequestUseCase(
                    prompt = any(),
                    pictureRatio = any(),
                    isParentPic = any(),
                    imageKeyList = any()
                )
            } returns Result.success(true)

            // getThreeImageBucketUseCase 모킹
            coEvery {
                getThreeImageBucketUseCase(
                    selectedImageList = any()
                )
            } returns Result.success(fakeImageBucketList)

            // uploadImageToBucketUseCase 모킹
            coEvery {
                uploadImageToBucketUseCase(
                    bucketUrl = any(),
                    imageUrl = any()
                )
            } returns Result.success(Unit)
        }

        @Test
        fun `NextButton 인텐트 처리 시, 이미지 업로드 로직 및 NavigateToWaiting side effect가 발생해야 한다`() =
            runTest {
                // when
                viewModel.onIntent(GenerateIntent.NextBtnClick)

                // then
                val sideEffect = viewModel.generateSideEffect.first()
                assertEquals(GenerateSideEffect.NavigateToWaiting(false), sideEffect)
            }

        @Test
        fun `extraImageList가 빈 리스트가 아닐 시, item이 6개인 KeyList를 반환해야 한다`() =
            runTest {
                // given
                viewModel.setExtraImageFileModelListForTest(fakeImageFileList)

                // when
                val keyList = viewModel.getUploadedKeyList()

                // then
                assertEquals(6, keyList.size)
            }
    }
}