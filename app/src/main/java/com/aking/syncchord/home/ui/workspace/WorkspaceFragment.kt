package com.aking.syncchord.home.ui.workspace

import android.os.Bundle
import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentWorkspaceBinding

/**
 * @author Ak
 * 2024/12/30  16:58
 */
class WorkspaceFragment : BaseFragment<FragmentWorkspaceBinding>(R.layout.fragment_workspace) {

    companion object {
        private const val WORKSPACE_ID = "workspace_id"

        fun newInstance(id: String) = WorkspaceFragment().apply {
            arguments = Bundle().apply {
                putString(WORKSPACE_ID, id)
            }
        }
    }

    override fun FragmentWorkspaceBinding.initView() {

    }

}