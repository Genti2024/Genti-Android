package kr.genti.presentation.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kr.genti.core.util.ItemDiffCallback
import kr.genti.domain.entity.response.ImageModel
import kr.genti.presentation.databinding.ItemProfileImageBinding

class ProfileAdapter(
    private val imageClick: (ImageModel) -> Unit,
) : ListAdapter<ImageModel, ProfileViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfileViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemProfileImageBinding =
            ItemProfileImageBinding.inflate(inflater, parent, false)
        return ProfileViewHolder(binding, imageClick)
    }

    override fun onBindViewHolder(
        holder: ProfileViewHolder,
        position: Int,
    ) {
        val item = getItem(position) ?: return
        holder.onBind(item)
    }

    fun addList(newItems: List<ImageModel>) {
        val currentItems = currentList.toMutableList()
        currentItems.addAll(newItems)
        submitList(currentItems)
    }

    companion object {
        private val diffUtil =
            ItemDiffCallback<ImageModel>(
                onItemsTheSame = { old, new -> old.id == new.id },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
