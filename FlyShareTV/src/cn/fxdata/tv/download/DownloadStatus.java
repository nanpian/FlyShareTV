package cn.fxdata.tv.download;

public class DownloadStatus {
    // 定义下载的状态：初始化状态，正在下载状态，暂停状态,下载失败
    public static final int INIT = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int FAILED = 4;
}
