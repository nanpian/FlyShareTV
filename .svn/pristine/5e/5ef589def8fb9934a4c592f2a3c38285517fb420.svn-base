package cn.fxdata.tv.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.fxdata.tv.R;
import cn.fxdata.tv.db.DownloadUtil;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import cn.fxdata.tv.download.DLVideoItem;
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

public class CacheAdapter2 extends BaseAdapter {
    public List<DLVideoItem> videoItems = new ArrayList<DLVideoItem>();
    private Context mContext;

    public CacheAdapter2(Context context) {
        this.mContext = context;
        initDownloadItems();
    }

    public void initDownloadItems() {
        videoItems = DownloadUtil.getInstance(mContext).getInfos();
        
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
        final DLVideoItem item = videoItems.get(position);
        String filename = item.getMovie_name();
        int progress = item.getCompeleteSize();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.download_item, null);
        }
        
        TextView videoSize2 = (TextView) convertView.findViewById(R.id.videoSize2);
        TextView videoSize = (TextView) convertView.findViewById(R.id.videoSize);
        videoSize2.setText(String.valueOf(item.getEndPos()));
        videoSize.setText(String.valueOf(item.getCompeleteSize()));
        ProgressBar mBar = (ProgressBar) convertView.findViewById(R.id.download_seekbar);
        mBar.setMax(item.getEndPos());
        mBar.setProgress(progress);
        TextView fileName = (TextView) convertView.findViewById(R.id.download_videoName);
        fileName.setText(filename);
        //final ImageView stopButton = (ImageView) convertView.findViewById(R.id.download_stop);
        
        return convertView;
    }

}
