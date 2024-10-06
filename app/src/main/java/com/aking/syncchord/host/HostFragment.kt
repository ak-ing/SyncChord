package com.aking.syncchord.host

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aking.base.base.BaseFragment
import com.aking.base.widget.bindSystemWindowInsets
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHostBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Host
 */
class HostFragment : BaseFragment<FragmentHostBinding>(R.layout.fragment_host) {

    init {
        lifecycleLogEnable(true)
    }

    override fun FragmentHostBinding.initView() {
        root.bindSystemWindowInsets(topInsets = true)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_content) as NavHostFragment
        val navController = navHostFragment.navController
        navigation.setupWithNavController(navController)
        setupBadge(navigation)
    }

    private fun setupBadge(navView: BottomNavigationView) {
        val badge = navView.getOrCreateBadge(R.id.notification)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number or text is set:
        //badge.number = 15  // or badge.text = "New"
    }
}