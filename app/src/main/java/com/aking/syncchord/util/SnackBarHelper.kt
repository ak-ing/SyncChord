package com.aking.syncchord.util

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import com.aking.syncchord.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/**
 * Description:
 * 用于显示 Snackbar 的辅助类。
 *
 * 此类提供了一些方法，用于显示具有不同选项的 Snackbar，
 * 例如消息、持续时间、操作标签和操作。
 *
 * Created by Rick at 2024-10-18 21:27.

 * @param view Snackbar 将锚定到的视图。
 */
class SnackBarHelper(private val view: View) {

    /**
     * 显示具有给定消息和持续时间的 Snackbar。
     *
     * @param message 要在 Snackbar 中显示的消息。
     * @param duration Snackbar 的持续时间。
     */
    fun showMessage(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        anchorView: View = view,
        onDismiss: (() -> Unit)? = null
    ) {
        Snackbar.make(anchorView, message, duration)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    onDismiss?.invoke()
                }
            })
            .show()
    }

    /**
     * 显示具有给定消息、操作标签、持续时间和操作的 [Snackbar]。
     *
     * @param message 要在 Snackbar 中显示的消息。
     * @param actionLabel 操作按钮的标签。
     * @param duration Snackbar 的持续时间。
     * @param action 单击操作按钮时要执行的操作。
     */
    fun showMessage(
        message: String,
        actionLabel: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        anchorView: View = view,
        onDismiss: (() -> Unit)? = null,
        action: (View) -> Unit
    ) {
        Snackbar.make(anchorView, message, duration)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAction(actionLabel, action)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    onDismiss?.invoke()
                }
            })
            .show()
    }

    /**
     * 显示具有给定消息、错误、操作标签和持续时间的 [Snackbar]。
     *
     * 如果提供了错误，Snackbar 将包含一个操作按钮，允许用户将错误日志复制到剪贴板。
     *
     * @param message 要在 Snackbar 中显示的消息。
     * @param error 要在 Snackbar 中显示的错误。
     * @param actionLabel 操作按钮的标签。
     * @param duration Snackbar 的持续时间。
     */
    fun showError(
        message: String, error: Throwable? = null,
        actionLabel: String = view.context.getString(R.string.text_error_msg),
        duration: Int = Snackbar.LENGTH_SHORT,
        anchorView: View = view
    ) {
        Snackbar.make(anchorView, message, duration)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .apply {
                if (error != null) {
                    setAction(actionLabel) {
                        val clipboard = view.context.getSystemService(ClipboardManager::class.java)
                        // Creates a new text clip to put on the clipboard.
                        val clip: ClipData = ClipData.newPlainText("Error Log", error.toString())
                        // Set the clipboard's primary clip.
                        clipboard?.setPrimaryClip(clip)
                    }
                }
            }.show()
    }
}