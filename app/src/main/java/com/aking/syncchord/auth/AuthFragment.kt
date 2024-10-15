package com.aking.syncchord.auth

import android.widget.Toast
import androidx.annotation.StringRes
import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import com.google.android.material.snackbar.Snackbar
import dev.convex.android.AuthState
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
        authViewModel.authState.collectWithLifecycle(viewLifecycleOwner) {
            when (it) {
                is AuthState.AuthLoading -> {
                    login.setText(R.string.text_auth_loading)
                    login.isEnabled = false
                }

                is AuthState.Unauthenticated -> {
                    login.setText(R.string.text_nav_login)
                    login.isEnabled = true
                }

                is AuthState.Authenticated -> {
                    Snackbar.make(root, R.string.text_welcome, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        //loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
        //    loginResult ?: return@Observer
        //    loading.visibility = View.GONE
        //    loginResult.error?.let {
        //        showLoginFailed(it)
        //    }
        //    loginResult.success?.let {
        //        updateUiWithUser(it)
        //    }
        //})
    }

    private fun updateUiWithUser(displayName: String) {
        val welcome = getString(R.string.text_welcome) + displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}