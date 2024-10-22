package com.aking.syncchord.home.ui

import androidx.fragment.app.viewModels
import com.aking.base.base.BaseFragment
import com.aking.base.widget.bindSystemWindowInsets
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun FragmentHomeBinding.initView() {
        root.bindSystemWindowInsets(topInsets = true)
        tabMessage.setOnClickListener {
            tabMessage.isSelected = it.isSelected.not()
        }
    }
}