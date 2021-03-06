package cn.fxdata.tv.adapter;

import java.util.List;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.search.SearchSchoolActivity.SchoolData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<SchoolData> list;
    private ViewHolder viewHolder;

    public ListViewAdapter(Context context, List<SchoolData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        if (list.get(position).getName().length() == 1)// 如果是字母索引
            return false;// 表示不能点击
        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = list.get(position).getName();
        viewHolder = new ViewHolder();
        if (item.length() == 1) {
            // "A","B","C"...
            // 用于区分各个分组的分组名
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.search_school_activity_listview_item_school_index,
                    null);
            viewHolder.indexTv = (TextView) convertView
                    .findViewById(R.id.indexTv);
            convertView.setTag("null");
        } else {
            // 北京大学, 北京交通大学等分组中的内容
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.search_school_activity_listview_item_school_name,
                    null);
            viewHolder.itemTv = (TextView) convertView
                    .findViewById(R.id.itemTv);
            convertView.setTag(list.get(position).getName());
        }
        if (item.length() == 1) {
            viewHolder.indexTv.setText(list.get(position).getName());
        } else {
            viewHolder.itemTv.setText(list.get(position).getName());
        }
        convertView.setTag(R.id.tag_hotplay_school_name, list.get(position)
                .getName());
        convertView.setTag(R.id.tag_hotplay_school_id,
                new Integer(list.get(position).getSchoolId()));
        return convertView;
    }

    private class ViewHolder {
        private TextView indexTv;
        private TextView itemTv;
    }

}