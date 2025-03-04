package kr.genti.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun ProfileRoute(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    ProfileScreen(
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
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