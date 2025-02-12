package com.aking.syncchord.home.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.aking.data.model.Workspace
import com.aking.reactive.base.BaseFragment
import com.aking.reactive.base.Reactive
import com.aking.reactive.base.StateDiff
import com.aking.reactive.dsl.render
import com.aking.reactive.dsl.renderColumn
import com.aking.reactive.widget.bindSpacingDecoration
import com.aking.reactive.widget.logI
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentHomeBinding
import com.aking.syncchord.home.ui.workspace.CreateWorkspaceFragment
import com.aking.syncchord.home.ui.workspace.MsgWorkspaceFragment
import com.aking.syncchord.home.ui.workspace.WorkspaceFragment
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    Reactive<HomeState> {

    init {
        lifecycleLogEnable(true)
    }

    private val viewModel: HomeViewModel by viewModel()
    private val workspaceRender by lazy { WorkspaceRender(layoutInflater) }

    override fun FragmentHomeBinding.initView() {
        rvWorkspaces.itemAnimator = null
        rvWorkspaces.bindSpacingDecoration(topSpacing = 8, expectFirst = true)
        rvWorkspaces.renderColumn(
            Workspace.diffCallback,
            renderBuilder = workspaceRender
        )
        workspaceRender.event { item, _, _ ->
            val workspaceId = item().id
            viewModel.reducer(HomeAction.SwitchWorkspace(workspaceId))
        }
    }

    override fun initData() {
        viewModel.initialize(this)
    }

    override suspend fun render(state: HomeState, diff: StateDiff<HomeState>) {
        // 渲染workspace列表
        diff({ state.workspaces }) {
            binding.rvWorkspaces.render(it)
        }
        // 切换workspace面板
        diff({ it.currentWorkspace }) {
            logI("initData currentWorkspace = $it")
            workspaceRender.reducer(it)
            childFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.workspace_panel, switchWorkspacePanel(it))
            }
        }
        // 弹窗'创建workspace'面板
        if (state.showCreateWorkspace) {
            delay(1000)
            viewModel.reducer(HomeAction.CreateWorkspaceShown)
            naviToCreateWorkspace()
        }
    }

    /** 切换workspace面板 */
    private fun switchWorkspacePanel(workspaceId: String): Fragment {
        return when (workspaceId) {
            WORKSPACE_MESSAGE_ID -> MsgWorkspaceFragment()
            else -> WorkspaceFragment.newInstance(workspaceId)
        }
    }

    /** 跳转到创建workspace */
    private fun naviToCreateWorkspace() {
        requireActivity().supportFragmentManager.commit {
            addToBackStack(null)
            setCustomAnimations(
                R.anim.nav_slide_in_bottom_top, R.anim.nav_fade_out,
                R.anim.nav_fade_in, R.anim.nav_slide_out_top_to_bottom
            )
            replace(R.id.nav_host, CreateWorkspaceFragment.newInstance())
        }
    }

}