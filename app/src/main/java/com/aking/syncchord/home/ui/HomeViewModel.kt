package com.aking.syncchord.home.ui

import androidx.lifecycle.viewModelScope
import com.aking.data.model.Workspace
import com.aking.reactive.base.BaseViewModel
import com.aking.reactive.base.Intent
import com.aking.reactive.base.Reducer
import com.aking.reactive.widget.logD
import com.aking.syncchord.home.data.repository.WorkspaceRepository
import kotlinx.coroutines.launch

const val WORKSPACE_MESSAGE_ID = "0"

data class HomeState(
    val currentWorkspace: String = WORKSPACE_MESSAGE_ID,
    val workspaces: List<Workspace> = emptyList(),
    val showCreateWorkspace: Boolean = false
)

sealed class HomeAction : Intent {
    data class SwitchWorkspace(val id: String) : HomeAction()
    data object CreateWorkspaceShown : HomeAction()
}

class HomeViewModel(
    private val workspaceRepo: WorkspaceRepository
) : BaseViewModel<HomeState>(HomeState()), Reducer<HomeAction> {

    private val defWorkspace = Workspace(0.0, WORKSPACE_MESSAGE_ID, "", "Default", "")

    override fun onInitialize() {
        updateAppendDefault()
        viewModelScope.launch {
            workspaceRepo.getWorkspaces().collect {
                logD("$it")
                updateAppendDefault(it.getOrDefault(emptyList()))
            }
        }
    }

    override fun reducer(intent: HomeAction) {
        when (intent) {
            is HomeAction.SwitchWorkspace -> switchWorkspace(intent.id)
            is HomeAction.CreateWorkspaceShown -> createWorkspaceShown()
        }
    }

    /** 更新状态并附加默认workspace */
    private fun updateAppendDefault(workspaces: List<Workspace> = emptyList()) {
        update {
            copy(
                showCreateWorkspace = workspaces.isEmpty(),
                workspaces = listOf(defWorkspace) + workspaces
            )
        }
    }


    /** 切换workspace */
    private fun switchWorkspace(id: String) {
        update { copy(currentWorkspace = id) }
    }

    /** 清楚显示创建workspace页面状态 */
    private fun createWorkspaceShown() {
        update { copy(showCreateWorkspace = false) }
    }
}