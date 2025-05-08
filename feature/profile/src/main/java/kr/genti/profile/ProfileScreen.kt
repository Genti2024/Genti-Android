package kr.genti.profile

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import kr.genti.common.extension.toast
import kr.genti.common.manager.ImageManager.getImageChooserIntent
import kr.genti.common.manager.LauncherManager.rememberPermissionLauncher
import kr.genti.common.manager.PermissionManager
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiImageDetailDialog
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.event.LocalSnackBarTrigger
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.profile.component.ProfileEmptyScreen
import kr.genti.profile.component.ProfileGenerationBanner
import kr.genti.profile.component.ProfileImagesGridScreen
import kr.genti.profile.component.ProfileTopBar

@Composable
internal fun ProfileRoute(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToGenerate: () -> Unit = {},
    navigateToSetting: () -> Unit = {}
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val showSnackBar = LocalSnackBarTrigger.current

    val writePermissionLauncher = rememberPermissionLauncher {
        viewModel.onIntent(ProfileIntent.SaveBtnClick)
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileIntent.Init)
    }

    LaunchedEffect(viewModel.profileSideEffect, lifecycleOwner) {
        viewModel.profileSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProfileSideEffect.ShowDownloadToast -> context.toast(context.getString(R.string.profile_image_download_success))
                is ProfileSideEffect.ShowErrorToast -> showSnackBar(R.string.error_msg)
                is ProfileSideEffect.NavigateToGenerate -> navigateToGenerate()
                is ProfileSideEffect.NavigateToSetting -> navigateToSetting()

                is ProfileSideEffect.StartPermissionLauncher -> {
                    PermissionManager.checkPermissionAndLaunch(
                        permission = WRITE_EXTERNAL_STORAGE,
                        context = context,
                        onPermissionGranted = { viewModel.onIntent(ProfileIntent.SaveBtnClick) },
                        onPermissionNotGranted = {
                            writePermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
                        },
                        onPermissionAlreadyDenied = { intentToSetting ->
                            context.toast(context.getString(R.string.permission_to_setting))
                            context.startActivity(intentToSetting)
                        }
                    )
                }

                is ProfileSideEffect.NavigateToShare -> {
                    context.startActivity(getImageChooserIntent(sideEffect.imageUri))
                }
            }
        }
    }

    ProfileScreen(
        paddingValues = paddingValues,
        itemList = profileState.itemList,
        isLoading = profileState.isLoading,
        isGenerating = profileState.isGenerating,
        onImageItemClick = { viewModel.onIntent(ProfileIntent.ImageItemClick(it)) },
        onGenerateBtnClick = { viewModel.onIntent(ProfileIntent.GenerateBtnClick) },
        onSettingBtnClick = { viewModel.onIntent(ProfileIntent.SettingBtnClick) },
        onLastColumnLoaded = { viewModel.onIntent(ProfileIntent.LastColumnLoaded) }
    )

    if (profileState.isDetailDialogShown) {
        GentiImageDetailDialog(
            imageUrl = profileState.detailImageUrl,
            isGaro = profileState.isDetailImageGaro,
            isOneButton = false,
            onSaveBtnClick = { viewModel.onIntent(ProfileIntent.SaveBtnClick) },
            onShareBtnClick = { viewModel.onIntent(ProfileIntent.ShareBtnClick) },
            onDismissRequest = { viewModel.onIntent(ProfileIntent.DialogDismiss) }
        )
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    itemList: ImmutableList<ImageModel> = persistentListOf(),
    isLoading: Boolean = false,
    isGenerating: Boolean = false,
    onImageItemClick: (ImageModel) -> Unit = {},
    onGenerateBtnClick: () -> Unit = {},
    onSettingBtnClick: () -> Unit = {},
    onLastColumnLoaded: () -> Unit = {}
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
                .statusBarsPadding()
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
                ProfileImagesGridScreen(
                    itemList = itemList,
                    isGenerating = isGenerating,
                    onImageItemClick = onImageItemClick,
                    onGenerateBtnClick = onGenerateBtnClick,
                    onLastColumnLoaded = onLastColumnLoaded
                )
            }
        }

        GentiLoadingScreen(
            isLoading = isLoading,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
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

@Preview
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

@Preview
@Composable
private fun ProfileScreenPreviewThree() {
    GentiTheme {
        ProfileScreen(
            isGenerating = true,
            itemList = persistentListOf()
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreviewFour() {
    GentiTheme {
        ProfileScreen(
            isGenerating = false,
            itemList = persistentListOf()
        )
    }
}