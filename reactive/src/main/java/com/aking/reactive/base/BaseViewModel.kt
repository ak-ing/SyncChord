package com.aking.reactive.base

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.aking.reactive.extended.collectWithLifecycle
import com.aking.reactive.widget.logI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Description:
 * 封装状态流，方便使用
 * [请避免在 ViewModel 的 init 块或构造函数中启动异步操作](https://developer.android.google.cn/topic/architecture/ui-layer/state-production?hl=zh-cn#initializing-state-production)
 * Created by Rick at 2024-10-17 19:50.
 */
abstract class BaseViewModel<S>(initialState: S) : ViewModel() {
    private var initializeCalled = false
    private var stateDiff = StateDiff(initialState)

    private val uiState = MutableStateFlow(initialState)
    val stateFlow = uiState.asStateFlow()

    /**
     * This function is idempotent provided it is only called from the UI thread.
     */
    @MainThread
    fun initialize(reactive: Reactive<S>? = null) {
        when (reactive) {
            is Fragment -> seedPipeline(reactive.viewLifecycleOwner, reactive)
            is ComponentActivity -> seedPipeline(reactive, reactive)
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

    /**
     * seed the state production pipeline
     */
    private fun seedPipeline(lifecycleOwner: LifecycleOwner, reactive: Reactive<S>) {
        stateDiff.clear()   // clear previous state diff
        stateFlow.collectWithLifecycle(lifecycleOwner, collector = {
            stateDiff.onEach(it) {
                reactive.render(it, stateDiff)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        logI("onCleared")
    }
}