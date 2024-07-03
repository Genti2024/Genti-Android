package kr.genti.presentation.main

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.domain.enums.PictureRatio
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityMainBinding
import kr.genti.presentation.main.create.CreateFragment
import kr.genti.presentation.main.feed.FeedFragment
import kr.genti.presentation.main.profile.ProfileFragment
import kr.genti.presentation.result.finished.FinishedActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initCreateBtnListener()
        setStatusBarColor()
        moveToFinish()
    }

    // TODO 서버통신 진행 후 삭제
    private fun moveToFinish() {
        binding.btnMoveToFinish.setOnSingleClickListener {
            FinishedActivity.createIntent(
                this,
                0,
                "https://github.com/Marchbreeze/Marchbreeze/assets/97405341/ad58982b-9ba3-448d-a788-748511718ffe",
                PictureRatio.RATIO_2_3.name,
            ).apply { startActivity(this) }
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
            supportFragmentManager.commit {
                replace<CreateFragment>(R.id.fcv_main)
            }
            binding.bnvMain.selectedItemId = R.id.menu_create
        }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }
}
