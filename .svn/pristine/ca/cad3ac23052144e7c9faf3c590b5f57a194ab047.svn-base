package cn.fxdata.tv.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

import org.apache.http.HttpStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import cn.fxdata.tv.bean.DownloadVideoManager;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import cn.fxdata.tv.bean.VideoItem;

public class DownloadTask {
    private Context mContext = null;
    private VideoItem mVideo = null;
    private int mFinished = 0;
    // public boolean isDownload = true;
    private FlyShareTVdataHelper dbHelper;
    private int mDownLoadProgress = 0;
    public static boolean isDownload = true;

    public DownloadTask(Context context, VideoItem item) {
        mContext = context;
        mVideo = item;
        dbHelper = new FlyShareTVdataHelper(mContext);
    }

    public void download() {
        new downloadVideoThread(mVideo).start();
    }

    private class downloadVideoThread extends Thread {
        private VideoItem mItem = null;

        public downloadVideoThread(VideoItem item) {
            mItem = item;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            InputStream is = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mVideo.getVideoUri());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int start = getFinished(mVideo.getVideoUri());
                mFinished = start;
                Log.d("renjun1", "start: " + start);
                conn.setRequestProperty("Range", "bytes=" + start + "-"
                        + mVideo.getVideoLast());
                File file = new File(DownloadManagerService.DOWNLOAD_PATH,
                        mVideo.getfileName()+".mp4");
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    is = conn.getInputStream();
                    byte buf[] = new byte[1024 * 4];
                    int numread = -1;
                    long time = System.currentTimeMillis();
                    while ((numread = is.read(buf)) != -1) {
                        raf.write(buf, 0, numread);
                        mFinished += numread;
                        mDownLoadProgress = (int) (((float) mFinished / mVideo
                                .getVideolength()) * 100);
                        if (System.currentTimeMillis() - time > 500) {
                            Log.d("renjun1", "sendmessage");
                            mVideo.setmProgress(mDownLoadProgress);
                            DownloadManagerService.mHandler
                            .obtainMessage(DownloadManagerService.DOWNLOAD, mVideo).sendToTarget();
                            time = System.currentTimeMillis();
                        }
                        if (!isDownload) {
                            stopAndSaveSql(mVideo.getMoveId(), mFinished);
                            return;
                        }

                        Log.d("renjun1", "mDownLoadProgress: "
                                + mDownLoadProgress);
                    }
                }

            } catch (MalformedURLException e) {
                Log.d("renjun1", "xcvvxvxvxvxv");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("renjun1", "999999999999999999999");
                e.printStackTrace();
            } finally {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        raf = null;
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        is = null;
                    }
                }
            }
            Log.d("renjun1", "5686786868");
            DownloadManagerService.mHandler
                    .sendEmptyMessage(DownloadManagerService.DOWNLOAD_FINISH);
        }
    };

    public int getFinished(String moveId) {
        // TODO Auto-generated method stub
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        int mFinished = 0;
        try {
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,down_fin" }, "_id=?",
                    new String[] { moveId }, null, null, null);
            if (mCursor.moveToFirst()) {
                int com = mCursor.getColumnIndex("down_fin");
                Log.d("renjun1", "com: " + com);
                if (com >= 0) {
                    mFinished = mCursor.getInt(com);
                }
            }
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return mFinished;
    }

    public void stopAndSaveSql(String moveId, int finished) {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put("down_fin", finished);
        dbHelper.update(values, moveId);
    }

}
