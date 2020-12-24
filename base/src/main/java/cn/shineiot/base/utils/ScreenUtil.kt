package cn.shineiot.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import cn.shineiot.base.BaseApplication
import java.io.File
import java.util.*

object ScreenUtil {

    /**
     * 获取设备号
     */
    @SuppressLint("HardwareIds")
    fun getImei(context: Context): String? {
        /*val telephonyManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var deviceId = telephonyManager.deviceId

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
        }
        return deviceId*/
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取App信息
     */
    fun getAppInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    /**
     * 打电话
     */
    fun callPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:${phone}")
        intent.data = data
        context.startActivity(intent)
    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * dp转px
     */
    fun px2dp(context: Context, px: Int): Float {
        val scale: Float = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }

    /**
     * 获取屏幕密度
     */
    fun getDensity(): Float {
        val dm = BaseApplication.context.resources.displayMetrics
        return dm.density
    }

    /**
     * 屏幕宽度
     */
    fun getScreenWidth(): Int {
        val dm = BaseApplication.context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 屏幕高度
     */
    fun getScreenHeight(): Int {
        val dm = BaseApplication.context.resources.displayMetrics
        return dm.heightPixels
    }


    /**
     * 跳转到应用详情页面
     */
    fun gotoAppDetailIntent(activity: Activity) {
        val intent = Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    /**
     * 安装APK
     */
    fun installApk(context: Context, file: File) {

        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    context, context.packageName + ".fileProvider", file
                ), "application/vnd.android.package-archive"
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    /**
     * GPS是否打开
     */
    fun isGpsOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 设置GPS
     */
    fun gotoSetGPS(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            intent.action = Settings.ACTION_SETTINGS
            context.startActivity(intent)
        }
    }

    /**
     * 获取手机厂商
     * @return  手机厂商
     */
    fun getDeviceBrand(): String {
        return Build.BRAND.toUpperCase(Locale.ROOT)
    }

}