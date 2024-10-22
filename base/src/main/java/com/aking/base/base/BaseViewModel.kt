package com.aking.base.base

import androidx.lifecycle.ViewModel
import com.aking.base.widget.logI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Description:
 * 封装状态流，方便使用
 * Created by Rick at 2024-10-17 19:50.
 */
abstract class BaseViewModel<S> : ViewModel() {
    protected abstract val initialState: S
    private val uiState by lazy { MutableStateFlow(initialState) }
    val stateFlow by lazy { uiState.asStateFlow() }

    protected fun setState(reducer: S.() -> S) {
        uiState.update(reducer)
    }

    protected fun setState(state: S) {
        uiState.value = state
    }

    override fun onCleared() {
        super.onCleared()
        logI("onCleared")
    }
}