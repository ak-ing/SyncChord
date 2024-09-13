package com.aking.base.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.aking.base.BR
import kotlin.properties.Delegates

/**
 * Created by Rick on 2023-08-04  16:46.<p>
 *
 * Description: 默认生成通用BR.item
 */
class BRItem<T> : BaseObservable() {
    @get:Bindable
    var item: T by Delegates.observable(item) { _, _, _ ->
        notifyPropertyChanged(BR.item)
    }
}


/**
 * Created by Rick on 2023-01-03  18:07.
 * Description: 通用ViewHolder
 */
open class CommonViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    val context: Context get() = itemView.context

    open fun <T> bind(
        item: T,
        itemClick: ((T) -> Unit)? = null,
        itemLongClick: ((T, View) -> Unit)? = null
    ) {
        binding.run {
            setVariable(BR.item, item)
            executePendingBindings()

            itemClick?.let {
                root.setOnClickListener { _ ->
                    it.invoke(item)
                }
            }
            itemLongClick?.let {
                root.setOnLongClickListener { _ ->
                    it.invoke(item, root)
                    return@setOnLongClickListener false
                }
            }
        }
    }

    /**
     * 获取ViewDataBinding
     */
    inline fun <reified T : ViewDataBinding> getBind(): T {
        return binding as T
    }

}

/**
 * 创建LayoutInflate
 */
fun View.inflater(): LayoutInflater = LayoutInflater.from(context)
