package kr.genti.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

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
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Text("Profile")
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    GentiTheme {
        ProfileScreen()
    }
}