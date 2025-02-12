package com.aking.syncchord.host

import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aking.reactive.base.BaseFragment
import com.aking.reactive.base.Reactive
import com.aking.reactive.base.StateDiff
import com.aking.syncchord.R
import com.aking.syncchord.auth.AuthFragment
import com.aking.syncchord.databinding.FragmentHostBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Host
 */
class HostFragment : BaseFragment<FragmentHostBinding>(R.layout.fragment_host),
    Reactive<HostState> {
    init {
        lifecycleLogEnable(true)
    }
    companion object {
        fun newInstance(): HostFragment {
            return HostFragment()
        }
    }

    private val viewModel: HostViewModel by viewModel()

    override fun FragmentHostBinding.initView() {
        setAppearanceLightStatusBars(true)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_content) as NavHostFragment
        val navController = navHostFragment.navController
        navigation.setupWithNavController(navController)
        setupBadge(navigation)
    }

    override fun initData() {
        viewModel.initialize(this)
    }


    override suspend fun render(state: HostState, diff: StateDiff<HostState>) {
        state.validateSession?.let {
            naviToAuth()
        }
    }

    private fun setupBadge(navView: BottomNavigationView) {
        val badge = navView.getOrCreateBadge(R.id.notification)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number or text is set:
        //badge.number = 15  // or badge.text = "New"
    }

    /** 导航到 Auth */
    private fun naviToAuth() {
        viewModel.validateMessageShown()
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.nav_host, AuthFragment.newInstance())
        }
    }
}