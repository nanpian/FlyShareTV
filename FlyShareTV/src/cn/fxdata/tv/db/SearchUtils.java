package cn.fxdata.tv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public class SearchUtils {
    public static SearchUtils mSeachUtils;
    private Context mContext;
    private SQLiteHelper mSqliteHelper;
    private SQLiteDatabase mSqLiteDatabase;

    private SearchUtils(Context context) {
        this.mContext = context;
        this.mSqliteHelper = new SQLiteHelper(context);
        mSqLiteDatabase = mSqliteHelper.getWritableDatabase();
    }

    public static SearchUtils getInstance(Context context) {
        if (mSeachUtils == null) {
            mSeachUtils = new SearchUtils(context);
        }
        return mSeachUtils;
    }

    public void insertSearchHis(ContentValues contentValues) {
        mSqLiteDatabase.insert(SQLiteHelper.TABLE_SEARCH, null, contentValues);
    }

    public Cursor querySearchHis(String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, String limit) {
        Cursor cursor = mSqLiteDatabase.query(SQLiteHelper.TABLE_SEARCH,
                columns, selection, selectionArgs, groupBy, having, orderBy,
                limit);
        return cursor;
    }

    public void updateSearchHis(ContentValues values, String whereClause,
            String[] whereArgs) {
        mSqLiteDatabase.update(SQLiteHelper.TABLE_SEARCH, values, whereClause,
                whereArgs);
    }

    public void deleteSearchHis(String whereClause, String[] whereArgs) {
        mSqLiteDatabase.delete(SQLiteHelper.TABLE_SEARCH, whereClause,
                whereArgs);
    }

    public void updateSearchHisData(ContentValues contentValues) {
        String name = (String) contentValues.get(SQLiteHelper.SEARCH_NAME);
        boolean isIndb = false;
        if (!TextUtils.isEmpty(name)) {
            String selection = SQLiteHelper.SEARCH_NAME + " = ?";
            String[] selectionArgs = new String[] { name };
            Cursor cursor = querySearchHis(null, selection, selectionArgs,
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                isIndb = true;
            }

            if (isIndb) {
                // 如果包含，更新数据
                updateSearchHis(contentValues, selection, selectionArgs);
            } else {
                // 不包含，插入
                insertSearchHis(contentValues);
            }
        }
    }

    public int getNumberByName(String name) {
        int number = -1;
        if (!TextUtils.isEmpty(name)) {
            String selection = SQLiteHelper.SEARCH_NAME + " = ?";
            String[] selectionArgs = new String[] { name };
            Cursor cursor = querySearchHis(null, selection, selectionArgs,
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                number = cursor.getInt(cursor
                        .getColumnIndex(SQLiteHelper.SEARCH_NUMBER));
            }
        }
        return number;
    }
}
