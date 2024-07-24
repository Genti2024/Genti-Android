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
import kr.genti.presentation.result.waiting.WaitingActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel>()

    private var createFinishedDialog: CreateFinishedDialog? = null
    private var createErrorDialog: CreateErrorDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initCreateBtnListener()
        setStatusBarColor()
        observeStatusResult()
        observeResetResult()
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

                R.id.menu_create -> navigateTo<CreateFragment>()

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
        }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    private fun navigateByGenerateStatus() {
        when (viewModel.currentStatus) {
            GenerateStatus.NEW_REQUEST_AVAILABLE -> {
                binding.bnvMain.selectedItemId = R.id.menu_create
            }

            GenerateStatus.AWAIT_USER_VERIFICATION -> {
                createFinishedDialog = CreateFinishedDialog()
                createFinishedDialog?.show(supportFragmentManager, DIALOG_FINISHED)
            }

            GenerateStatus.IN_PROGRESS -> {
                Intent(this, WaitingActivity::class.java).apply {
                    startActivity(this)
                }
            }

            GenerateStatus.CANCELED -> {
                viewModel.postResetStateToServer()
                createErrorDialog = CreateErrorDialog()
                createErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
            }
        }
    }

    private fun observeStatusResult() {
        viewModel.getStatusResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(stringOf(R.string.error_msg))
        }.launchIn(lifecycleScope)
    }

    private fun observeResetResult() {
        viewModel.postResetResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) {
                toast(stringOf(R.string.error_msg))
            } else {
                binding.bnvMain.selectedItemId = R.id.menu_create
            }
        }.launchIn(lifecycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        createFinishedDialog = null
        createErrorDialog = null
    }

    companion object {
        private const val DIALOG_FINISHED = "DIALOG_FINISHED"
        private const val DIALOG_ERROR = "DIALOG_ERROR"
    }
}
