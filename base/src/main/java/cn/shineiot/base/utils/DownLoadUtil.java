package cn.shineiot.base.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author zhang
 * @date 2016/3/24
 */

public class DownLoadUtil {

    private static DownLoadTask downLoadTask;
    private static Context mcontext;
    public static OkHttpClient okhttpclient = null;
    private static Boolean isDownFinish = false;

    public static OkHttpClient getOkHttp() {
        if (okhttpclient == null) {
            okhttpclient = new OkHttpClient();
        }
        return okhttpclient;
    }

    public static DownLoadTask getInstance(Context context) {
        mcontext = context;
        downLoadTask = new DownLoadTask();
        return downLoadTask;
    }

    public static class DownLoadTask extends AsyncTask<String, Integer, File> {

        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            myDialog = new ProgressDialog(mcontext);
            myDialog.setMessage("正在下载……");
            myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            myDialog.setProgress(100);
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();

        }

        @Override
        protected File doInBackground(String... params) {

            OkHttpClient okhttpClient = getOkHttp();
            Request request = new Request.Builder().url(params[0]).build();

            final String filePath = params[1];
            final String fileName = params[2];

            Response response;
            File result = null;
            try {
                response = okhttpClient.newCall(request).execute();
                if (response.isSuccessful()) {

                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    InputStream is;
                    FileOutputStream fos;
                    is = response.body().byteStream();
                    long file_length = response.body().contentLength();

                    if (file_length <= 0) {
                        return null;
                    }
                    result = new File(dir, fileName);
                    LogUtil.INSTANCE.e(result.getAbsolutePath());

                    int total_length = 0;
                    int len;
                    byte[] buf = new byte[1024];

                    fos = new FileOutputStream(result);
                    while ((len = is.read(buf)) != -1) {
                        total_length += len;
                        int progress_value = (int) ((total_length / (float) file_length) * 100);
                        fos.write(buf, 0, len);
                        publishProgress(progress_value);

                    }
                    fos.flush();

                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        fos.close();
                    } catch (IOException ignored) {
                    }
                } else {
                    LogUtil.INSTANCE.e(response.message());
                }

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int value = values[0];
            if(value == 100){
                isDownFinish = true;
            }
            myDialog.setProgress(value);
        }

        @Override
        protected void onPostExecute(File result) {
            myDialog.dismiss();
            if (null != result && isDownFinish ) {
                isDownFinish = false;
                ScreenUtil.INSTANCE.installApk(mcontext, result);
            }else if(null != result && result.exists()){
                LogUtil.INSTANCE.e("已删除_"+result.getAbsolutePath());
                result.delete();
            }
        }

    }
}

