package com.aking.reactive.extended

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * 在生命周期安全的收集
 * 解决Flow collect 协程多层嵌套
 */
fun <T> Flow<T>.collectWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: FlowCollector<T>
) = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
        .distinctUntilChanged()
        .collect(collector)
}

/** @see collectWithLifecycle */
fun <T> Flow<T>.collectWithLifecycle(
    fragment: Fragment,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: FlowCollector<T>
) = fragment.viewLifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle, minActiveState)
        .distinctUntilChanged()
        .collect(collector)
}
