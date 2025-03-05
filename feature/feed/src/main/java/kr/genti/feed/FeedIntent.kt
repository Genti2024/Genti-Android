package kr.genti.feed

sealed class FeedIntent {
    data object Init: FeedIntent()
    data object InfoBtnClick: FeedIntent()
    data object TooltipClick: FeedIntent()
}