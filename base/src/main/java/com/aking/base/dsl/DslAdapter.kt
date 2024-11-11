package com.aking.base.dsl

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


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
inline fun <D, reified VB : ViewDataBinding> RecyclerView.renderRow(
    scope: RowRender<D, VB>.() -> Unit
) {
    layoutManager = LinearLayoutManager(context)

    RowRender<D, VB>(
        reducer = { reducer(it) },
        diffSetup = { diff, render ->
            adapter = object : ListAdapter<D, ItemViewHolder>(diff) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                    return ItemViewHolder(render.content(parent, viewType)).apply {
                        render.run {
                            getBind<VB>().event(
                                { getItem(adapterPosition) },
                                { adapterPosition },
                                viewType
                            )
                        }
                    }
                }

                override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                    render.run {
                        holder.getBind<VB>().render(getItem(position), position)
                    }
                }
            }
        }
    ).scope()
}


class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 获取ViewDataBinding
     */
    inline fun <reified T : ViewDataBinding> getBind(): T {
        return binding as T
    }
}


class RowRender<D, VB : ViewDataBinding>(
    private val reducer: (items: List<Any>) -> Unit,
    private val diffSetup: (diff: ItemCallback<D>, render: RowRender<D, VB>) -> Unit
) {
    lateinit var content: (parent: ViewGroup, viewType: Int) -> VB
    lateinit var event: VB.(item: () -> D, position: () -> Int, viewType: Int) -> Unit
    lateinit var render: VB.(item: D, position: Int) -> Unit

    fun setUp(items: List<Any>, diffCallback: ItemCallback<D>) {
        diffSetup(diffCallback, this)
        reducer(items)
    }

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