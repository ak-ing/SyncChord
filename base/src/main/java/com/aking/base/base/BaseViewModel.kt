package com.aking.base.base

import androidx.annotation.MainThread
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
    private var initializeCalled = false
    protected abstract val initialState: S
    private val uiState by lazy { MutableStateFlow(initialState) }
    val stateFlow by lazy { uiState.asStateFlow() }

    /**
     * This function is idempotent provided it is only called from the UI thread.
     */
    @MainThread
    fun initialize() {
        if (initializeCalled) return
        initializeCalled = true
        onInit()
    }

    protected abstract fun onInit()

    fun reduce(action: Action) {}

    protected fun update(reducer: S.() -> S) {
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