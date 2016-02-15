package cn.fxdata.tv.db;

import java.util.concurrent.atomic.AtomicInteger;

import cn.sharesdk.tencent.qq.m;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavDbUtil {

    private static FavDbUtil mInstance;
    private Context mContext;
    private FlyShareTVdataHelper mSQLHelp;
    public SQLiteDatabase mSQLiteDatabase;
    
    // 多线程问题解决
    private AtomicInteger mOpenCounter = new AtomicInteger();  
    
    // 打开数据连接
    public synchronized SQLiteDatabase getDatabase() {  
        if(mOpenCounter.incrementAndGet() == 1) {  
            mSQLiteDatabase =  mSQLHelp.getWritableDatabase();
        }
        return mSQLiteDatabase;
    }

    // 关闭数据库连接
    public void closeDataBase(){
        if(mOpenCounter.decrementAndGet() == 0) {  
            // Closing database  
            mSQLiteDatabase.close();  
        } 
    }

    private FavDbUtil(Context context) {
        try {
            mContext = context;
            mSQLHelp = new FlyShareTVdataHelper(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库操作Util单例类
     */
    public static synchronized FavDbUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FavDbUtil(context);
        }
        return mInstance;
    }

    /**
     * 关闭数据库
     */
    public synchronized void close() {
        mSQLHelp.close();
        mSQLHelp = null;
        mInstance = null;
        
        if (mSQLiteDatabase.isOpen() ) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
        
        if (mSQLiteDatabase!=null){
            mSQLiteDatabase = null;
        }
    }

    /**
     * 
     * 向收藏视频数据库添加内容
     * 
     * @param movieid
     *            影片id
     * @param moviename
     *            影片名称
     * @param movieTime
     *            影片发布时间
     * @param moviePraiseTimes
     *            影片点赞次数
     * @param movieImageUrl
     *            影片图片url
     */
    public void addUpdateFavVideo(String movieid, String moviename,
            String movieTime, String moviePraiseTimes, String movieImageUrl) {

        Cursor c = null;
        ContentValues values = new ContentValues();
        values.put(FlyShareTVdataHelper.MOVIE_ID, movieid);
        values.put(FlyShareTVdataHelper.MOVIE_NAME, moviename);
        values.put(FlyShareTVdataHelper.MOVIE_TIME, movieTime);
        values.put(FlyShareTVdataHelper.MOVIE_PLAY_TIMES, moviePraiseTimes);
        try {
            String selection = FlyShareTVdataHelper.MOVIE_ID + "=?";
            String[] selectionArgs = new String[] { movieid };
            mSQLiteDatabase = getDatabase();
            c = mSQLiteDatabase.query(FlyShareTVdataHelper.TBL_FAV_VIDEOS,
                    null, selection, selectionArgs, null, null, null);
            if (c != null && c.getCount() > 0) {
                mSQLiteDatabase.update(FlyShareTVdataHelper.TBL_FAV_VIDEOS,
                        values, selection, selectionArgs);
            } else {
                mSQLiteDatabase.insert(FlyShareTVdataHelper.TBL_FAV_VIDEOS,
                        null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        closeDataBase();
    }

    /**
     * 判断是否是收藏的视频
     * 
     * @param movieId
     * @return
     */
    public boolean isFavVideo(String movieId) {
        
        Cursor c = null;
        try {
            String selection = FlyShareTVdataHelper.MOVIE_ID + "=?";
            String[] selectionArgs = new String[] { movieId };
            mSQLiteDatabase = getDatabase();
            c = mSQLiteDatabase.query(FlyShareTVdataHelper.TBL_FAV_VIDEOS,
                    null, selection, selectionArgs, null, null, null);
            if (c != null && c.getCount() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            closeDataBase();
        }
    }

    /**
     * 删除movie id对应的收藏视频数据
     */
    public void removeFavVideo(String movieid) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FlyShareTVdataHelper.MOVIE_ID, movieid);
            String whereArgs = FlyShareTVdataHelper.MOVIE_ID + " =?";
            String[] whereValue = { movieid + "" };
            mSQLiteDatabase = getDatabase();
            mSQLiteDatabase.delete(FlyShareTVdataHelper.TBL_FAV_VIDEOS,
                    whereArgs, whereValue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDataBase();
        }
    }

}
