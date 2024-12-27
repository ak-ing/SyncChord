package com.aking.syncchord.host

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aking.base.base.BaseFragment
import com.aking.base.base.Reactive
import com.aking.base.widget.logI
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHostBinding
import com.aking.syncchord.util.SnackBarHelper
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

    override fun render(state: HostState) {
        if (state.validateSession == false) {
            SnackBarHelper.showMessage("认证过期，请重新登录", binding.root) {
                lifecycleScope.launchWhenResumed {
                    logI("认证过期 to auth")
                    //findNavController().navigate(R.id.action_host_to_auth)
                }
            }
            viewModel.validateMessageShown()
        }
    }

    private fun setupBadge(navView: BottomNavigationView) {
        val badge = navView.getOrCreateBadge(R.id.notification)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number or text is set:
        //badge.number = 15  // or badge.text = "New"
    }
}