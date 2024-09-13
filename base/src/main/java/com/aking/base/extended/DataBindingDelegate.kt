package com.aking.base.extended

/**
 * Created by AK on 2024-03-28.
 * Description: DataBinding拓展
 */
import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.function.Function
import kotlin.reflect.KProperty

/**
 *  Activity初始化DataBinding的拓展，自动调用 [Activity.setContentView]
 *
 *  ```
 *  sample:
 *  private val binding by contentView(ActivityMainBinding::inflate)
 *  ```
 */
fun <A : AppCompatActivity, T : ViewDataBinding> AppCompatActivity.contentView(
    inflater: Function<LayoutInflater, T>
): ContentDataBindingDelegate<A, T> = ContentDataBindingDelegate(inflater)

/**
 * Fragment初始化DataBinding的拓展，返回binding,自动销毁
 */
fun <F : Fragment, T : ViewDataBinding> Fragment.binding(): FragmentDataBindingDelegate<F, T> =
    FragmentDataBindingDelegate()


/**
 * 延迟扩展数据绑定布局，调用 [Activity.setContentView]、设置生命周期所有者并返回绑定的委托。
 */
class ContentDataBindingDelegate<in A : AppCompatActivity, out T : ViewDataBinding>(
    private val inflater: Function<LayoutInflater, T>
) {

    private lateinit var binding: T

    operator fun getValue(activity: A, property: KProperty<*>): T {
        if (this::binding.isInitialized) {
            return binding
        }
        binding = inflater.apply(activity.layoutInflater)
        binding.lifecycleOwner = activity
        return binding
    }
}


/**
 * 绑定fragment布局View，设置生命周期所有者并返回binding,自动销毁
 */
class FragmentDataBindingDelegate<in F : Fragment, out T : ViewDataBinding> {

    private var binding: T? = null

    operator fun getValue(fragment: F, property: KProperty<*>): T {
        binding?.let { return it }

        val view = checkNotNull(fragment.view) {
            "The view of the fragment has not been initialized or has been destroyed!"
        }

        binding = DataBindingUtil.bind<T>(view)?.also {
            it.lifecycleOwner = fragment.viewLifecycleOwner
        }

        fragment.parentFragmentManager.registerFragmentLifecycleCallbacks(Clear(fragment), false)

        return requireNotNull(binding)
    }

    inner class Clear(private val thisRef: F) : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if (thisRef === f) {
                binding?.unbind()
                binding = null
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

}