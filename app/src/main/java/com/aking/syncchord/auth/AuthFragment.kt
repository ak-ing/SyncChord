package com.aking.syncchord.auth

import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.base.widget.logE
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth) {
    init {
        lifecycleLogEnable(true)
    }

    private val authViewModel: AuthViewModel by activityViewModel()

    override fun FragmentAuthBinding.initView() {
        setAppearanceLightStatusBars(false)
        login.setOnClickListener {
            authViewModel.signIn(requireActivity())
        }
    }

    override fun FragmentAuthBinding.initData() {
        authViewModel.stateFlow.collectWithLifecycle(viewLifecycleOwner) { state ->
            logE(state.toString())
            if (state.isAuthenticated) return@collectWithLifecycle
            showLoadingUI(state.isLoading)
            state.errorMessage?.let {
                showAuthFailed(it, state.error)
            }
        }
    }

    private fun showLoadingUI(show: Boolean) {
        binding.groupLoading.isVisible = show
        binding.groupDef.isVisible = show.not()
        setAppearanceLightStatusBars(show)
    }

    private fun showAuthFailed(errorMessage: String, error: Throwable?) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).apply {
            setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            if (error != null) {
                setAction(R.string.text_error_msg) {
                    val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
                    // Creates a new text clip to put on the clipboard.
                    val clip: ClipData = ClipData.newPlainText("报错日志", error.toString())
                    // Set the clipboard's primary clip.
                    clipboard?.setPrimaryClip(clip)
                }
            }
            show()
        }
        authViewModel.userMessageShown()
    }
}