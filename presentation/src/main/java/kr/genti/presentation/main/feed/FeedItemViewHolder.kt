package kr.genti.presentation.main.feed

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.presentation.databinding.ItemFeedItemBinding

class FeedItemViewHolder(
    val binding: ItemFeedItemBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: FeedItemModel) {
        with(binding) {
            ivFeedItemImage.setImageURI(item.url.toUri())
            tvFeedItemDescription.text = item.prompt
        }
    }
}
