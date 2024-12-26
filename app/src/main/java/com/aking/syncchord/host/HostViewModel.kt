package com.aking.syncchord.host

import com.aking.base.base.BaseViewModel
import com.aking.syncchord.auth.AuthRepository

data class HostState(
    val validateSession: Boolean? = null
)

/**
 * Description:
 * Created by Rick at 2024-10-22 22:45.
 */
class HostViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<HostState>(HostState()) {

    fun validateMessageShown() {
        update { copy(validateSession = null) }
    }

    override fun onInitialize() {

    }
}