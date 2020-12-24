package cn.shineiot.base.utils

import cn.shineiot.base.manager.DownProgressListener
import okhttp3.ResponseBody
import java.io.*

/**
 * 文件写入工具
 */
object FileUtil {

    fun writeToFile(
        body: ResponseBody,
        savePath: String,
        progressListener: DownProgressListener? = null
    ) {

        val index = savePath.lastIndexOf("/")
        val filePath = savePath.substring(0, index)
        val fileName = savePath.substring(index, savePath.length)

        val dir = File(filePath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file_length = body.contentLength()

        if (file_length <= 0) {
            return
        }
        var result: File? = null
        var inPutStream: InputStream? = null
        var fos: FileOutputStream? = null
        inPutStream = body.byteStream()
        result = File(dir, fileName)
        //LogUtil.INSTANCE.e(result.getAbsolutePath());
        var total_length = 0
        var len: Int
        val buf = ByteArray(1024)

        try {
            fos = FileOutputStream(result)
            while (inPutStream.read(buf).also { len = it } != -1) {
                total_length += len
                val progress = (total_length / file_length.toFloat() * 100).toInt()

                progressListener?.downProgress(progress)

                fos.write(buf, 0, len)
            }
            fos.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        try {
            inPutStream.close()
        } catch (ignored: IOException) {
            ignored.stackTrace
        }

        try {
            fos!!.close()
        } catch (ignored: IOException) {
            ignored.stackTrace
        }
    }

}