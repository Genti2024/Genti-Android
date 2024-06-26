package kr.genti.presentation.setting

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivitySettingBinding

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private var settingLogoutDialog: SettingLogoutDialog? = null
    private var settingQuitDialog: SettingQuitDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initBackBtnListener()
        initInfoBtnsListener()
        initLogoutBtnListener()
        initQuitBtnListener()
    }

    private fun initView() {
        setStatusBarColorFromResource(R.color.green_3)
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initInfoBtnsListener() {
        with(binding) {
            btnTermsOfService.setOnSingleClickListener {
                // TODO
            }
            btnPrivacyPolicy.setOnSingleClickListener {
                // TODO
            }
            btnAndroidVersion.setOnSingleClickListener {
                // TODO
            }
            btnCompanyInfo.setOnSingleClickListener {
                // TODO
            }
        }
    }

    private fun initLogoutBtnListener() {
        binding.btnLogout.setOnSingleClickListener {
            settingLogoutDialog = SettingLogoutDialog()
            settingLogoutDialog?.show(supportFragmentManager, DIALOG_LOGOUT)
        }
    }

    private fun initQuitBtnListener() {
        binding.btnQuit.setOnSingleClickListener {
            settingQuitDialog = SettingQuitDialog()
            settingQuitDialog?.show(supportFragmentManager, DIALOG_QUIT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        settingLogoutDialog = null
        settingQuitDialog = null
    }

    companion object {
        private const val DIALOG_LOGOUT = "DIALOG_LOGOUT"
        private const val DIALOG_QUIT = "DIALOG_QUIT"
    }
}
