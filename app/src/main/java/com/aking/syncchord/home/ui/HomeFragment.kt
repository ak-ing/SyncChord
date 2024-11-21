package com.aking.syncchord.home.ui

import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding
import com.aking.syncchord.util.Constants.WORKSPACE_MESSAGE_ID
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun FragmentHomeBinding.initView() {
        viewModel.stateFlow.collectWithLifecycle(viewLifecycleOwner) {
            tabMessage.isSelected = it.currentWorkspace == WORKSPACE_MESSAGE_ID
        }

        tabMessage.setOnClickListener {
            viewModel.switchWorkspace(WORKSPACE_MESSAGE_ID)
            viewModel.createWorkspace("ak的")
        }

    }

}