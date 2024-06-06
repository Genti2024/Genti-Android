package kr.genti.presentation.main.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.genti.core.util.ItemDiffCallback
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ItemFeedInfoBinding
import kr.genti.presentation.databinding.ItemFeedItemBinding

class FeedAdapter(
    private val genBtnClick: (Unit) -> (Unit),
) : ListAdapter<FeedItemModel, RecyclerView.ViewHolder>(diffUtil) {
    private var itemList = mutableListOf<FeedItemModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }

        return when (viewType) {
            VIEW_TYPE_GEN_INFO ->
                FeedInfoViewHolder(
                    ItemFeedInfoBinding.inflate(inflater, parent, false),
                    genBtnClick,
                )

            VIEW_TYPE_ITEM ->
                FeedItemViewHolder(
                    ItemFeedItemBinding.inflate(inflater, parent, false),
                )

            else -> throw ClassCastException(
                parent.context.getString(
                    R.string.view_type_error_msg,
                    viewType,
                ),
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is FeedInfoViewHolder -> {
                holder.onBind()
            }

            is FeedItemViewHolder -> {
                val itemPosition = position - HEADER_COUNT
                holder.onBind(itemList[itemPosition])
            }
        }
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = if (position == itemList.size) 24 else 0
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount() = itemList.size + HEADER_COUNT

    override fun getItemViewType(position: Int) =
        when (position) {
            0 -> VIEW_TYPE_GEN_INFO
            else -> VIEW_TYPE_ITEM
        }

    fun addItemList(newItems: List<FeedItemModel>) {
        this.itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<FeedItemModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (this.itemList.isNotEmpty()) {
            this.itemList.removeAt(position)
            notifyItemRemoved(position + HEADER_COUNT)
            notifyItemRangeChanged(position + HEADER_COUNT, itemCount)
        }
    }

    companion object {
        private val diffUtil =
            ItemDiffCallback<FeedItemModel>(
                onItemsTheSame = { old, new -> old.picture.id == new.picture.id },
                onContentsTheSame = { old, new -> old == new },
            )

        private const val HEADER_COUNT = 1

        private const val VIEW_TYPE_GEN_INFO = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}
