package com.aking.syncchord.auth

import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.base.widget.logE
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import com.aking.syncchord.util.SnackBarHelper
import com.aking.syncchord.util.navigateAndClearBackStack
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth) {
    init {
        lifecycleLogEnable(true)
    }

    private val authViewModel: AuthViewModel by activityViewModel()
    private val snackBarHelper by lazy { SnackBarHelper(binding.root) }

    override fun FragmentAuthBinding.initView() {
        setAppearanceLightStatusBars(false)
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
    }

    /**
     * Show a loading spinner if the user is authenticating.
     */
    private fun showLoadingUI(show: Boolean) {
        if (show) {
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
        findNavController().navigateAndClearBackStack(
            R.id.host, navOptionsBuilder = NavOptions.Builder()
                .setEnterAnim(R.animator.nav_default_enter_anim)
                .setExitAnim(R.animator.nav_default_exit_anim)
                .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
        )
    }
}