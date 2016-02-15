package cn.fxdata.tv.db;

import java.util.ArrayList;
import java.util.List;

import cn.fxdata.tv.download.DLVideoItem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownloadUtil {

    public static DownloadUtil mInstance;
    private Context mContext;
    private FlyShareTVdataHelper dbHelper;

    public DownloadUtil(Context context) {
        dbHelper = new FlyShareTVdataHelper(context);
    }

    public static DownloadUtil getInstance(Context mContext){
        if(mInstance==null)return new DownloadUtil(mContext);
        else return mInstance;
    }

    /**
     * 查看数据库中是否有数据
     */
    public boolean isHasInfors(String urlstr) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select count(*)  from download_info where url=?";
        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    /**
     * 保存 下载的具体信息
     */
    public void saveInfos(List<DLVideoItem> infos) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        for (DLVideoItem info : infos) {
            String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url,movieId,movieName) values (?,?,?,?,?)";
            Object[] bindArgs = { info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl() };
            database.execSQL(sql, bindArgs);
        }
    }

    /**
     * 保存 一条下载任务的具体信息
     */
    public void saveInfo(DLVideoItem info) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into download_info(start_pos, end_pos,compelete_size,url,movieId,movieName, downloadstatus ) values (?,?,?,?,?,?,?)";
        Object[] bindArgs = { info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl(), info.getMovie_id(), info.getMovie_name() , info.getDownload_status() };
        database.execSQL(sql, bindArgs);
    }

    /**
     * 得到某条下载任务的具体信息
     */
    public DLVideoItem getInfo(String urlstr) {
        DLVideoItem info = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select start_pos, end_pos,compelete_size,url,movieId,movieName, downloadstatus from download_info where url=?";
        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });

        while (cursor.moveToNext()) {
            info = new DLVideoItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
            break;
        }
        cursor.close();
        return info;
    }
    
    /**
     * 得到下载具体信息
     */
    public List<DLVideoItem> getInfos(String urlstr) {
        List<DLVideoItem> list = new ArrayList<DLVideoItem>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select start_pos, end_pos,compelete_size,url,movieId,movieName, downloadstatus from download_info where url=?";
        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
        while (cursor.moveToNext()) {
            cn.fxdata.tv.download.DLVideoItem info = new DLVideoItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getInt(6));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    
    /**
     * 得到下载具体信息
     */
    public List<DLVideoItem> getInfos() {
        List<DLVideoItem> list = new ArrayList<DLVideoItem>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select start_pos, end_pos,compelete_size,url,movieId,movieName, downloadstatus from download_info";
        Cursor cursor = database.rawQuery(sql, new String[] { });
        while (cursor.moveToNext()) {
            cn.fxdata.tv.download.DLVideoItem info = new DLVideoItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getInt(6));
            list.add(info);
        }
        cursor.close();
        return list;
    }
    /**
     * 更新数据库中的下载信息
     */
    public void updataInfos( int compeleteSize, String urlstr) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "update download_info set compelete_size=? where url=?";
        Object[] bindArgs = { compeleteSize, urlstr };
        database.execSQL(sql, bindArgs);
    }
    
    /**
     * 更新下载状态信息
     */
    public void updateDownloadStatusDb(String urlstr,int status ) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "update download_info set downloadstatus=? where url=?";
        Object[] bindArgs = { status, urlstr};
        database.execSQL(sql, bindArgs);
    }

    /**
     * 关闭数据库
     */
    public void closeDb() {
        dbHelper.close();
    }

    /**
     * 下载完成后删除数据库中的数据
     */
    public void delete(String url) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.delete("download_info", "url=?", new String[] { url });
        database.close();
    }

}
