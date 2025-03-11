package kr.genti.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun GenerateSelectDialog(
    modifier: Modifier = Modifier,
    onBtnClick: (Boolean) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .noRippleClickable { onDismissRequest() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Black, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_photo_add),
                    contentDescription = null,
                )

                GenerateSelectItem(
                    titleRes = R.string.create_select_tv_default_title,
                    subtitleRes = R.string.create_select_tv_default_subtitle,
                    bodyRes = R.string.create_select_tv_default_body,
                    captionRes = R.string.create_select_tv_default_caption,
                    onClick = { onBtnClick(false) },
                    modifier = Modifier.offset(y = (-16).dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                GenerateSelectItem(
                    titleRes = R.string.create_select_tv_parent_title,
                    subtitleRes = R.string.create_select_tv_parent_subtitle,
                    bodyRes = R.string.create_select_tv_parent_body,
                    captionRes = R.string.create_select_tv_parent_caption,
                    onClick = { onBtnClick(true) },
                    modifier = Modifier.offset(y = (-16).dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenerateSelectDialogPreview() {
    GentiTheme {
        GenerateSelectDialog()
    }
}