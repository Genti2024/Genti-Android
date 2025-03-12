package kr.genti.generate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun ImageThreeExample(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.selfie_tv_input_title),
            style = GentiTheme.typography.body1,
            color = GentiGreen,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Image(
                painter = painterResource(
                    id = if (isParentPic) R.drawable.img_parent_ex_1 else R.drawable.img_selfie_one
                ),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(
                    id = if (isParentPic) R.drawable.img_parent_ex_2 else R.drawable.img_selfie_two
                ),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(
                    id = if (isParentPic) R.drawable.img_parent_ex_3 else R.drawable.img_selfie_three
                ),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun ImageThreeExamplePreview() {
    GentiTheme {
        ImageThreeExample()
    }
}
