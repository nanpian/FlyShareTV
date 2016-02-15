package cn.fxdata.tv.download;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cn.fxdata.tv.utils.Log;

import android.R.integer;
import android.content.Context;

public class FxDownloadTaskManager {
    private static final String TAG = "FxDownloadTaskManager";
    private LinkedList<FxDownloadTask> downloadTasks;
    private Set<String> taskIdSet;
    private Context mContext;
    public static final String D_FLAG = "downLoadStatus";

    private static FxDownloadTaskManager downloadTaskMananger;

    private FxDownloadTaskManager(Context context) {
        downloadTasks = new LinkedList<FxDownloadTask>();
        taskIdSet = new HashSet<String>();
        mContext = context;
    }

    public static synchronized FxDownloadTaskManager getInstance(Context context) {
        if (downloadTaskMananger == null) {
            downloadTaskMananger = new FxDownloadTaskManager(context);
        }
        return downloadTaskMananger;
    }

    public boolean addDownloadTask(FxDownloadTask downloadTask) {
        synchronized (downloadTasks) {
            downloadTasks.addLast(downloadTask);
            return true;
        }
    }

    public boolean isTaskRepeat(String fileId) {
        synchronized (taskIdSet) {
            if (taskIdSet.contains(fileId)) {
                Log.d(TAG, "task repeat,the task fileId is " + fileId);
                return true;
            } else {
                Log.i(TAG, "add download stask " + fileId);
                taskIdSet.add(fileId);
                return false;
            }
        }
    }

    public FxDownloadTask getDownloadTask() {
        synchronized (downloadTasks) {
            if (downloadTasks.size() > 0) {
                Log.i(TAG, "add down load task " + "get download task");
                FxDownloadTask downloadTask = downloadTasks.removeFirst();
                return downloadTask;
            }
        }
        return null;
    }

    // 停止下载任务
    public void stopDownloadTask(String url) {
        synchronized (downloadTasks) {
            for (int i = 0; i < downloadTasks.size(); i++) {
                if (downloadTasks.get(i).getFileId().equals(url)) {
                    downloadTasks.get(i).pause();
                }
            }
        }
    }

    // 删除下载任务
    public void removeDownloadTask(String url) {
        for (int i = 0; i < downloadTasks.size(); i++) {
            if (downloadTasks.get(i).getFileId().equals(url)) {
                downloadTasks.get(i).delete(url);
            }
        }
    }
}
