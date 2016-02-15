package cn.fxdata.tv.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class RadioViewGroup {

    private Context mContext;
    private Map<Object, View> map;
    private View selectedView;

    /**
     * 
     * @param context
     * @param isMap
     *            false:不启用组管理
     */
    public RadioViewGroup(Context context, boolean isMap) {
        this.mContext = context;
        if (isMap) {
            map = new HashMap<Object, View>();
        }
    }

    public void putById(View view) {
        map.put(view.getId(), view);
    }

    public void put(Object key, View view) {
        map.put(key, view);
    }

    public void selected(View view) {
        if (null != selectedView) {
            selectedView.setSelected(false);
        }
        selectedView = view;
        selectedView.setSelected(true);
    }

}
