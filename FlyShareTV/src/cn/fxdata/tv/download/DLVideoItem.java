package cn.fxdata.tv.download;

public class DLVideoItem {
    private int threadId;// 下载器id
    private int startPos;// 开始点
    private int endPos;// 结束点
    private int compeleteSize;// 完成度
    private String url;// 下载器网络标识
    private String movie_id; // 下载的视频id
    private String movie_name; // 下载的视频名称
    private int download_status; // 下载的状态信息

    public DLVideoItem(int threadId, int startPos, int endPos, int compeleteSize, String url, String movieid,String moviename) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
        this.movie_name = moviename;
        this.movie_id = movieid;
    }
    
    public DLVideoItem(int startPos, int endPos, int compeleteSize, String url, String movieid,String moviename, int status) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
        this.movie_name = moviename;
        this.movie_id = movieid;
        this.download_status = status;
    }

    public DLVideoItem() {

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getCompeleteSize() {
        return compeleteSize;
    }

    public void setCompeleteSize(int compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public int getDownload_status() {
        return download_status;
    }

    public void setDownload_status(int download_status) {
        this.download_status = download_status;
    }

    @Override
    public String toString() {
        return "DownloadInfo [threadId=" + threadId + ", startPos=" + startPos + ", endPos=" + endPos + ", compeleteSize=" + compeleteSize + ", moviename =" + movie_name + ", movieid = "
                + movie_id + "]";
    }
}