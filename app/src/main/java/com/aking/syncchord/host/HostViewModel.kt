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
    override val initialState: HostState,
    private val authRepository: AuthRepository
) : BaseViewModel<HostState>() {

    fun validateMessageShown() {
        update { copy(validateSession = null) }
    }

    override fun onInit() {

    }
}