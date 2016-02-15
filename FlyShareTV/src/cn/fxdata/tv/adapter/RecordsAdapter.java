package cn.fxdata.tv.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.videorecords.MoviePlayRecordsActivity;
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.bean.movie.Data;
import cn.fxdata.tv.db.FlyShareTVdataHelper;

/**
 * Created by Jianyong on 15/7/22.
 */
/* Modify by renjun on 2015.725 */
public class RecordsAdapter extends BaseAdapter {
    private static final String TAG = "RecordsAdapter";
    private Context context;
    private LayoutInflater inflater;
    private FlyShareTVdataHelper dbHelper;
    private ArrayList<VideoItem> mItems = new ArrayList<VideoItem>();
    private boolean mDeleteMode = false;
    //add delte list to record want to delete
    private ArrayList<VideoItem> mDeleteList = new ArrayList<VideoItem>();
    private Handler mHandler;

    public RecordsAdapter(Context context, Handler handler) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        dbHelper = new FlyShareTVdataHelper(context);
        initVideItem();
        mDeleteList.clear();
        this.mHandler = handler;
    }

    public void setDeleteMode(boolean state) {
        this.mDeleteMode = state;
        notifyDataSetChanged();
    }

    private void initVideItem() {
        // TODO Auto-generated method stub
        mItems.clear();
        Log.d("renjun1", "124323423423424234");
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    null, null,
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
                vItem.setThumb(mCursor.getString(mCursor.getColumnIndex("_thumb")));
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

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, " getview start");
        // Log.d(TAG, "mDelteAllSelect:" + mDelteAllSelect);
        final ViewHolder holder;
        final VideoItem vitem = mItems.get(position);
        final String itemTiem = vitem.getVideoLast();
        final String itemName = vitem.getfileName();
        int postion = vitem.getVideoPostion();
        String playtime = formatTime(postion);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.movie_play_records_item,
                    null);
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.select_check);
            holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            holder.tvTimeHead = (TextView) convertView
                    .findViewById(R.id.time_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(vitem.getThumb(), holder.thumb);
        holder.tvTitle.setText(itemName);
        holder.tvTimeHead.setText("已观看到" + playtime);
        if (mDeleteMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            if (mDeleteList.contains(vitem)) {
                holder.checkBox.setChecked(true);
                holder.checkBox
                        .setButtonDrawable(R.drawable.records_butn_check_select);
            } else {
                holder.checkBox.setChecked(false);
                holder.checkBox
                        .setButtonDrawable(R.drawable.records_delete_checkbox);
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (holder.checkBox.isChecked()) {
                        holder.checkBox
                                .setButtonDrawable(R.drawable.records_butn_check_select);
                        mDeleteList.add(vitem);
                        Log.d(TAG, "mDeleteList.size(): " + mDeleteList.size());
                    } else {
                        holder.checkBox
                                .setButtonDrawable(R.drawable.records_delete_checkbox);
                        mDeleteList.remove(vitem);
                        Log.d(TAG, "mDeleteList.size(): " + mDeleteList.size());
                        Log.d(TAG, "remove one");
                    }
                    mHandler.sendEmptyMessage(MoviePlayRecordsActivity.SELEC_ALL_RECORD);
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    private String formatTime(int postion) {
        // TODO Auto-generated method stub
        int second = postion / 1000;
        if (second / 60 == 0) {
            return "00:" + (second < 10 ? ("0" + second) : second);
        } else if (second / 60 / 60 == 0) {
            return ((second / 60) < 10 ? ("0" + second / 60) : (second / 60)) + ":"
                    + ((second % 60) < 10 ? ("0" + second % 60) : (second % 60));
        } else {
            int hour = second / 60 / 60;
            int minitue = second / 60 % 60;
            int mSecond = second % 60;
            return (hour < 10 ? ("0" + hour)
                    : hour) + ":" + (minitue < 10 ? ("0" + minitue) : minitue) + ":"
                            + (mSecond < 10 ? ("0" + mSecond) : mSecond);
        }
    }

    public ArrayList<VideoItem> getmDeleteList() {
        return mDeleteList;
    }

    public void setmDeleteList(ArrayList<VideoItem> mDeleteList) {
        this.mDeleteList = mDeleteList;
    }

    public void deleteSelectRecord() {
        Log.d(TAG, "delete record");
        SQLiteDatabase db = null;
        Log.d(TAG, "mDeleteList.size(): " + mDeleteList.size());
        try {
            for (int i = 0; i < mDeleteList.size(); i++) {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("_time", "");
                Log.d("renjun1", "delete");
                dbHelper.update(values, mDeleteList.get(i).getMoveId());
                mItems.remove(mDeleteList.get(i));
            }
        } finally {
            if (null != db) {
                db.close();
            }
        }
        mDeleteList.clear();
    }

    public void selectAllRecord() {
        Log.d(TAG, "SELECT ALL");
        mDeleteList.clear();
        for (int i = 0; i < mItems.size(); i++) {
            mDeleteList.add(mItems.get(i));
        }
        Log.d(TAG, "mDeleteList.size(): " + mDeleteList.size());
        notifyDataSetChanged();
    }

    public void dropSelectAll() {
        Log.d(TAG, "DROP select");
        mDeleteList.clear();
        Log.d(TAG, "mDeleteList.size(): " + mDeleteList.size());
        notifyDataSetChanged();
    }

    public boolean isAllRecordSelected() {
        Log.d(TAG, "mItems.size():" + mItems.size());
        Log.d(TAG, "mDeleteList.size():" + mDeleteList.size());
        return (mItems.size() != 0) && (mItems.size() == mDeleteList.size());
    }

    class ViewHolder {
        private CheckBox checkBox;
        private ImageView thumb;
        private TextView tvTitle;
        private TextView tvTimeHead;
    }
}