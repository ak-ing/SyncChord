package com.aking.syncchord.home.ui.workspace

import android.annotation.SuppressLint
import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentCreateWorkspaceBinding
import com.google.android.material.internal.ViewUtils

/**
 * @author Created by Ak on 2025-01-02 21:50.
 */
class CreateWorkspaceFragment :
    BaseFragment<FragmentCreateWorkspaceBinding>(R.layout.fragment_create_workspace) {

    override fun FragmentCreateWorkspaceBinding.initView() {
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        // hide keyboard
        etServiceName.setOnFocusChangeListener { v, hasFocus ->
            @SuppressLint("RestrictedApi")
            if (hasFocus.not()) ViewUtils.hideKeyboard(v)
        }
    }

    override fun initData() {
    }

    companion object {
        fun newInstance() = CreateWorkspaceFragment()
    }
}