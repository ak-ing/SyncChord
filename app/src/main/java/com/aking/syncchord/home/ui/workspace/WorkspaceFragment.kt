package com.aking.syncchord.home.ui.workspace

import androidx.core.os.bundleOf
import com.aking.reactive.base.BaseFragment
import com.aking.reactive.base.Reactive
import com.aking.reactive.base.StateDiff
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentWorkspaceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Ak
 * 2024/12/30  16:58
 */
class WorkspaceFragment : BaseFragment<FragmentWorkspaceBinding>(R.layout.fragment_workspace),
    Reactive<WorkspaceState> {

    companion object {
        private const val WORKSPACE_ID = "workspace_id"

        fun newInstance(id: String) = WorkspaceFragment().apply {
            arguments = bundleOf(WORKSPACE_ID to id)
        }
    }

    private val viewModel by viewModel<WorkspaceViewModel>()

    override fun FragmentWorkspaceBinding.initView() {}

    override fun initData() {
        viewModel.initialize(this)
    }

    override suspend fun render(state: WorkspaceState, diff: StateDiff<WorkspaceState>) {

    }

}