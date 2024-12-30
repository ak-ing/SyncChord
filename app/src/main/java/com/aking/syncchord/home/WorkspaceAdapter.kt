package com.aking.syncchord.home

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.aking.base.dsl.RenderBuilder
import com.aking.data.model.Workspace
import com.aking.syncchord.R
import com.aking.syncchord.databinding.ItemWorkspaceBinding

/**
 * Description:
 * Created by Rick at 2024-11-21 20:50.
 */
class WorkspaceRender(inflater: LayoutInflater) :
    RenderBuilder<Workspace, ItemWorkspaceBinding>() {

    private var current: String = ""

    init {
        content { parent, _ ->
            ItemWorkspaceBinding.inflate(inflater, parent, false)
        }
        render { item, position, holder ->
            if (position == 0) {
                name.isVisible = false
                icon.setImageResource(R.drawable.animated_selector_workspace_icon)
            } else {
                name.isVisible = true
                name.text = item.name
                icon.setImageResource(R.drawable.selector_workspace_icon)
            }
            icon.isSelected = current == item.id
        }
    }

    fun reducer(current: String) {
        val old = this.current
        this.current = current
        adapter?.run {
            notifyItemChanged(currentList.indexOfFirst { it.id == old })
            notifyItemChanged(currentList.indexOfFirst { it.id == current })
        }
    }
}