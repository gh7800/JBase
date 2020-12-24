package cn.shineiot.base.manager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import cn.shineiot.base.utils.LogUtil
import java.io.File

object FileManger {

    //App包下文件路径
    fun getPackagePath(context: Context):String{
        val file  = context.getExternalFilesDir("")
        if(!file?.exists()!!){
            file.mkdirs()
        }
        return file.absolutePath
    }

    //SD卡根路径
    fun getSdcardPath(path:String):String{
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun deleteFile(file: File?) :Boolean{
        if(file == null){
            return false
        }
        if(!file.isDirectory){
            file.delete()
        }else{
            if(null != file.listFiles()) {
                file.listFiles().forEach {
                    deleteFile(it)
                }
            }
        }
        return true
    }


    //文件夹大小
    fun getFileSize(file: File?) :Long{
        if (file == null) return -1
        var size : Long = 0
        if(!file.isDirectory){
            size = file.length()
        }else{
            if(null != file.listFiles() && file.listFiles().size > 0) {
                file.listFiles().forEach {
                    size += getFileSize(it)
                }
            }
        }
        return size
    }

    private const val ONE_KB_SIZE = 1024
    private const val ONE_MB_SIZE = 1024 * 1024
    private const val ONE_GB_SIZE = 1024 * 1024 * 1024

    private const val BT_TAG = "B"
    private const val KB_TAG = "K"
    private const val MB_TAG = "M"
    private const val GB_TAG = "G"

    // 把文件大小转化成容易理解的格式显示，如多少M
    @SuppressLint("DefaultLocale")
    fun sizeToString(size: Double): String? {
        var str = ""
        str = if (size >= 0 && size < ONE_KB_SIZE) {
            size.toInt().toString() + BT_TAG
        } else if (size >= ONE_KB_SIZE && size < ONE_MB_SIZE) {
            java.lang.String.format("%.1f", size / ONE_KB_SIZE) + KB_TAG
        } else if (size >= ONE_MB_SIZE && size < ONE_GB_SIZE) {
            java.lang.String.format("%.1f", size / ONE_MB_SIZE) + MB_TAG
        } else {
            java.lang.String.format("%.1f", size / ONE_GB_SIZE) + GB_TAG
        }
        return str
    }
}