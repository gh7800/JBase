package cn.shineiot.base.utils

import android.Manifest.permission
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresPermission
import cn.shineiot.base.BaseApplication.Companion.context
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author GF63
 */
object NetworkUtil {
    private const val TIMEOUT = 3000 // TIMEOUT

    /**
     * 网络是否连接
     */
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isConnected
        }
        return false
    }

    /**
     * ping百度是否能通
     */
    fun pingNetWork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL("https://www.baidu.com")
                .openConnection() as HttpURLConnection
            httpUrl.connectTimeout = TIMEOUT
            httpUrl.connect()
            result = true
        } catch (e: IOException) {
        } finally {
            if (null !== httpUrl) {
                httpUrl.disconnect()
            }
        }
        return result
    }

    fun isWifiConnected(): Boolean {

        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWiFiNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable
        }

        return false
    }

    fun isMobileConnected(): Boolean {

        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
        return false
    }

    fun getConnectedType(): Int {

            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
                return mNetworkInfo.type
            }

        return -1
    }

}