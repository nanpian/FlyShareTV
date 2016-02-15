package com.zgntech.core.manager;



import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.FileDownloader;
import com.zgntech.core.util.DebugLog;
import com.zgntech.core.util.SystemTool;
import com.zgntech.core.util.T;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @description：TODO<软件更新下载>
 * @author：lyu
 * @date:2015-6-6
 * @version：V1.0
 */
public class UpdateManager {
    public static boolean isUpdating = false;

    private Activity mContext;
    // 是否取消更新
    private boolean cancelUpdate = false;
    private DownloadListener downListener;

    // 下载保存路径
    private String mSavePath;
    // 下载保存文件名
    private String mSaveName;
    // 下载路径
    private String mUpdateUrl;

    private FileDownloader mDownloder;
    private FileDownloader.DownloadController controller;
    private ProgressDialog xh_pDialog;
    private VolleyTools tools;

    public UpdateManager(Activity mContext) {
        super();
        this.mContext = mContext;
        this.tools = VolleyTools.getInstance(mContext);
        this.mDownloder = new FileDownloader(tools.requestQueue, 1);
    }

    public void setDownListener(DownloadListener downListener) {
        this.downListener = downListener;
    }

    public void downloadStop() {
        mDownloder.clearAll();
    }


    public void downloadApk() {
        xh_pDialog = new ProgressDialog(mContext);
        xh_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        xh_pDialog.setIndeterminate(false);
        xh_pDialog.setProgress(100);
        xh_pDialog.setCancelable(true);
        // 获取下载文件存放路径 和 文件名
        // String str = Environment.DIRECTORY_DOWNLOADS;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mSavePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            if (mSavePath == null || mSavePath.equals("")) {
                mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                        + "FlyShare" + "/" + "download";
            }
            String filePath = mSavePath + "/" + mSaveName;
            mDownloder.add(filePath, mUpdateUrl, new Listener<Void>() {

                @Override
                public void onPreExecute() {
                    // TODO Auto-generated method stub
                    super.onPreExecute();
                    xh_pDialog.show();
                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    super.onFinish();
                    DebugLog.d("onFinish");
                    xh_pDialog.dismiss();
                    installApk();
                }

                @Override
                public void onError(VolleyError error) {
                    // TODO Auto-generated method stub
                    super.onError(error);
                    if (downListener != null) {
                        downListener.onDownloadError();
                    }
                }

                @Override
                public void onProgressChange(long fileSize, long downloadedSize) {
                    // TODO Auto-generated method stub
                    super.onProgressChange(fileSize, downloadedSize);
                    xh_pDialog.setProgress((int) (downloadedSize * 100 / fileSize));
                    DebugLog.d("onProgressChange" + fileSize + "-" + downloadedSize + ";"
                            + (int) (downloadedSize * 100 / fileSize));

                }

                @Override
                public void onSuccess(Void response) {
                    // TODO Auto-generated method stub
                    DebugLog.d("onSuccess");
                    xh_pDialog.dismiss();
                }
            });
        }
    }

    /**
     * 安装APK
     */
    private void installApk() {
        // downListener.onDownloadSuccess();
        File apk = new File(mSavePath, mSaveName);
        if (!apk.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        SystemTool.installApk(mContext, apk);
    }

    public interface DownloadListener {

        public void onDownloadCancel();

        public void onDownloadError();

        public void onDownloadSuccess();
    }
}
