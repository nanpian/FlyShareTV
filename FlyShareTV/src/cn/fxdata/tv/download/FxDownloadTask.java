package cn.fxdata.tv.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.fxdata.tv.db.DownloadUtil;
import cn.fxdata.tv.download.FxDownloadService.DownloadStatusListener;
import cn.fxdata.tv.utils.Log;
import android.R.bool;
import android.content.Context;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

public class FxDownloadTask implements Runnable {

    private static final String TAG = "FxDownloadTask";

    DLVideoItem dlVideoItem;
    private String localfile;// 保存路径
    private DownloadUtil dao; // 保存下载状态的数据库
    private int startPos;
    private int endPos;
    private int compeleteSize;
    private String moviename;
    private String movieid;
    // 下载视频的url地址，做唯一标识
    private String urlstr;
    // 下载状态
    private int downloadStatus;
    // 下载状态回调
    public DownloadStatusListener downloadListener;
    // 处理下载进度的handler
    private Handler mainHandler;

    public int state = DownloadStatus.INIT;
    private Context mcContext;
    // 所要下载的文件的大小
    private int fileSize;

    // 固定存放下载的音乐的路径：SD卡目录下
    private static final String SD_PATH = "/mnt/sdcard/";

    private Handler downloadHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String url = (String) msg.obj;
                int length = msg.arg1;
                if (downloadListener != null) {
                    Log.d(TAG, "Download status the length -->> " + length);
                    downloadListener.onUpdate(url,length);
                }
            }
        }
    };

    public FxDownloadTask(DLVideoItem dlVideoItem1, Context mContext, Handler mainHandler1) {
        dlVideoItem = dlVideoItem1;
        dao = DownloadUtil.getInstance(mContext);
        mainHandler = mainHandler1;
    }

    public FxDownloadTask(int startPos, int endPos, int compeleteSize, String urlstr, String moviename, String movieid, Context mContext, Handler mainHandler1) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.urlstr = urlstr;
        this.moviename = moviename;
        this.movieid = movieid;
        dao = new DownloadUtil(mContext);
        mainHandler = mainHandler1;
    }

    public FxDownloadTask(int startPos, int endPos, int compeleteSize, String urlstr, String moviename, String movieid, Context mContext, DownloadStatusListener statusInterface, int status) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.urlstr = urlstr;
        this.moviename = moviename;
        this.movieid = movieid;
        this.downloadListener = statusInterface;
        this.downloadStatus = status;
        this.mcContext = mContext;
        dao = new DownloadUtil(mContext);
    }

    /**
     * 获得下载任务的唯一标识
     * 
     * @return urlstr 下载地址url
     */
    public String getFileId() {
        return urlstr;
    }

    /**
     * 判断是否是第一次 下载
     */
    private boolean isFirst(String urlstr) {
        return dao.isHasInfors(urlstr);
    }

    public FxDownloadTask getDownloadTaskInfo(String urlString) {
        // 如果数据库不存在下载任务的信息，判定是第一次下载
        if (isFirst(urlString)) {
            Log.d(TAG, "isFirst");
            boolean successfullyInited = initFirstTimeDownloadTask();
            if (!successfullyInited)
                return null;
            // 保存infos中的数据到数据库
            DLVideoItem dlVideoItemTmpDlVideoItem = new DLVideoItem(0, fileSize, 0, urlString, movieid, moviename,DownloadStatus.INIT);
            dao.saveInfo(dlVideoItemTmpDlVideoItem);
            FxDownloadTask fxDownloadTask = new FxDownloadTask(0, fileSize, 0, urlString, urlString, urlString, mcContext, null, DownloadStatus.INIT);

            return fxDownloadTask;
        } else {
            // 否则说明有下载任务的信息，从数据库中读取相关信息
            // 得到数据库中已有的urlstr的下载任务的具体信息
            DLVideoItem info = dao.getInfo(urlstr);
            int size = 0;
            int compeleteSize = 0;
            size = info.getEndPos() - info.getStartPos() + 1;
            compeleteSize = info.getCompeleteSize();
            Log.d(TAG, "not first, size -->> " + size + " complete size -->> " + compeleteSize);
            FxDownloadTask fxDownloadTask = new FxDownloadTask(0, fileSize, compeleteSize, urlString, urlString, urlString, mcContext, null, info.getDownload_status());
            return fxDownloadTask;
        }
    }

    /**
     * 初始化下载任务
     */
    private boolean initFirstTimeDownloadTask() {
        try {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            fileSize = connection.getContentLength();
            localfile = SD_PATH + moviename;
            File file = new File(localfile);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 本地访问文件
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(fileSize);
            accessFile.close();
            connection.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        HttpURLConnection connection = null;
        RandomAccessFile randomAccessFile = null;
        InputStream is = null;

        try {
            // 设置任务状态为下载中
            state = DownloadStatus.DOWNLOADING;
            dao.updateDownloadStatusDb(urlstr, state);
            URL url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            // 设置范围，格式为Range：bytes x-y;
            connection.setRequestProperty("Range", "bytes=" + (startPos + compeleteSize) + "-" + endPos);

            randomAccessFile = new RandomAccessFile(localfile, "rwd");
            randomAccessFile.seek(startPos + compeleteSize);
            Log.d(TAG, "connection--->>>" + connection);
            // 将要下载的文件写到保存在保存路径下的文件中
            is = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int length = -1;
            long time = System.currentTimeMillis();
            while ((length = is.read(buffer)) != -1) {
                Log.d(TAG, "downloading-----------------" + length);
                randomAccessFile.write(buffer, 0, length);
                compeleteSize += length;
                // 每0.5s发送一次更新信息
                if(System.currentTimeMillis()-time>500) {
                    // 更新数据库中的下载信息
                    dao.updataInfos(compeleteSize, urlstr);
                    // 用消息将下载信息传给进度条，对进度条进行更新
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = urlstr;
                    message.arg1 = length;
                    mainHandler.sendMessage(message);
                    time = System.currentTimeMillis();
                }
                if (state == DownloadStatus.PAUSE) {
                    dao.updateDownloadStatusDb(urlstr, state);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            state = DownloadStatus.FAILED;
            dao.updateDownloadStatusDb(urlstr, state);
        } finally {
            try {
                is.close();
                randomAccessFile.close();
                connection.disconnect();
                dao.closeDb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 删除数据库中urlstr对应的下载器信息
    public void delete(String urlstr) {
        dao.delete(urlstr);
    }

    // 设置暂停
    public void pause() {
        state = DownloadStatus.PAUSE;
    }

    // 重置下载状态
    public void reset() {
        state = DownloadStatus.INIT;
    }

    /**
     * 判断是否正在下载
     */
    public boolean isdownloading() {
        return state == DownloadStatus.DOWNLOADING;
    }

}
