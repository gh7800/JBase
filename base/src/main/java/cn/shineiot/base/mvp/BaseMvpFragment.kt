@file:Suppress("UNCHECKED_CAST")

package cn.shineiot.base.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import cn.shineiot.base.utils.LogUtil


abstract class BaseMvpFragment<V : BaseView, T : BasePresenter<V>> : Fragment() {

    lateinit var mContext: Context
    lateinit var presenter: T

    var isInitialization: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            lazyLoad()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.e(this.javaClass.simpleName+"---onViewCreated")

        initP()
        initView()
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e("${this.javaClass.simpleName}----onResume")
        if (!isInitialization) {
            isInitialization = true
            lazyLoad()
        }
    }

    /**
     * 初始化presenter
     */
    abstract fun initPresenter(): T

    private fun initP() {
        presenter = initPresenter()
        presenter.attachView(this as V)
    }


    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("${this.javaClass.simpleName}----onDestroy")
        presenter.detachView()
    }

}
