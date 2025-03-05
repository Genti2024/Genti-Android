package kr.genti.designsystem.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiGradationButton
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White10
import kr.genti.designsystem.theme.White80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GentiBottomSheet(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    @StringRes btnRes: Int,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onBtnClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        containerColor = Gray,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onDismissRequest()
        },
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_drag_handle),
                    contentDescription = null,
                    tint = White10,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(titleRes),
                style = GentiTheme.typography.title,
                textAlign = TextAlign.Center,
                color = White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(subtitleRes),
                style = GentiTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = White80
            )

            GentiGradationButton(
                textRes = btnRes,
                onClick = {
                    scope.launch { sheetState.hide() }
                    onBtnClick()
                },
                modifier = Modifier.padding(vertical = 33.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GentiBottomSheetPreview() {
    GentiTheme {
        GentiBottomSheet(
            titleRes = R.string.feed_info_tv_title,
            subtitleRes = R.string.feed_info_tv_subtitle,
            btnRes = R.string.feed_info_btn_more,
            sheetState = SheetState(
                initialValue = SheetValue.Expanded,
                skipPartiallyExpanded = true,
                density = Density(1f)
            )
        )
    }
}