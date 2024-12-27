package com.aking.syncchord.host

import com.aking.base.base.BaseViewModel

data class HostState(
    val validateSession: Boolean? = null
)

/**
 * Description:
 * Created by Rick at 2024-10-22 22:45.
 */
class HostViewModel() : BaseViewModel<HostState>(HostState()) {

    fun validateMessageShown() {
        update { copy(validateSession = null) }
    }

    override fun onInitialize() {

    }
}