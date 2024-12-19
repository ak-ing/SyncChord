package com.aking.syncchord.auth

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.base.widget.logE
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import com.aking.syncchord.host.HostFragment
import com.aking.syncchord.util.SnackBarHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth) {
    init {
        lifecycleLogEnable(true)
    }

    private val authViewModel: AuthViewModel by viewModel()
    private val snackBarHelper by lazy { SnackBarHelper(binding.root) }

    override fun FragmentAuthBinding.initView() {
        setAppearanceLightStatusBars(false)
        authViewModel.initialize()
        login.setOnClickListener {
            authViewModel.signIn(requireActivity())
        }
    }

    override fun FragmentAuthBinding.initData() {
        authViewModel.stateFlow.collectWithLifecycle(viewLifecycleOwner) { state ->
            logE(state.toString())
            if (state.isAuthenticated) {
                naviToHost()
                return@collectWithLifecycle
            }
            showLoadingUI(state.isLoading)
            state.errorMessage?.let {
                showAuthFailed(it, state.error)
            }
        }
        // 尝试使用以前缓存的凭据登录
        authViewModel.signInAutomatically()
    }

    /**
     * Show a loading spinner if the user is authenticating.
     */
    private fun showLoadingUI(show: Boolean) {
        if (show) {
            if (binding.groupLoading.isVisible) return
            binding.groupLoading.visibility = View.VISIBLE
            binding.groupDef.visibility = View.INVISIBLE
        } else {
            binding.groupLoading.visibility = View.GONE
            binding.groupDef.visibility = View.VISIBLE
        }
    }

    /**
     * Show an error message and log the error.
     */
    private fun showAuthFailed(errorMessage: String, error: Throwable?) {
        snackBarHelper.showError(errorMessage, error)
        authViewModel.userMessageShown()
    }

    /**
     * Navigate to the host destination
     */
    private fun naviToHost() {
        parentFragmentManager.commit {
            replace(R.id.nav_host, HostFragment.newInstance())
        }
    }
}