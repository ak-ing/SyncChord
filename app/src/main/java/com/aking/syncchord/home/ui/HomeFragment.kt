package com.aking.syncchord.home.ui

import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Serializable
data class AA(
    val identifier: JsonObject?
)

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun FragmentHomeBinding.initView() {
        
    }

}