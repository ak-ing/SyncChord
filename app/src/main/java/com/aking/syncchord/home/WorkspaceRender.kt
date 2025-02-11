package com.aking.syncchord.home

import android.view.LayoutInflater
import com.aking.data.model.Workspace
import com.aking.reactive.dsl.MultiTypeRender
import com.aking.syncchord.R
import com.aking.syncchord.databinding.ItemWorkspaceBinding
import com.aking.syncchord.databinding.ItemWorkspaceDefBinding


/**
 * Description:
 * Created by Rick at 2024-11-21 20:50.
 */
class WorkspaceRender(inflater: LayoutInflater) : MultiTypeRender<Workspace>(inflater) {

    private var current: String = ""

    init {
        itemViewType { _, position ->
            if (position == 0) R.layout.item_workspace_def else R.layout.item_workspace
        }

        render { item, _, holder ->
            when (val binding = holder.binding) {
                is ItemWorkspaceBinding -> {
                    binding.run {
                        name.text = item.name
                        icon.isSelected = current == item.id
                    }
                }

                is ItemWorkspaceDefBinding -> {
                    binding.icon.isSelected = current == item.id
                }
            }
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