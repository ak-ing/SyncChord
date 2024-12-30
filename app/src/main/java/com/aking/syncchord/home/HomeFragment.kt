package com.aking.syncchord.home

import androidx.lifecycle.lifecycleScope
import com.aking.base.base.BaseFragment
import com.aking.base.base.Reactive
import com.aking.base.dsl.render
import com.aking.base.dsl.renderColumn
import com.aking.base.extended.collectWithLifecycle
import com.aking.data.model.Workspace
import com.aking.syncchord.R
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.databinding.FragmentHomeBinding
import com.aking.syncchord.databinding.ItemWorkspaceBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    Reactive<HomeState> {

    init {
        lifecycleLogEnable(true)
    }

    private val viewModel: HomeViewModel by viewModel()
    private val workspaceRender by lazy { WorkspaceRender(layoutInflater) }

    override fun FragmentHomeBinding.initView() {
        tabMessage.setOnClickListener {
            // test logout
            val repo by inject<AuthRepository>()
            lifecycleScope.launch { repo.logout(requireContext()) }
        }
        rvWorkspaces.renderColumn<Workspace, ItemWorkspaceBinding>(
            Workspace.diffCallback,
            renderBuilder = workspaceRender
        )
        workspaceRender.event { item, _, _ ->
            viewModel.reducer(HomeAction.SwitchWorkspace(item().id))
        }
    }

    override fun initData() {
        viewModel.initialize(this)
        viewModel.stateFlow
            .map { it.currentWorkspace }
            .distinctUntilChanged()
            .collectWithLifecycle(viewLifecycleOwner) {
                workspaceRender.reducer(it)
            }
    }

    override suspend fun render(state: HomeState) {
        binding.rvWorkspaces.render(state.workspaces)
    }

}