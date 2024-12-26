package com.aking.base.extended

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * 在生命周期安全的收集
 * 解决Flow collect 协程多层嵌套
 */
fun <T> Flow<T>.collectWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    collector: FlowCollector<T>
) = lifecycleOwner.lifecycleScope.launchWhenStarted {
    collect(collector)
}

/** @see collectWithLifecycle */
fun <T> Flow<T>.collectWithLifecycle(
    fragment: Fragment,
    collector: FlowCollector<T>
) = fragment.viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    collect(collector)
}

/**
 * 在生命周期安全的收集，onStart重复收集
 * 解决Flow collect 协程多层嵌套
 */
fun <T> Flow<T>.launchAndCollectIn(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: FlowCollector<T>
) = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(collector)
}

/** @see launchAndCollectIn */
fun <T> Flow<T>.launchAndCollectIn(
    fragment: Fragment,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) = fragment.viewLifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle, minActiveState).collect(action)
}
