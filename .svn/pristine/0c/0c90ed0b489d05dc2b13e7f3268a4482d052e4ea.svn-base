package cn.fxdata.tv.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.fxdata.tv.R;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import cn.fxdata.tv.service.DownloadManagerService;
import cn.fxdata.tv.service.DownloadTask;
import cn.fxdata.tv.bean.VideoItem;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheAdapter extends BaseAdapter {
    public ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
    private Context mContext;
    private FlyShareTVdataHelper dbHelper;

    public CacheAdapter(Context context) {
        this.mContext = context;
        dbHelper = new FlyShareTVdataHelper(mContext);
        initItems();
    }

    public void initItems() {
        // TODO Auto-generated method stub
        Log.d("renjun1", "124323423423424234");
        videoItems.clear();
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history",
                    new String[] { "_id,_uri,_name,down_fin" }, null, null,
                    null, null, null);
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                Log.d("renjun1", "aaaaaaaaaaaaaaa");
                VideoItem vItem = new VideoItem();
                vItem.setVideoUri(mCursor.getString(mCursor
                        .getColumnIndex("_uri")));
                vItem.setfileName(mCursor.getString(mCursor
                        .getColumnIndex("_name")));
                vItem.setFinished(mCursor.getInt(mCursor
                        .getColumnIndex("down_fin")));
                vItem.setMoveId(mCursor.getString(mCursor.getColumnIndex("_id")));
                Log.d("renjun1",
                        mCursor.getString(mCursor.getColumnIndex("_uri")));
                // Log.d("renjun1",mCursor.getString(mCursor.getColumnIndex("_name")));
                if (mCursor.getInt(mCursor.getColumnIndex("down_fin")) >= 0) {
                    videoItems.add(vItem);
                }
                mCursor.moveToNext();
            }
            notifyDataSetChanged();

        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    protected int isExistItme(VideoItem vitem) {
        // TODO Auto-generated method stub
/*        for (int i = 0; i < videoItems.size(); i++) {
            if (videoItems.get(i).getVideoUri() == vitem.getVideoUri()) {
                return i;
            }
        }
        return -1;*/
        return -1;
    }

    public void updateDownloadItem(int pro, String id) {
        Log.d("renjun3","pro: "+pro+"id: "+id);
        for(int i = 0; i < videoItems.size();i++) {
            Log.d("renjun3","videoItems.get(i).getMoveId(): "+videoItems.get(i).getMoveId());
            if (videoItems.get(i).getMoveId().equals(id)) {
                Log.d("renjun1","updatepronnnn");
                videoItems.get(i).setmProgress(pro);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return videoItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return videoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Log.d("renjun3","getview data change");
        final VideoItem item = videoItems.get(position);
        String filename = item.getfileName();
        int progress = item.getmProgress();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.download_notify, null);
        }
        ProgressBar mBar = (ProgressBar) convertView
                .findViewById(R.id.download_progress);
        TextView fileview = (TextView) convertView
                .findViewById(R.id.download_file);
        /*final ImageView stopButton = (ImageView) convertView.findViewById(R.id.download_stop);
        if (DownloadTask.isDownload){
            stopButton.setImageResource(R.drawable.ic_media_pause);
        } else {
            stopButton.setImageResource(R.drawable.ic_media_pause);
        }*/
        /*stopButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                if (DownloadTask.isDownload) {
                    stopButton.setImageResource(R.drawable.ic_media_play);
                    Intent intent = new Intent(DownloadManagerService.ACTION_STOP_DOWNLOAD);
                    intent.putExtra("moveId", item.getMoveId());
                    mContext.sendBroadcast(intent);
                } else {
                    stopButton.setImageResource(R.drawable.ic_media_pause);
                    Intent intent = new Intent(DownloadManagerService.ACTION_START_DOWNLOAD);
                    intent.putExtra("moveId", item.getMoveId());
                    mContext.sendBroadcast(intent);
                }
            }
        });*/
        fileview.setText(filename);
        mBar.setProgress(progress);
        return convertView;
    }

}
