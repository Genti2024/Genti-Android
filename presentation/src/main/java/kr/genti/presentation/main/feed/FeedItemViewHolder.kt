package kr.genti.presentation.main.feed

import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.presentation.databinding.ItemFeedItemBinding

class FeedItemViewHolder(
    val binding: ItemFeedItemBinding,
    private val checkLastImageLoadFinished: (Int) -> (Unit),
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        item: FeedItemModel,
        position: Int,
    ) {
        with(binding) {
            ivFeedItemImage.load(item.picture.url) {
                listener(
                    onSuccess = { _, _ ->
                        checkLastImageLoadFinished(position)
                    },
                )
            }
            tvFeedItemDescription.text = item.prompt
        }
    }
}
