package com.aking.reactive.extended

import android.view.View
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * @author Ak
 * 2024/9/27 15:35
 */

/**
 * sample:
 * ```
 * viewLifecycleOwner.lifecycleScope.launch {
 *     // 将该视图设置为不可见，再设置一些文字
 *     titleView.isInvisible = true
 *     titleView.text = "Hi everyone!"
 *
 *     // 等待下一次布局事件的任务，然后才可以获取该视图的高度
 *     titleView.awaitNextLayout()
 *
 *     // 布局任务被执行
 *     // 现在，我们可以将视图设置为可见，并其向上平移，然后执行向下的动画
 *     titleView.isVisible = true
 *     titleView.translationY = -titleView.height.toFloat()
 *     titleView.animate().translationY(0f)
 * }
 * ```
 * 等待 View 被布局完成
 */
suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->
    // 这里的 lambda 表达式会被立即调用，允许我们创建一个监听器
    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            // 视图的下一次布局任务被调用
            // 先移除监听，防止协程泄漏
            removeOnLayoutChangeListener(this)
            // 最终，唤醒协程，恢复执行
            cont.resume(Unit)
        }

    }
    // 如果协程被取消，移除该监听
    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
    // 最终，将监听添加到 view 上
    addOnLayoutChangeListener(listener)
    // 这样协程就被挂起了，除非监听器中的 cont.resume() 方法被调用
}