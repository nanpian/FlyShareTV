package cn.fxdata.tv.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.search.SearchSchoolActivity.SchoolData;
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PathAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<VideoItem> mItems = new ArrayList<VideoItem>();
    private ViewHolder viewHolder;
    private FlyShareTVdataHelper dbHelper;

    public PathAdapter(Context context) {
        mContext = context;
        Log.d("renjun1", "pathadapter oncreat");
        dbHelper = new FlyShareTVdataHelper(mContext);
        initVideItem();

        /*
         * mItems.add(new String[]{"2015.6.22","观看了奔跑吧兄弟"}); mItems.add(new
         * String[]{"2015.6.22","观看了奔跑吧兄弟"}); mItems.add(new
         * String[]{"2015.6.21","观看了奔跑吧兄弟"}); mItems.add(new
         * String[]{"2015.6.19","观看了奔跑吧兄弟"}); mItems.add(new
         * String[]{"2015.6.19","观看了奔跑吧兄弟"}); mItems.add(new
         * String[]{"2015.6.18","观看了奔跑吧兄弟"});
         */
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final VideoItem vitem = mItems.get(position);
        final String itemTiem = vitem.getVideoLast();
        final String itemName = vitem.getfileName();

        Log.d("renjun1", "itemTiem:" + itemTiem + "itemName: " + itemName);
        viewHolder = new ViewHolder();
        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(
                    R.layout.path_item, null);
        }
        viewHolder.pathdata = (TextView) contentView
                .findViewById(R.id.path_data);
        viewHolder.pathcontent = (TextView) contentView
                .findViewById(R.id.path_content);
        viewHolder.pathdelete = (ImageView) contentView
                .findViewById(R.id.path_delte);
        if (position == 0) {
            if (isTheDay(itemTiem, "Today")) {
                Log.d("renjun1", "11111");
                viewHolder.pathdata.setText("今天");
            } else if (isTheDay(itemTiem, "Lastday")) {
                Log.d("renjun1", "2222222222");
                viewHolder.pathdata.setText("昨天");
            } else {
                viewHolder.pathdata.setText(itemTiem);
            }
            viewHolder.pathdata.setVisibility(View.VISIBLE);
        } else {
            if (itemTiem.equals(mItems.get(position - 1).getVideoLast())) {
                viewHolder.pathdata.setVisibility(View.GONE);
            } else {
                if (isTheDay(itemTiem, "Today")) {
                    Log.d("renjun1", "11111");
                    viewHolder.pathdata.setText("今天");
                } else if (isTheDay(itemTiem, "Lastday")) {
                    Log.d("renjun1", "2222222222");
                    viewHolder.pathdata.setText("昨天");
                } else {
                    viewHolder.pathdata.setText(itemTiem);
                }
                viewHolder.pathdata.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.pathcontent.setText("观看了" + itemName);
        viewHolder.pathdelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                deleteHistoryfromSQL(vitem);
                mItems.remove(vitem);
                notifyDataSetChanged();
            }
        });
        return contentView;
    }

    protected void deleteHistoryfromSQL(VideoItem vitem) {
        // TODO Auto-generated method stub
        String videouri = vitem.getVideoUri();
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            Log.d("renjun1", "111111111");
            // SQLiteDatabase db = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,_time" }, "_id=?",
                    new String[] { vitem.getMoveId() }, null, null, null);
            ContentValues values = new ContentValues();
            values.put("_time", "");
            Log.d("renjun1", "delete");
            dbHelper.update(values, vitem.getMoveId());
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private boolean isTheDay(String day, String compareday) {
        final long now = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(now);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int date = mCalendar.get(Calendar.DATE);

        if (compareday == "Lastday") {
            mCalendar.add(Calendar.DATE, -1);
            month = mCalendar.get(Calendar.MONTH) + 1;
            year = mCalendar.get(Calendar.YEAR);
            date = mCalendar.get(Calendar.DATE);
        }
        Log.d("renjun1", "month: " + month + "year: " + year + "date;" + date);
        String mCompday;
        if (month < 9) {
            if (date < 9) {
                mCompday = year + "" + "0" + month + "" + "0" + date;
            } else {
                mCompday = year + "" + "0" + month + "" + date;
            }
        } else {
            mCompday = year + "" + month + "" + date;
        }
        /*
         * String myear = day.substring(0, day.indexOf("."));
         * Log.d("renjun1","day.indexOf:"+day.indexOf(".")); String mmonth =
         * day.substring(day.indexOf(".")+1, day.lastIndexOf(".")); //if
         * (mmonth.length() == 1) { // mmonth = "0"+mmonth; //} String mdate =
         * day.substring(day.lastIndexOf(".")+1); //if (mdate.length() == 1) {
         * // mdate = "0"+mdate; //}
         * Log.d("renjun1","myear:"+myear+"mmonth:"+mmonth+"mdate:"+mdate);
         */
        if (day.equals(mCompday)) {
            return true;
        } else {
            return false;
        }
    }

    private class ViewHolder {
        private TextView pathdata;
        private TextView pathcontent;
        private ImageView pathdelete;
    }

    private void initVideItem() {
        // TODO Auto-generated method stub
        Log.d("renjun1", "124323423423424234");
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,_time,_name,_fav,_pos" }, null,
                    null, null, null, null);
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                VideoItem vItem = new VideoItem();
                vItem.setMoveId(mCursor.getString(mCursor.getColumnIndex("_id")));
                vItem.setVideoUri(mCursor.getString(mCursor
                        .getColumnIndex("_uri")));
                vItem.setfileName(mCursor.getString(mCursor
                        .getColumnIndex("_name")));
                vItem.setVideoLast(mCursor.getString(mCursor
                        .getColumnIndex("_time")));
                vItem.setVideoPostion(mCursor.getInt(mCursor
                        .getColumnIndex("_pos")));
                if (mCursor.getInt(mCursor.getColumnIndex("_fav")) == 1) {
                    vItem.setFavVideo(true);
                } else {
                    vItem.setFavVideo(false);
                }
                Log.d("renjun1",
                        mCursor.getString(mCursor.getColumnIndex("_uri")));
                // Log.d("renjun1",mCursor.getString(mCursor.getColumnIndex("_name")));
                Log.d("renjun1",
                        mCursor.getString(mCursor.getColumnIndex("_time")));
                if (!mCursor.getString(mCursor.getColumnIndex("_time"))
                        .isEmpty()) {
                    mItems.add(vItem);
                }
                mCursor.moveToNext();
            }

        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

}
