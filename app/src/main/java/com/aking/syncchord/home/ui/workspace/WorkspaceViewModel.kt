package com.aking.syncchord.home.ui.workspace

import com.aking.base.base.BaseViewModel

data class WorkspaceState(
    val currentWorkspace: String = "0"
)

/**
 * @author Ak
 * 2024/12/31  17:44
 */
class WorkspaceViewModel : BaseViewModel<WorkspaceState>(WorkspaceState()) {
    override fun onInitialize() {

    }
}