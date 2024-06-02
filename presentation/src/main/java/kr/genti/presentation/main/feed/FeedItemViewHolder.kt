package kr.genti.presentation.main.feed

import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.presentation.databinding.ItemFeedItemBinding

class FeedItemViewHolder(
    val binding: ItemFeedItemBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: FeedItemModel) {
        with(binding) {
            ivFeedItemImage.load(item.url)
            tvFeedItemDescription.text = item.prompt
        }
    }
}
