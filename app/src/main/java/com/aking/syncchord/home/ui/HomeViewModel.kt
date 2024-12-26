package com.aking.syncchord.home.ui

import androidx.lifecycle.viewModelScope
import com.aking.base.base.BaseViewModel
import com.aking.base.widget.logI
import com.aking.syncchord.util.Constants.WORKSPACE_MESSAGE_ID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class HomeState(
    val currentWorkspace: Int = WORKSPACE_MESSAGE_ID,
    val workspaces: String = ""
)

class HomeViewModel(
    private val workspaceRepo: WorkspaceRepository
) : BaseViewModel<HomeState>(HomeState()) {

    init {
        viewModelScope.launch {
            logI("HomeViewModel init")
            workspaceRepo.getWorkspaces().onEach {
                logI("$it")
            }.collect()
        }
    }

    override fun onInitialize() {

    }

    fun createWorkspace(name: String) = viewModelScope.launch {
        logI("createWorkspace")
        workspaceRepo.createWorkspace(name).onSuccess {
            logI("onSuccess $it")
        }.onFailure {
            logI("onFailure $it")
        }

    }

    fun switchWorkspace(id: Int) {
        update { copy(currentWorkspace = id) }
    }

}