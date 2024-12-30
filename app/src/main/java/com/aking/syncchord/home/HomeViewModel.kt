package com.aking.syncchord.home

import androidx.lifecycle.viewModelScope
import com.aking.base.base.BaseViewModel
import com.aking.base.base.Intent
import com.aking.base.base.Reducer
import com.aking.base.widget.logD
import com.aking.data.model.Workspace
import com.aking.syncchord.home.domain.WorkspaceRepository
import kotlinx.coroutines.launch

const val WORKSPACE_MESSAGE_ID = "0"

data class HomeState(
    val currentWorkspace: String = WORKSPACE_MESSAGE_ID,
    val workspaces: List<Workspace> = emptyList()
)

sealed class HomeAction : Intent {
    data class CreateWorkspace(val name: String) : HomeAction()
    data class SwitchWorkspace(val id: String) : HomeAction()
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
            is HomeAction.CreateWorkspace -> createWorkspace(intent.name)
            is HomeAction.SwitchWorkspace -> switchWorkspace(intent.id)
        }
    }

    /** 更新状态并附加默认workspace */
    private fun updateAppendDefault(workspaces: List<Workspace> = emptyList()) {
        update { copy(workspaces = listOf(defWorkspace) + workspaces) }
    }

    private fun createWorkspace(name: String) = viewModelScope.launch {
        logD("createWorkspace")
        workspaceRepo.createWorkspace(name).onSuccess {
            logD("onSuccess $it")
        }.onFailure {
            logD("onFailure $it")
        }

    }

    private fun switchWorkspace(id: String) {
        update { copy(currentWorkspace = id) }
    }

}