package kr.genti.feed

sealed class FeedSideEffect {
    data object ShowErrorToast : FeedSideEffect()
}