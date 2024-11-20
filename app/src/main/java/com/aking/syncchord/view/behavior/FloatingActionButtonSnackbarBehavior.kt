package com.aking.syncchord.view.behavior

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

/**
 * Description:
 * Created by Rick at 2024-11-18 21:17.
 */
@SuppressLint("RestrictedApi")
class FloatingActionButtonSnackbarBehavior(context: Context?, attrs: AttributeSet?) :
    FloatingActionButton.Behavior(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return updateFloatingActionButtonPosition(child, dependency)
    }

    private fun updateFloatingActionButtonPosition(child: View, dependency: View): Boolean {
        if (dependency is SnackbarLayout) {
            val oldTranslation = child.translationY
            val height = dependency.getHeight().toFloat()
            val newTranslation = dependency.getTranslationY() - height
            child.translationY = newTranslation
            return oldTranslation != newTranslation
        } else {
            return false
        }
    }

}