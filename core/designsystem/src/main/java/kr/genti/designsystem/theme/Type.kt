package kr.genti.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kr.genti.core.designsystem.R

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))
val PretendardLight = FontFamily(Font(R.font.pretendard_light, FontWeight.Light))

@Stable
class StempoTypography internal constructor(
    title: TextStyle,
    subtitle1: TextStyle,
    subtitle2: TextStyle,
    body1: TextStyle,
    body2: TextStyle,
    caption1: TextStyle,
    caption2: TextStyle,
) {
    var title: TextStyle by mutableStateOf(title)
        private set
    var subtitle1: TextStyle by mutableStateOf(subtitle1)
        private set
    var subtitle2: TextStyle by mutableStateOf(subtitle2)
        private set
    var body1: TextStyle by mutableStateOf(body1)
        private set
    var body2: TextStyle by mutableStateOf(body2)
        private set
    var caption1: TextStyle by mutableStateOf(caption1)
        private set
    var caption2: TextStyle by mutableStateOf(caption2)
        private set

    fun copy(
        title: TextStyle = this.title,
        subtitle1: TextStyle = this.subtitle1,
        subtitle2: TextStyle = this.subtitle2,
        body1: TextStyle = this.body1,
        body2: TextStyle = this.body2,
        caption1: TextStyle = this.caption1,
        caption2: TextStyle = this.caption2,
    ): StempoTypography = StempoTypography(
        title,
        subtitle1,
        subtitle2,
        body1,
        body2,
        caption1,
        caption2
    )

    fun update(other: StempoTypography) {
        title = other.title
        subtitle1 = other.subtitle1
        subtitle2 = other.subtitle2
        body1 = other.body1
        body2 = other.body2
        caption1 = other.caption1
        caption2 = other.caption2
    }
}

fun stempoTextStyle(
    fontFamily: FontFamily,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit = 0.sp,
): TextStyle = TextStyle(
    fontFamily = fontFamily,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

@Composable
fun stempoTypography(): StempoTypography {
    return StempoTypography(
        title = stempoTextStyle(
            fontFamily = PretendardBold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        subtitle1 = stempoTextStyle(
            fontFamily = PretendardBold,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        subtitle2 = stempoTextStyle(
            fontFamily = PretendardBold,
            fontSize = 16.sp,
            lineHeight = 20.sp
        ),
        body1 = stempoTextStyle(
            fontFamily = PretendardBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        body2 = stempoTextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        caption1 = stempoTextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        caption2 = stempoTextStyle(
            fontFamily = PretendardRegular,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    )
}