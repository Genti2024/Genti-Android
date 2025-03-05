package kr.genti.feed

sealed class FeedSideEffect {
    data object ShowErrorToast : FeedSideEffect()
    data class NavigateToWebsite(val url: String) : FeedSideEffect()
}