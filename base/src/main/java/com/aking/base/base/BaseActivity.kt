package com.aking.base.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import com.aking.base.extended.contentView
import java.util.function.Function

/**
 * Created by Rick on 2023-01-30  19:24.
 * Description:
 */
abstract class BaseActivity<V : ViewDataBinding>(inflater: Function<LayoutInflater, V>) : BaseLifecycleActivity() {

    protected val binding: V by contentView(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.initView()
        binding.initData()
    }

    /**
     * 绑定布局变量
     */
    @MainThread
    fun bindVariables(vararg args: Pair<Int, Any>) {
        for (arg in args) {
            binding.setVariable(arg.first, arg.second)
        }
        binding.executePendingBindings()
    }

    protected open fun V.initView() {}
    protected open fun V.initData() {}

}
