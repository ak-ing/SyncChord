package com.aking.base.dsl

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aking.base.widget.CommonViewHolder


/**
 * Dsl 的形式使用 RecyclerView ListAdapter
 * @author Ak
 * 2024/11/11 10:57
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderRow(
    scope: RowRender<D, VB>.() -> Unit
) {
    layoutManager = LinearLayoutManager(context)

    RowRender<D, VB>(
        reducer = { reducer(it) },
        diffSetup = { diff, render ->
            adapter = object : ListAdapter<D, CommonViewHolder>(diff) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): CommonViewHolder {
                    return CommonViewHolder(render.content(parent, viewType))
                }

                override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
                    render.run {
                        holder.getBind<VB>().render(getItem(position), position)
                    }
                }
            }
        }
    ).scope()
}

class RowRender<D, VB : ViewDataBinding>(
    private val reducer: (items: List<Any>) -> Unit,
    private val diffSetup: (diff: ItemCallback<D>, render: RowRender<D, VB>) -> Unit
) {
    lateinit var content: (parent: ViewGroup, viewType: Int) -> VB
    lateinit var render: VB.(item: D, position: Int) -> Unit

    fun setUp(items: List<Any>, diffCallback: ItemCallback<D>) {
        diffSetup(diffCallback, this)
        reducer(items)
    }

    fun content(content: (parent: ViewGroup, viewType: Int) -> VB) {
        this.content = content
    }

    fun render(render: VB.(item: D, position: Int) -> Unit) {
        this.render = render
    }
}

@Suppress("UNCHECKED_CAST")
fun RecyclerView.reducer(items: List<Any>) {
    adapter?.let {
        (it as ListAdapter<Any, *>).submitList(items)
    }
}