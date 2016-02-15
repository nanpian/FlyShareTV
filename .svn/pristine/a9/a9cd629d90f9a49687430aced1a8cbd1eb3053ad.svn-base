package cn.fxdata.tv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fly_sharetv.db";
    private static final int VERSION = 1;
    public static final String TABLE_SEARCH = "search_history";
    public static final String TABLE_SEARCH_HOT = "search_hot";
    //
    public static final String SEARCH_NAME = "search_name";
    public static final String SEARCH_NUMBER = "search_number";
    public static final String SEARCH_TIME = "search_time";

    public static final String SEARCH_HOT_NAME = "search_hot_name";

    private Context mContext;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_search = "create table if not exists " + TABLE_SEARCH
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + SEARCH_NAME
                + " TEXT, " + SEARCH_NUMBER + " INTEGER, " + SEARCH_TIME
                + " TEXT)";
        String sql_search_hot = "create table if not exists "
                + TABLE_SEARCH_HOT + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SEARCH_HOT_NAME + " TEXT)";

        db.execSQL(sql_search);
        db.execSQL(sql_search_hot);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HOT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
        onCreate(db);
    }
}
