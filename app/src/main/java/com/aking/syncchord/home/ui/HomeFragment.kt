package com.aking.syncchord.home.ui

import androidx.lifecycle.lifecycleScope
import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Serializable
data class AA(
    val identifier: JsonObject?
)

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    init {
        lifecycleLogEnable(true)
    }
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun FragmentHomeBinding.initView() {
        tabMessage.setOnClickListener {
            val repo by inject<AuthRepository>()
            lifecycleScope.launch { repo.logout(requireContext()) }
        }
    }

    override fun initData() {
        viewModel.initialize()
    }

}