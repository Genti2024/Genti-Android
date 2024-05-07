package kr.genti.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityMainBinding
import kr.genti.presentation.main.create.CreateFragment
import kr.genti.presentation.main.feed.FeedFragment
import kr.genti.presentation.main.profile.ProfileFragment

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initCreateBtnListener()
        setStatusBarColor()
    }

    private fun initBnvItemIconTintList() {
        with(binding.bnvMain) {
            itemIconTintList = null
            selectedItemId = R.id.menu_create
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main) ?: navigateTo<CreateFragment>()

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
            true
        }
    }

    fun initCreateBtnListener() {
        binding.btnMenuCreate.setOnSingleClickListener {
            supportFragmentManager.commit {
                replace<CreateFragment>(R.id.fcv_main)
            }
            initBnvItemIconTintList()
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
