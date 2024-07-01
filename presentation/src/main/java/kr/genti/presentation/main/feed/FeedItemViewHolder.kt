package kr.genti.presentation.main.feed

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.enums.PictureRatio
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
            tvFeedItemDescription.text = item.prompt

            if (item.picture.pictureRatio?.name == PictureRatio.RATIO_3_2.name) {
                (cvFeedItemImage.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "3:2"
            } else {
                (cvFeedItemImage.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "2:3"
            }

            ivFeedItemImage.load(item.picture.url) {
                listener(
                    onSuccess = { _, _ ->
                        checkLastImageLoadFinished(position)
                    },
                )
            }
        }
    }
}
