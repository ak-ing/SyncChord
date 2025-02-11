package com.aking.syncchord.host

import androidx.lifecycle.viewModelScope
import com.aking.reactive.Async
import com.aking.reactive.base.BaseViewModel
import com.aking.reactive.widget.logD
import com.aking.syncchord.auth.AuthRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class HostState(
    val validateSession: Boolean? = null
)

/**
 * Description:
 * Created by Rick at 2024-10-22 22:45.
 */
class HostViewModel(
    private val repository: AuthRepository
) : BaseViewModel<HostState>(HostState()) {

    override fun onInitialize() {
        logD("onInitialize")
        repository.authState.onEach {
            logD(it.toString())
            if (it is Async.Uninitialized) {
                update { copy(validateSession = false) }
            }
        }.launchIn(viewModelScope)
    }

    fun validateMessageShown() {
        update { copy(validateSession = null) }
    }
}