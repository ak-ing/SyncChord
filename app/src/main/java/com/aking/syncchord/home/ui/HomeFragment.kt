package com.aking.syncchord.home.ui

import androidx.lifecycle.lifecycleScope
import com.aking.base.base.BaseFragment
import com.aking.base.extended.collectOfRepeat
import com.aking.base.widget.logI
import com.aking.data.Convex
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding
import com.aking.syncchord.util.Constants.WORKSPACE_MESSAGE_ID
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel
@Serializable
data class AA(
        val identifier:JsonObject?
        )

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun FragmentHomeBinding.initView() {
        viewModel.stateFlow.collectOfRepeat(viewLifecycleOwner) {
            tabMessage.isSelected = it.currentWorkspace == WORKSPACE_MESSAGE_ID
        }

        tabMessage.setOnClickListener {
            //viewModel.switchWorkspace(WORKSPACE_MESSAGE_ID)
            viewModel.createWorkspace("ak的2")
//            lifecycleScope.launch {
//                val a = Convex.client.mutation<AA>("auth:signInAuth0Test",null)
//                logI(a.toString())
//            }
        }

    }

}