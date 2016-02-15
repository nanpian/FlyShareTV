package cn.fxdata.tv.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.fxdata.tv.utils.Log;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class FxDownloadService {

    private static final String TAG = "FxDownloadService";
    private Context mContext;
    // 下载管理线程
    private Thread managerThread;

    public DownloadTaskManagerRunnable downloadTaskManagerRunnable;
    
    public class DownloadTaskManagerRunnable implements Runnable {
        private ExecutorService pool;
        private final int POOL_SIZE = 5;
        private final int SLEEP_TIME = 200;
        // Flag to indicator if it needs to stop the manager thread.
        private boolean isStop = false;
        public FxDownloadTaskManager downloadTaskManager;

        public DownloadTaskManagerRunnable() {
            downloadTaskManager = FxDownloadTaskManager.getInstance(mContext);
            pool = Executors.newFixedThreadPool(POOL_SIZE);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!isStop) {
                FxDownloadTask downloadTask = downloadTaskManager.getDownloadTask();

                if (downloadTask != null) {
                     // 如果此下载任务的状态是running，则不需要执行此条任务
                    if (downloadTask.getFileId()!=null) {
                        if (downloadTask.state == DownloadStatus.DOWNLOADING)
                            return;
                    }
                    pool.execute(downloadTask);
                } else {
                    try {
                        isStop = true;
                        Log.d(TAG,"The thread controll is -->> " + isStop);
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            if (isStop) {
                pool.shutdown();
            }

        }

        /**
         * @param isStop the isStop to set
         */
        public void setStop(boolean isStop) {
            this.isStop = isStop;
        }

    }

    public interface DownloadStatusListener {
        public void onUpdate(String url,int length);
    }

    private DownloadStatusListener downloadListener;

    public void setDownloadListener(DownloadStatusListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    private static FxDownloadService downloadService;

    public static FxDownloadService getService(Context mContext) {
        if (downloadService == null) {
            downloadService = new FxDownloadService();
            mContext = mContext;
        }
        return downloadService;
    }
    
    /**
     * 启动一个管理线程，管理所有的下载线程
     */
    public void startManagerThread(){
        if (managerThread == null || !managerThread.isAlive()) {
            Log.d(TAG, "manager thread is null ");
            downloadTaskManagerRunnable = new DownloadTaskManagerRunnable();
            Log.i(TAG, "start up schedule manager thread!");
            managerThread = new Thread(downloadTaskManagerRunnable);
            managerThread.start();
        }
    }

}
