package cn.shineiot.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import cn.shineiot.base.BaseApplication
import cn.shineiot.base.R
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.maning.mndialoglibrary.MProgressDialog
import com.maning.mndialoglibrary.config.MDialogConfig
import com.maning.mndialoglibrary.listeners.OnDialogDismissListener

object DialogUtil {
    private val mBuild = MDialogConfig.Builder()
    private var dialog: MaterialDialog? = null

    /**
     * loading
     */
    fun showLoading(
        context: Context ,
        content: String? = "加载中"
    ) {
        mBuild.isCancelable(true)
            .isCanceledOnTouchOutside(false)
        val mConfig = mBuild.build()
        MProgressDialog.showProgress(context, content, mConfig)
    }

    /**
     * @param onDialogDismissListener dialog关闭监听
     */
    fun showLoading(
        context: Context ,
        content: String,
        onDialogDismissListener: OnDialogDismissListener
    ) {
        mBuild.isCancelable(true)
            .isCanceledOnTouchOutside(false)
            .setOnDialogDismissListener(onDialogDismissListener)
        val mConfig = mBuild.build()
        MProgressDialog.showProgress(context, content, mConfig)
    }

    fun hideDialog() {
        MProgressDialog.dismissProgress()
    }

    /**
     * MaterialDialog
     *
     * cancelBack　取消回调
     * sureBack 确定回调
     * owner 是否绑定生命周期
     */
    fun showMaterialDialog(
        context: Context = BaseApplication.context(),
        msg: String,
        titleStr: String? = null,
        positiveBt: String? = "确定",
        negativeBt: String? = "取消",
        cancelBack: DialogCallback? = null,
        sureBack: DialogCallback? = null,
        drawable: Int? = 0
    ) {
        dialog = MaterialDialog(context).show {
            if (drawable != null && drawable > 0) {
                icon(drawable)
            }
            cancelOnTouchOutside(true)
            cancelable(true)
            message(0, msg)

            if (null != titleStr) {
                title(0, titleStr)
            }

            positiveButton(0, positiveBt) {
                if (null != sureBack) {
                    it.show(sureBack)
                }
            }
            negativeButton(0, negativeBt) {
                if (null != cancelBack) {
                    it.onCancel(cancelBack)
                }
            }
            onDismiss {
                dialog?.dismiss()
                dialog = null
            }
        }
    }

}