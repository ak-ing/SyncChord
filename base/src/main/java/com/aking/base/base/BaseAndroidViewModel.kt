package com.aking.base.base

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.aking.base.extended.collectWithLifecycle
import com.aking.base.widget.logI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Created by Ak on 2024-12-26 20:46.
 */
abstract class BaseAndroidViewModel<S>(app: Application, initialState: S) : AndroidViewModel(app) {
    private var initializeCalled = false

    private val uiState = MutableStateFlow(initialState)
    val stateFlow = uiState.asStateFlow()

    fun getAppContext() = getApplication<Application>()

    /**
     * This function is idempotent provided it is only called from the UI thread.
     */
    @MainThread
    fun initialize(reactive: Reactive<S>? = null) {
        when (reactive) {
            is Fragment -> {
                stateFlow.collectWithLifecycle(reactive, collector = reactive::render)
            }

            is ComponentActivity -> {
                stateFlow.collectWithLifecycle(reactive, collector = reactive::render)
            }
        }
        if (initializeCalled) return
        initializeCalled = true
        onInitialize()
    }

    protected abstract fun onInitialize()

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