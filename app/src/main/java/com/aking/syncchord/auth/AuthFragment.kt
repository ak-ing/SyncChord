package com.aking.syncchord.auth

import android.widget.Toast
import androidx.annotation.StringRes
import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentAuthBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth) {
    init {
        lifecycleLogEnable(true)
    }

    private val loginViewModel: AuthViewModel by viewModel()

    override fun FragmentAuthBinding.initView() {
        setAppearanceLightStatusBars(false)
        login.setOnClickListener {
            loginViewModel.signIn(requireActivity())
        }
    }

    override fun FragmentAuthBinding.initData() {
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