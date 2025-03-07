package kr.genti.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGradationDarkEnd
import kr.genti.designsystem.theme.GentiGradationDarkStart
import kr.genti.designsystem.theme.GentiGradationEnd66
import kr.genti.designsystem.theme.GentiGradationStart
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White40

@Composable
fun GentiSelectButton(
    @DrawableRes iconRes: Int,
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isTintNeeded: Boolean = false,
    onBtnClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    var cardWidth by remember { mutableStateOf(0.dp) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onBtnClick() }
            .then(
                if (isSelected) Modifier
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            listOf(GentiGradationStart, GentiGradationEnd66)
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                else Modifier
            ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    cardWidth = with(density) { coordinates.size.width.toDp() }
                }
                .heightIn(min = cardWidth)
                .then(
                    if (isSelected) {
                        Modifier.background(
                            Brush.linearGradient(
                                listOf(GentiGradationDarkStart, GentiGradationDarkEnd)
                            )
                        )
                    } else {
                        Modifier.background(Gray)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    contentDescription = null,
                    tint = when {
                        !isTintNeeded -> Color.Unspecified
                        isSelected -> GentiGreen
                        else -> White40
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(textRes),
                    style = GentiTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = White,
                )
            }
        }
    }
}

@Preview(backgroundColor = 0x777777, showBackground = true)
@Composable
fun GentiSelectButtonsPreview() {
    GentiTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                GentiSelectButton(
                    iconRes = R.drawable.ic_male,
                    textRes = R.string.signup_tv_gender_male,
                    isSelected = false,
                    isTintNeeded = true,
                )
            }
            item {
                GentiSelectButton(
                    iconRes = R.drawable.ic_female,
                    textRes = R.string.signup_tv_gender_female,
                    isSelected = true,
                    isTintNeeded = true,
                )
            }
            item {
                GentiSelectButton(
                    iconRes = R.drawable.img_ratio_2_3,
                    textRes = R.string.signup_tv_gender_male,
                    isSelected = false,
                    isTintNeeded = false,
                )
            }
            item {
                GentiSelectButton(
                    iconRes = R.drawable.img_ratio_3_2,
                    textRes = R.string.signup_tv_gender_female,
                    isSelected = true,
                    isTintNeeded = false,
                )
            }
        }
    }
}