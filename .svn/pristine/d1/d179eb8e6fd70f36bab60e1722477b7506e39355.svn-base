package cn.fxdata.tv.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpStatus;

import cn.fxdata.tv.R;
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
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

public class DownloadManagerService extends Service {
    private DownloadBinder binder = new DownloadBinder();
    private static Context context = null;
    private String mSavePath;
    private String mRemotePath;
    public static final int DOWNLOAD = 1;
    /* 下载结束 */
    public static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_FAIL = 3;
    private static final int MSG_INIT_VIDEO = 4;
    public static final String ACTION_STOP_DOWNLOAD = "stop_downloa";
    public static final String ACTION_START_DOWNLOAD = "start_downloa";
    public static final String DOWNLOAD_NOTIFY_PROGRESS = "download_progress_notify";
    public static final String DOWNLOAD_NOTIFY_INIT = "download_init_notify";
    public static final String DOWNLOAD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/download/";
    // private Context mContext;
    private static Notification mNotification;
    private static NotificationManager mNotificationManager;
    private ProgressBar mProgress;
    private int mDownLoadProgress;
    private Timer mTimer;
    private static ArrayList<VideoItem> mDownItem = new ArrayList<VideoItem>();
    private static Map<String, DownloadTask> mTasks = new HashMap<String, DownloadTask>(); 
    public static boolean isDownload = true;
    private static FlyShareTVdataHelper dbHelper;
    private static DownloadTask mDownload;

    public void setRemotePath(String path) {

        this.mRemotePath = path;
        Log.d("renjun1", "mRemotePath: " + mRemotePath);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.d("renjun1", "downloader open22222gjghjgjgjgj");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mTimer = new Timer();
        dbHelper = new FlyShareTVdataHelper(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STOP_DOWNLOAD);
        filter.addAction(ACTION_START_DOWNLOAD);
        registerReceiver(onClickReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("renjun1", "onstart commard 11111");
        // if(intent!= null){
        VideoItem item = (VideoItem) intent.getSerializableExtra("videoRes");
        addVideoToDownQuee(item);
        // }
        return Service.START_REDELIVER_INTENT;

    }

    // class InitThread extends Thread {

    // }

    public void addVideoToDownQuee(VideoItem item) {
        DownloadTask.isDownload = true;
        new initVideoThread(item).start();
    }

    private void startDownload() {
        DownloadTask.isDownload = true;
        new initVideoThread(mDownItem.get(0)).start();
    }

    public void stopDownload() {
        Log.d("renjun1", "stopdownloa 1111111");
        DownloadTask.isDownload = false;
    }

    public class DownloadBinder extends Binder {
        private void startDownload() {

        }

        public void addVideoToDownQuee(VideoItem item) {

        }

    }
    
    public void removeDownloadTask(VideoItem item){
        mTasks.remove(item.getMoveId());
    }

    @Override
    public void onDestroy() {
        Log.d("renjun1", "service destroy nnnnnnnnnnnnnn");

        super.onDestroy();

    }

    // private void calculateTotallength() {

    // }

    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            // 正在下载
            case DOWNLOAD:
                Log.d("renjun1", "toast 111 begin");
                VideoItem mv = (VideoItem)msg.obj;
               // int mPro = (Integer) mv.get("mPro");
                //String moveid = (String) mv.get("moveid");
                //Log.d("renjun1", "mPro: " + mPro);
                Intent intent = new Intent(DOWNLOAD_NOTIFY_PROGRESS);
                //mDownItem.get(0).setFinished(mPro);
                // Bundle bud = new Bundle();
                // bud.putSerializable("videoRes", mDownItem.get(0));
                Bundle bund = new Bundle();
                bund.putSerializable("download_progress", mv);
                intent.putExtras(bund);
                context.sendBroadcast(intent);
                // startForeground(1,mNotification);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                Log.d("renjun1", "下载完成 22222222");
                // Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG);
                mNotificationManager.cancelAll();
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
                mTasks.put(mVideo.getMoveId(),new DownloadTask(context,mVideo));
                mTasks.get(mVideo.getMoveId()).download();
                //DownloadTask.isDownload = true;
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
                        Log.d("renjun1","lengeth:"+length);
                    }
                    if (length <= 0) {
                        return;
                    }
                    File dir = new File(DOWNLOAD_PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File(dir, vitem.getfileName()+".mp4");
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
    /*
    private void initNotification() {
        int icon = R.drawable.vedio_download_bg;
        CharSequence tickerText = "";
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);

        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.download_notify);

        contentView.setTextViewText(R.id.download_file, "下载文件");
        contentView.setProgressBar(R.id.download_progress, 100, 0, false);
        Intent buttonIntent = new Intent(ACTION_STOP_DOWNLOAD);
        PendingIntent pendButtonIntent = PendingIntent.getBroadcast(this, 0,
                buttonIntent, 0);
        contentView.setOnClickPendingIntent(R.id.download_stop,
                pendButtonIntent);
        // R.id.trackname为你要监听按钮的id
        // 指定个性化视图
        mNotification.contentView = contentView;
    }
    */

    BroadcastReceiver onClickReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_STOP_DOWNLOAD)) {
                stopDownload();                    
            } else if(intent.getAction().equals(ACTION_START_DOWNLOAD)) {
                startDownload();
            }
        }
    };
    
    protected static void updateDatabase(VideoItem mVideo) {
        // TODO Auto-generated method stub
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            Log.d("renjun1", "111111111");
            // SQLiteDatabase db = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,down_fin" }, "_id=?",
                    new String[] { mVideo.getMoveId()}, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                int index = mCursor.getColumnIndex("down_fin");
                Log.d("renjun1","index: "+index);
                if (index >= 0) {
                    int mfinish = mCursor.getInt(index);
                    if (mfinish == -1) {
                        ContentValues value = new ContentValues();
                        value.put("down_fin", 0);
                        dbHelper.update(value, mVideo.getMoveId());
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
