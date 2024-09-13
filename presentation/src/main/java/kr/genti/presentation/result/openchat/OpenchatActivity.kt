package kr.genti.presentation.result.openchat

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.core.state.UiState
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityOpenchatBinding

@AndroidEntryPoint
class OpenchatActivity : BaseActivity<ActivityOpenchatBinding>(R.layout.activity_openchat) {
    private val viewModel by viewModels<OpenchatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initExitBtnListener()
        initAccessAgainBtnListener()
        setTitleTextGradation()
        observeGetOpenchatState()
    }

    private fun initExitBtnListener() {
        // TODO
        binding.btnExit.setOnSingleClickListener { }
    }

    private fun initAccessAgainBtnListener() {
        binding.btnAccessAgain.setOnSingleClickListener {
            if (viewModel.isAccessible) {
                viewModel.isAccessible = false
                binding.ivAccessAgain.load(R.drawable.ic_check_checked)
                binding.tvAccessAgain.setTextColor(Color.parseColor("#0D2D2B"))
            } else {
                viewModel.isAccessible = true
                binding.ivAccessAgain.load(R.drawable.ic_check_unchecked)
                binding.tvAccessAgain.setTextColor(Color.parseColor("#990D2D2B"))
            }
        }
    }

    private fun setTitleTextGradation() {
        // TODO
    }

    private fun observeGetOpenchatState() {
        viewModel.getOpenchatState.flowWithLifecycle(lifecycle).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        with(state.data) {
                            if (!accessible) {
                                // TODO
                            }
                            url?.let { initEnterBtnListener(it) }
                            count?.let { setGuideTextInfo(it) }
                        }
                    }

                    is UiState.Failure -> toast(stringOf(R.string.error_msg))
                    else -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    private fun initEnterBtnListener(url: String) {
        binding.btnEnterOpenchat.setOnSingleClickListener {
            // TODO
            viewModel.setIsChatAccessible()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun setGuideTextInfo(count: Int) {
        // TODO
    }
}
