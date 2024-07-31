package kr.genti.presentation.auth.onboarding

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.genti.presentation.databinding.ItemOnboardingBinding

class OnboardingViewHolder(
    val binding: ItemOnboardingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(position: Int) {
        with(binding) {
            layoutOnboardingFirst.isVisible = position == 0
            layoutOnboardingSecond.isVisible = position != 0
        }
    }
}
