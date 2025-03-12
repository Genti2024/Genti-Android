package kr.genti.generate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.item.GentiPageIndicator
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.PromptExampleModel

@Composable
internal fun PromptExamplePager(
    modifier: Modifier = Modifier,
    exampleList: ImmutableList<PromptExampleModel> = persistentListOf(),
    onExamplePageSwipe: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { exampleList.size })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page > 0) onExamplePageSwipe()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val example = exampleList[page]
            PromptExampleItem(
                imageUrl = example.url,
                prompt = example.prompt,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        GentiPageIndicator(pagerState = pagerState)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun PromptExamplePagerPreview() {
    GentiTheme {
        PromptExamplePager(
            exampleList = persistentListOf(
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                ),
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                ),
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                )
            )
        )
    }
}