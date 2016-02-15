package cn.fxdata.tv.fragment.person;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.fxdata.tv.R;
import cn.fxdata.tv.adapter.CacheAdapter;
import cn.fxdata.tv.manager.DownloadVideoManager;
import cn.fxdata.tv.service.DownloadManagerService;

/**
 * Created by Jianyong on 15/6/12.
 */
public class OwnerMsgCacheFragment2 extends Fragment {
    // private ArrayList<VideoItem> mItem = new ArrayList<VideoItem> ();
    private CacheAdapter mCashAdapter;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.owner_msg_path_fragment,
                container, false);
        ListView mList = (ListView) view.findViewById(R.id.path_list);
        mCashAdapter = new CacheAdapter(getActivity());
        mList.setAdapter(mCashAdapter);
  
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}