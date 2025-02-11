package com.aking.reactive.dsl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation


/**
 * DSl 垂直列表渲染
 * @author Ak
 * 2024/11/11 10:57
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderColumn(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    scope: AdapterRender<D, VB>.() -> Unit
) {
    AdapterRender<D, VB>(LayoutInflater.from(context))
        .renderInner(this, items, diffCallback, RecyclerView.VERTICAL, reverse, scope)
}

/**
 * @see renderColumn
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderColumn(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    renderBuilder: AdapterRender<D, VB>
) {
    renderBuilder.renderInner(this, items, diffCallback, RecyclerView.VERTICAL, reverse)
}

/**
 * DSl 水平列表渲染
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderRow(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    scope: AdapterRender<D, VB>.() -> Unit
) {
    AdapterRender<D, VB>(LayoutInflater.from(context))
        .renderInner(this, items, diffCallback, RecyclerView.HORIZONTAL, reverse, scope)
}

/**
 * @see renderRow
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderRow(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    renderBuilder: AdapterRender<D, VB>
) {
    renderBuilder.renderInner(this, items, diffCallback, RecyclerView.HORIZONTAL, reverse)
}


inline fun <D, reified VB : ViewDataBinding> AdapterRender<D, VB>.renderInner(
    recyclerView: RecyclerView,
    items: List<Any> = emptyList(),
    diffCallback: ItemCallback<D>,
    @Orientation orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false,
    scope: AdapterRender<D, VB>.() -> Unit = {}
) {
    recyclerView.apply {
        scope()
        layoutManager = LinearLayoutManager(context, orientation, reverse)
        val innerAdapter = object : ListAdapter<D, ItemViewHolder>(diffCallback) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val content = requireNotNull(content) {
                    "content is null,check call content() method first."
                }(parent, viewType)
                return ItemViewHolder(content).apply {
                    event?.let {
                        content.root.setOnClickListener {
                            content.it({ getItem(adapterPosition) }, { adapterPosition }, viewType)
                        }
                    }
                }
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                render?.let { holder.getBind<VB>().it(getItem(position), position, holder) }
            }

            override fun getItemViewType(position: Int): Int {
                itemViewType?.let {
                    return it(getItem(position), position)
                }
                return super.getItemViewType(position)
            }

            override fun getItemId(position: Int): Long {
                itemId?.let {
                    return it(getItem(position), position)
                }
                return super.getItemId(position)
            }
        }
        this@renderInner.adapter = innerAdapter
        this.adapter = innerAdapter
        render(items)
    }
}


class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 获取ViewDataBinding
     */
    inline fun <reified T : ViewDataBinding> getBind(): T {
        return binding as T
    }
}

/**
 * Simple:
 * ```
 * class WorkspaceRender(inflater: LayoutInflater) : MultiTypeRender<Workspace>(inflater) {
 *
 *     init {
 *         itemViewType { _, position ->
 *             if (position == 0) R.layout.item_workspace_def else R.layout.item_workspace
 *         }
 *
 *         render { item, _, holder ->
 *             when (val binding = holder.binding) {
 *                 is ItemWorkspaceBinding -> {
 *                     binding.run {
 *                         name.text = item.name
 *                         icon.isSelected = current == item.id
 *                     }
 *                 }
 *
 *                 is ItemWorkspaceDefBinding -> {
 *                     binding.icon.isSelected = current == item.id
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 * 多类型渲染
 */
open class MultiTypeRender<D>(inflater: LayoutInflater) :
    AdapterRender<D, ViewDataBinding>(inflater) {
    override var content: ((parent: ViewGroup, viewType: Int) -> ViewDataBinding)? =
        { parent, viewType ->
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        }
}

/**
 * Simple:
 * ```
 * class WorkspaceRender(inflater: LayoutInflater) :
 *     AdapterRender<Workspace, ItemWorkspaceBinding>(inflater) {
 *
 *     init {
 *         //创建Item内容
 *         content { parent, viewType ->
 *             ItemWorkspaceBinding.inflate(inflater, parent, false)
 *         }
 *
 *        //渲染Item数据
 *         render { item, position, holder ->
 *             icon.setImageResource(R.drawable.selector_workspace_icon)
 *             name.text = item.name
 *         }
 *     }
 * }
 * ```
 * 通用Adapter渲染
 */
open class AdapterRender<D, VB : ViewDataBinding>(private val inflater: LayoutInflater) {
    var adapter: ListAdapter<D, ItemViewHolder>? = null

    open var content: ((parent: ViewGroup, viewType: Int) -> VB)? = null
    var event: (VB.(item: () -> D, position: () -> Int, viewType: Int) -> Unit)? = null
    var render: (VB.(item: D, position: Int, holder: ItemViewHolder) -> Unit)? = null

    var itemId: ((item: D, position: Int) -> Long)? = null
    var itemViewType: ((item: D, position: Int) -> Int)? = null

    /**
     * 创建Item内容
     */
    fun content(content: (parent: ViewGroup, viewType: Int) -> VB) {
        this.content = content
    }

    /**
     * Item事件设置
     */
    fun event(event: VB.(item: () -> D, position: () -> Int, viewType: Int) -> Unit) {
        this.event = event
    }

    /**
     * 渲染Item数据
     */
    fun render(render: VB.(item: D, position: Int, holder: ItemViewHolder) -> Unit) {
        this.render = render
    }

    fun itemViewType(itemViewType: (item: D, position: Int) -> Int) {
        this.itemViewType = itemViewType
    }

    fun itemId(itemId: (item: D, position: Int) -> Long) {
        this.itemId = itemId
    }

}

@Suppress("UNCHECKED_CAST")
fun RecyclerView.render(items: List<Any>) {
    adapter?.let {
        (it as ListAdapter<Any, *>).submitList(items)
    }
}