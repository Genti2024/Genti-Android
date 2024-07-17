package kr.genti.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.domain.enums.GenerateStatus
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityMainBinding
import kr.genti.presentation.main.create.CreateFragment
import kr.genti.presentation.main.feed.FeedFragment
import kr.genti.presentation.main.profile.ProfileFragment
import kr.genti.presentation.result.finished.FinishedActivity
import kr.genti.presentation.result.waiting.WaitingActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initCreateBtnListener()
        setStatusBarColor()
        observeStatusResult()
    }

    fun initBnvItemIconTintList() {
        with(binding.bnvMain) {
            itemIconTintList = null
            selectedItemId = R.id.menu_feed
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main) ?: navigateTo<FeedFragment>()

        binding.bnvMain.setOnItemSelectedListener { menu ->
            if (binding.bnvMain.selectedItemId == menu.itemId) {
                return@setOnItemSelectedListener false
            }
            when (menu.itemId) {
                R.id.menu_feed -> navigateTo<FeedFragment>()

                R.id.menu_create -> navigateByGenerateStatus()

                R.id.menu_profile -> navigateTo<ProfileFragment>()

                else -> return@setOnItemSelectedListener false
            }
            binding.btnMenuCreate.isVisible = menu.itemId != R.id.menu_create
            true
        }
    }

    private fun initCreateBtnListener() {
        binding.btnMenuCreate.setOnClickListener {
            navigateByGenerateStatus()
            binding.bnvMain.selectedItemId = R.id.menu_create
        }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    private fun navigateByGenerateStatus() {
        when (viewModel.currentStatus) {
            GenerateStatus.COMPLETED -> navigateTo<CreateFragment>()

            GenerateStatus.AWAIT_USER_VERIFICATION -> {
                // TODO 다이얼로그 이후 이동
                if (viewModel.checkNerPictureInitialized()) {
                    FinishedActivity.createIntent(
                        this,
                        viewModel.newPicture.pictureGenerateRequestId,
                        viewModel.newPicture.pictureGenerateResponse.url,
                        viewModel.newPicture.pictureGenerateResponse.pictureRatio.name,
                    ).apply { startActivity(this) }
                } else {
                    toast(stringOf(R.string.error_msg))
                }
            }

            GenerateStatus.IN_PROGRESS -> {
                Intent(this, WaitingActivity::class.java).apply {
                    startActivity(this)
                }
            }

            GenerateStatus.ERROR -> {
                // TODO 다이얼로그
            }
        }
    }

    private fun observeStatusResult() {
        viewModel.getStatusResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) {
                toast(stringOf(R.string.error_msg))
            }
        }.launchIn(lifecycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }
}
