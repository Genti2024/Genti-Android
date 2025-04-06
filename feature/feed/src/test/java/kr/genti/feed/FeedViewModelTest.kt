package kr.genti.feed

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.usecase.feed.GetFeedItemListUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getFeedItemListUseCase: GetFeedItemListUseCase = mockk(relaxed = true)

    private lateinit var viewModel: FeedViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // mock use case를 통해 뷰모델 구성
        viewModel = FeedViewModel(getFeedItemListUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Init 인텐트 처리 시, 성공하면 피드의 itemList가 업데이트되어야 한다`() = runTest {
        // given
        val feedItems = listOf(
            FeedItemModel(ImageModel(1, "url1", PictureRatio.RATIO_GARO), "prompt1"),
            FeedItemModel(ImageModel(2, "url2", PictureRatio.RATIO_SERO), "prompt2")
        )
        coEvery { getFeedItemListUseCase() } returns Result.success(feedItems)

        // when
        viewModel.onIntent(FeedIntent.Init)
        advanceUntilIdle()

        // then
        val state = viewModel.feedState.first()
        assertEquals(feedItems.toImmutableList(), state.itemList)
    }
}