package com.aking.base.dsl

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation


/**
 * Simple:
 * ```
 * recyclerView.renderRow<String, FragmentHomeBinding> {
 *     setUp(emptyList(), diffCallback)
 *     // 创建Item内容
 *     content { parent, viewType ->
 *         FragmentHomeBinding.inflate(layoutInflater, parent, false)
 *     }
 *     // Item事件设置
 *     event { item, position, viewType ->
 *         root.setOnClickListener {
 *             Toast.makeText(context, "position = $position", Toast.LENGTH_SHORT).show()
 *         }
 *     }
 *     // 渲染Item数据
 *     render { item, position ->
 *         text.text = item
 *     }
 * }
 *
 * // 更新列表
 * viewModel.items.observe(viewLifecycleOwner) {
 *     recyclerView.reducer(it)
 * }
 * ```
 *
 * Dsl 的形式使用 RecyclerView ListAdapter
 * @author Ak
 * 2024/11/11 10:57
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderColumn(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    scope: RenderBuilder<D, VB>.() -> Unit
) {
    RenderBuilder<D, VB>()
        .renderInner(this, items, diffCallback, RecyclerView.VERTICAL, reverse, scope)
}

inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderColumn(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    renderBuilder: RenderBuilder<D, VB>
) {
    renderBuilder.renderInner(this, items, diffCallback, RecyclerView.VERTICAL, reverse)
}

/**
 * Simple:
 * ```
 * recyclerView.renderRow<String, FragmentHomeBinding> {
 *     setUp(emptyList(), diffCallback)
 *     // 创建Item内容
 *     content { parent, viewType ->
 *         FragmentHomeBinding.inflate(layoutInflater, parent, false)
 *     }
 *     // Item事件设置
 *     event { item, position, viewType ->
 *         root.setOnClickListener {
 *             Toast.makeText(context, "position = $position", Toast.LENGTH_SHORT).show()
 *         }
 *     }
 *     // 渲染Item数据
 *     render { item, position ->
 *         text.text = item
 *     }
 * }
 *
 * // 更新列表
 * viewModel.items.observe(viewLifecycleOwner) {
 *     recyclerView.reducer(it)
 * }
 * ```
 *
 * Dsl 的形式使用 RecyclerView ListAdapter
 */
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderRow(
    diffCallback: ItemCallback<D>,
    items: List<Any> = emptyList(),
    reverse: Boolean = false,
    scope: RenderBuilder<D, VB>.() -> Unit
) {
    RenderBuilder<D, VB>()
        .renderInner(this, items, diffCallback, RecyclerView.HORIZONTAL, reverse, scope)
}


inline fun <D, reified VB : ViewDataBinding> RenderBuilder<D, VB>.renderInner(
    recyclerView: RecyclerView,
    items: List<Any> = emptyList(),
    diffCallback: ItemCallback<D>,
    @Orientation orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false,
    scope: RenderBuilder<D, VB>.() -> Unit = {}
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
                        getBind<VB>().it(
                            { getItem(adapterPosition) },
                            { adapterPosition },
                            viewType
                        )
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


open class RenderBuilder<D, VB : ViewDataBinding> {
    var adapter: ListAdapter<D, ItemViewHolder>? = null

    var content: ((parent: ViewGroup, viewType: Int) -> VB)? = null
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