package com.aking.syncchord.home

import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.aking.base.base.BaseFragment
import com.aking.base.base.Reactive
import com.aking.base.dsl.render
import com.aking.base.dsl.renderColumn
import com.aking.base.extended.collectWithLifecycle
import com.aking.data.model.Workspace
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding
import com.aking.syncchord.databinding.ItemWorkspaceBinding
import com.aking.syncchord.home.ui.workspace.WorkspaceFragment
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    Reactive<HomeState> {

    init {
        lifecycleLogEnable(true)
    }

    private val viewModel: HomeViewModel by viewModel()
    private val workspaceRender by lazy { WorkspaceRender(layoutInflater) }

    override fun FragmentHomeBinding.initView() {
        rvWorkspaces.renderColumn<Workspace, ItemWorkspaceBinding>(
            Workspace.diffCallback,
            renderBuilder = workspaceRender
        )
        workspaceRender.event { item, _, _ ->
            val workspaceId = item().id
            viewModel.reducer(HomeAction.SwitchWorkspace(workspaceId))
            childFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.workspace_panel, WorkspaceFragment.newInstance(workspaceId))
            }
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