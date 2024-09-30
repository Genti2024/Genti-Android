package kr.genti.presentation.main

import android.content.Context
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
import kr.genti.core.state.UiState
import kr.genti.domain.enums.GenerateStatus
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityMainBinding
import kr.genti.presentation.generate.finished.FinishedActivity
import kr.genti.presentation.generate.openchat.OpenchatActivity
import kr.genti.presentation.generate.verify.VerifyActivity
import kr.genti.presentation.generate.waiting.WaitingActivity
import kr.genti.presentation.main.create.CreateFragment
import kr.genti.presentation.main.feed.FeedFragment
import kr.genti.presentation.main.profile.ProfileFragment
import kr.genti.presentation.util.AmplitudeManager

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
        getNotificationIntent()
        observeStatusResult()
        observeNotificationState()
        observeResetResult()
        observeUserVerifyState()
    }

    override fun onResume() {
        super.onResume()

        with(viewModel) {
            getGenerateStatusFromServer(false)
            if (isUserTryingVerify) getIsUserVerifiedFromServer()
        }
    }

    fun initBnvItemIconTintList() {
        with(binding.bnvMain) {
            itemIconTintList = null
            selectedItemId = R.id.menu_feed
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main) ?: navigateTo<FeedFragment>(null)

        binding.bnvMain.setOnItemSelectedListener { menu ->
            if (binding.bnvMain.selectedItemId == menu.itemId) {
                return@setOnItemSelectedListener false
            }
            when (menu.itemId) {
                R.id.menu_feed -> navigateTo<FeedFragment>("click_maintab")

                R.id.menu_create -> navigateTo<CreateFragment>("click_createpictab")

                R.id.menu_profile -> navigateTo<ProfileFragment>("click_mypagetab")

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

    private fun getNotificationIntent() {
        when (intent.getStringExtra(EXTRA_TYPE)) {
            TYPE_SUCCESS -> viewModel.getGenerateStatusFromServer(true)
            TYPE_CANCELED -> viewModel.getGenerateStatusFromServer(true)
            TYPE_OPENCHAT -> startActivity(Intent(this, OpenchatActivity::class.java))
            else -> return
        }
    }

    private fun navigateByGenerateStatus() {
        when (viewModel.currentStatus) {
            GenerateStatus.NEW_REQUEST_AVAILABLE -> {
                viewModel.getIsUserVerifiedFromServer()
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
                createErrorDialog = CreateErrorDialog()
                createErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
            }

            GenerateStatus.EMPTY -> return
        }
    }

    private fun observeStatusResult() {
        viewModel.getStatusResult
            .flowWithLifecycle(lifecycle)
            .onEach { result ->
                if (!result) toast(stringOf(R.string.error_msg))
            }.launchIn(lifecycleScope)
    }

    private fun observeNotificationState() {
        viewModel.notificationState
            .flowWithLifecycle(lifecycle)
            .onEach { status ->
                when (status) {
                    GenerateStatus.AWAIT_USER_VERIFICATION -> {
                        if (viewModel.checkNewPictureInitialized()) {
                            AmplitudeManager.trackEvent(
                                "click_push_notification",
                                mapOf("push_type" to "creating_success"),
                            )
                            with(viewModel.newPicture.pictureGenerateResponse) {
                                FinishedActivity
                                    .createIntent(
                                        this@MainActivity,
                                        this?.pictureGenerateResponseId ?: -1,
                                        this?.pictureCompleted?.url.orEmpty(),
                                        this
                                            ?.pictureCompleted
                                            ?.pictureRatio
                                            ?.name
                                            .orEmpty(),
                                    ).apply { startActivity(this) }
                            }
                        } else {
                            toast(stringOf(R.string.error_msg))
                        }
                    }

                    GenerateStatus.CANCELED -> {
                        AmplitudeManager.trackEvent(
                            "click_push_notification",
                            mapOf("push_type" to "creating_fail"),
                        )
                        createErrorDialog = CreateErrorDialog()
                        createErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
                    }

                    else -> return@onEach
                }
                viewModel.resetNotificationState()
            }.launchIn(lifecycleScope)
    }

    private fun observeResetResult() {
        viewModel.postResetResult
            .flowWithLifecycle(lifecycle)
            .onEach { result ->
                if (!result) {
                    toast(stringOf(R.string.error_msg))
                } else {
                    binding.bnvMain.selectedItemId = R.id.menu_create
                }
            }.launchIn(lifecycleScope)
    }

    private fun observeUserVerifyState() {
        viewModel.userVerifyState
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        if (!viewModel.isUserTryingVerify) {
                            if (state.data) {
                                binding.bnvMain.selectedItemId = R.id.menu_create
                            } else {
                                viewModel.isUserTryingVerify = true
                                startActivity(Intent(this, VerifyActivity::class.java))
                            }
                        } else {
                            viewModel.isUserTryingVerify = false
                            if (state.data) {
                                binding.bnvMain.selectedItemId = R.id.menu_create
                            }
                        }
                    }

                    is UiState.Failure -> toast(stringOf(R.string.error_msg))
                    else -> return@onEach
                }
                viewModel.resetIsUserVerified()
            }.launchIn(lifecycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo(page: String?) {
        if (page != null) AmplitudeManager.trackEvent(page)
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

        const val TYPE_SUCCESS = "SUCCESS"
        const val TYPE_CANCELED = "CANCELED"
        const val TYPE_OPENCHAT = "OPENCHAT"

        private const val EXTRA_TYPE = "EXTRA_DEFAULT"

        @JvmStatic
        fun getIntent(
            context: Context,
            type: String? = null,
        ) = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_TYPE, type)
        }
    }
}
