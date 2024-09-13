package com.aking.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import timber.log.Timber

/**
 * Created by Rick on 2023-07-08  23:10
 * Description:
 */
abstract class BaseLifecycleFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var lifecycleLog = false

    /**
     * 生命周期日志开关
     */
    protected open fun lifecycleLogEnable(enable: Boolean) {
        lifecycleLog = enable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (lifecycleLog) Timber.v("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (lifecycleLog) Timber.v("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (lifecycleLog) Timber.v("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lifecycleLog) Timber.v("onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        if (lifecycleLog) Timber.v("onStart")
    }

    override fun onResume() {
        super.onResume()
        if (lifecycleLog) Timber.v("onResume")
    }

    override fun onPause() {
        super.onPause()
        if (lifecycleLog) Timber.v("onPause")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (lifecycleLog) Timber.v("onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        if (lifecycleLog) Timber.v("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (lifecycleLog) Timber.v("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (lifecycleLog) Timber.v("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        if (lifecycleLog) Timber.v("onDetach")
    }

}