package kr.genti.presentation.main.feed

import androidx.recyclerview.widget.RecyclerView
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.databinding.ItemFeedInfoBinding

class FeedInfoViewHolder(
    val binding: ItemFeedInfoBinding,
    val genBtnClick: (Unit) -> (Unit),
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind() {
        binding.btnFeedGenfluencer.setOnSingleClickListener {
            genBtnClick
        }
    }
}
