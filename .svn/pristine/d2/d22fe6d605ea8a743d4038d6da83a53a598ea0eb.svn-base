package cn.fxdata.tv.adapter;

import java.util.List;
import java.util.Map;

import cn.fxdata.tv.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EpisodeListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;

    public EpisodeListAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 10;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(context).inflate(R.layout.row_episode_item,
                null);
        return view;
    }

}