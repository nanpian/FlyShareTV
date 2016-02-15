package cn.fxdata.tv.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpStatus;

import cn.fxdata.tv.R;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import cn.fxdata.tv.service.DownloadTask;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadVideoManager {
    private static Context context = null;
    private String mSavePath;
    private String mRemotePath;
    public static final String DOWNLOAD_NOTIFY_PROGRESS = "download_progress_notify";
    public static final String DOWNLOAD_NOTIFY_INIT = "download_init_notify";
    public static final int DOWNLOAD = 1;
    /* 下载结束 */
    public static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_FAIL = 3;
    private static final int MSG_INIT_VIDEO = 4;
    private static final String ACTION_STOP_DOWNLOAD = "stop_downloa";
    private static final String ACTION_START_DOWNLOAD = "start_downloa";
    public static final String DOWNLOAD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/download/";
    // private Context mContext;
    private static Notification mNotification;
    private static NotificationManager mNotificationManager;
    private ProgressBar mProgress;
    private int mDownLoadProgress;
    private Timer mTimer;
    private static ArrayList<VideoItem> mDownItem = new ArrayList<VideoItem>();
    public static boolean isDownload = true;
    private static FlyShareTVdataHelper dbHelper;
    private static DownloadTask mDownload;

    public DownloadVideoManager(Context context) {
        this.context = context;
        dbHelper = new FlyShareTVdataHelper(context, "FlyShareTV.db3", null, 1);
    }

    // class InitThread extends Thread {

    // }

    public void addVideoToDownQuee(VideoItem item) {
        Log.d("renjun1", "item: " + item.getVideoUri());
        mDownItem.add(item);
        startDownload();
        // initNotification();
    }

    private void startDownload() {
        new initVideoThread(mDownItem.get(0)).start();
    }

    public void stopDownload() {
        Log.d("renjun1", "stopdownloa 1111111");
        mDownload.isDownload = false;
    }

    public class DownloadBinder extends Binder {
        private void startDownload() {

        }

        public void addVideoToDownQuee(VideoItem item) {

        }

    }

    // private void calculateTotallength() {

    // }

    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            // 正在下载
            case DOWNLOAD:
                Log.d("renjun1", "toast 111 begin");
                int mPro = (Integer) msg.obj;
                Log.d("renjun1", "mPro: " + mPro);
                Intent intent = new Intent(DOWNLOAD_NOTIFY_PROGRESS);
                mDownItem.get(0).setFinished(mPro);
                // Bundle bud = new Bundle();
                // bud.putSerializable("videoRes", mDownItem.get(0));
                intent.putExtra("download_progress", mPro);
                context.sendBroadcast(intent);
                // startForeground(1,mNotification);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                Log.d("renjun1", "下载完成 22222222");
                // Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG);
                // mNotificationManager.cancelAll();
                // stopForeground(true);
                break;
            // case DOWNLOAD_FAIL:
            // showLinkErrorDialog();
            // break;
            case MSG_INIT_VIDEO:
                VideoItem mVideo = (VideoItem) msg.obj;
                updateDatabase(mVideo);
                Intent mintent = new Intent(DOWNLOAD_NOTIFY_INIT);
                context.sendBroadcast(mintent);
                mDownload = new DownloadTask(context, mVideo);
                mDownload.download();
                mDownload.isDownload = true;
                break;
            default:
                break;
            }
        };
    };

    private class initVideoThread extends Thread {
        private VideoItem vitem = null;

        public initVideoThread(VideoItem item) {
            vitem = item;
        }

        @Override
        public void run() {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                mSavePath = sdpath + "download";
                int length = 0;
                int count = 0;
                String videoPath = vitem.getVideoUri();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(videoPath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == HttpStatus.SC_OK) {
                        length = conn.getContentLength();
                    }
                    if (length <= 0) {
                        return;
                    }
                    File dir = new File(DOWNLOAD_PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File(dir, "aa.mp4");
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.setLength(length);
                    vitem.setVideolength(length);
                    mHandler.obtainMessage(MSG_INIT_VIDEO, vitem)
                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }
    }

    protected static void updateDatabase(VideoItem mVideo) {
        // TODO Auto-generated method stub
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            Log.d("renjun1", "111111111");
            // SQLiteDatabase db = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,down_fin" }, "_uri=?",
                    new String[] { mVideo.getVideoUri() }, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                int index = mCursor.getColumnIndex("down_fin");
                if (index >= 0) {
                    int mfinish = mCursor.getInt(index);
                    if (mfinish == -1) {
                        ContentValues value = new ContentValues();
                        value.put("down_fin", 0);
                        dbHelper.update(value, mVideo.getVideoUri());
                    }
                }
            }

        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
