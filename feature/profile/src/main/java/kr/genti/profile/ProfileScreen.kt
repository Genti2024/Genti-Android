package kr.genti.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.profile.component.ProfileEmptyScreen
import kr.genti.profile.component.ProfileGenerateItem
import kr.genti.profile.component.ProfileGenerationBanner
import kr.genti.profile.component.ProfileItem
import kr.genti.profile.component.ProfileTopBar

@Composable
internal fun ProfileRoute(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToGenerate: () -> Unit = {},
    navigateToSetting: () -> Unit = {}
) {
    val feedState by viewModel.profileState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileIntent.Init)
    }

    LaunchedEffect(viewModel.profileSideEffect, lifecycleOwner) {
        viewModel.profileSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProfileSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is ProfileSideEffect.NavigateToGenerate -> navigateToGenerate()
                is ProfileSideEffect.NavigateToSetting -> navigateToSetting()
            }
        }
    }

    ProfileScreen(
        paddingValues = paddingValues,
        itemList = feedState.itemList,
        isLoading = feedState.isLoading,
        isGenerating = feedState.isGenerating,
        onGenerateBtnClick = { viewModel.onIntent(ProfileIntent.GenerateBtnClick) },
        onSettingBtnClick = { viewModel.onIntent(ProfileIntent.SettingBtnClick) }
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    itemList: ImmutableList<ImageModel> = persistentListOf(),
    isLoading: Boolean = false,
    isGenerating: Boolean = false,
    onGenerateBtnClick: () -> Unit = {},
    onSettingBtnClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(bottom = paddingValues.calculateBottomPadding()),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            ProfileTopBar(
                onSettingBtnClick = onSettingBtnClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileGenerationBanner(
                isGenerating = isGenerating
            )

            if (itemList.isEmpty()) {
                ProfileEmptyScreen()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    itemsIndexed(
                        items = itemList,
                        key = { _, model -> model.id }
                    ) { _, item ->
                        ProfileItem(item = item)
                    }
                    item {
                        ProfileGenerateItem(
                            isGenerating = isGenerating,
                            onBtnClick = onGenerateBtnClick
                        )
                    }
                }
            }
        }

        GentiLoadingScreen(
            isLoading = isLoading,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview()
@Composable
private fun ProfileScreenPreviewOne() {
    GentiTheme {
        ProfileScreen(
            isGenerating = true,
            itemList = persistentListOf(
                ImageModel(
                    id = 0,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_GARO
                ),
                ImageModel(
                    id = 1,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_SERO
                ),
                ImageModel(
                    id = 2,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_GARO
                ),
                ImageModel(
                    id = 3,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_SERO
                ),
                ImageModel(
                    id = 4,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_GARO
                ),
            )
        )
    }
}

@Preview()
@Composable
private fun ProfileScreenPreviewTwo() {
    GentiTheme {
        ProfileScreen(
            isGenerating = false,
            itemList = persistentListOf(
                ImageModel(
                    id = 0,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_GARO
                ),
                ImageModel(
                    id = 1,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_SERO
                ),
                ImageModel(
                    id = 2,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_GARO
                ),
                ImageModel(
                    id = 3,
                    url = "",
                    pictureRatio = PictureRatio.RATIO_SERO
                ),
            )
        )
    }
}

@Preview()
@Composable
private fun ProfileScreenPreviewThree() {
    GentiTheme {
        ProfileScreen(
            isGenerating = true,
            itemList = persistentListOf()
        )
    }
}

@Preview()
@Composable
private fun ProfileScreenPreviewFour() {
    GentiTheme {
        ProfileScreen(
            isGenerating = false,
            itemList = persistentListOf()
        )
    }
}