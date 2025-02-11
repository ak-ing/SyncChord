package com.aking.syncchord.auth

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.aking.reactive.Async
import com.aking.reactive.base.BaseFragment
import com.aking.reactive.base.Reactive
import com.aking.reactive.base.StateDiff
import com.aking.reactive.widget.logE
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import com.aking.syncchord.home.ui.workspace.WorkspaceState
import com.aking.syncchord.host.HostFragment
import com.aking.syncchord.util.SnackBarHelper
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth),
    Reactive<AuthState> {

        init {
            lifecycleLogEnable(true)
        }

    private val authViewModel: AuthViewModel by viewModel()

    override fun FragmentAuthBinding.initView() {
        setAppearanceLightStatusBars(false)
        login.setOnClickListener {
            authViewModel.reducer(AuthAction.SignIn(requireActivity()))
        }
    }

    override fun initData() {
        authViewModel.initialize(this)
        // 尝试使用以前缓存的凭据登录
        authViewModel.reducer(AuthAction.SignInAutomatically)
    }

    override suspend fun render(state: AuthState, diff: StateDiff<AuthState>) {
        logE("render state: $state")
        when (state.auth) {
            is Async.Fail -> {
                showAuthFailed(getString(R.string.text_auth_fail), state.auth.error)
            }

            is Async.Success -> naviToHost()
            else -> showLoadingUI(state.auth is Async.Loading)
        }
    }

    /**
     * Show a loading spinner if the user is authenticating.
     */
    private suspend fun showLoadingUI(show: Boolean) {
        if (show) {
            if (binding.groupLoading.isVisible) return
            binding.groupLoading.visibility = View.VISIBLE
            binding.groupDef.visibility = View.INVISIBLE
            delay(1000)
        } else {
            binding.groupLoading.visibility = View.GONE
            binding.groupDef.visibility = View.VISIBLE
        }
    }

    /**
     * Show an error message and log the error.
     */
    private fun showAuthFailed(errorMessage: String, error: Throwable?) {
        SnackBarHelper.showError(errorMessage, error, binding.root)
        authViewModel.reducer(AuthAction.UserMessageShown)
    }

    /**
     * Navigate to the host destination
     */
    private fun naviToHost() {
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.nav_host, HostFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = AuthFragment()
    }
}