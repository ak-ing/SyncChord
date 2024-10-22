package com.aking.syncchord.host

import androidx.lifecycle.viewModelScope
import com.aking.base.base.BaseViewModel
import com.aking.base.widget.logI
import com.aking.syncchord.auth.AuthRepository
import kotlinx.coroutines.launch

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

    init {
        viewModelScope.launch {
            authRepository.validateSession().collect { validate ->
                validate.onSuccess {
                    logI("validateSession: $it")
                    setState { copy(validateSession = it) }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    }

    fun validateMessageShown() {
        setState { copy(validateSession = null) }
    }
}