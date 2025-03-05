package kr.genti.feed

sealed class FeedIntent {
    data object Init: FeedIntent()
    data object InfoBtnClick: FeedIntent()
    data object TooltipClick: FeedIntent()
    data object ListScroll: FeedIntent()
    data object BottomSheetDismiss: FeedIntent()
    data object MoreBtnClick: FeedIntent()
}