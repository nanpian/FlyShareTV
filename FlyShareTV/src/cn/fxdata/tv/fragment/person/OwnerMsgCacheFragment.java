package cn.fxdata.tv.fragment.person;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.manager.DownloadVideoManager;
import cn.fxdata.tv.service.DownloadManagerService;

/**
 * Created by Jianyong on 15/6/12.
 */
public class OwnerMsgCacheFragment extends Fragment {
    // private ArrayList<VideoItem> mItem = new ArrayList<VideoItem> ();
    private CacheAdapter mCashAdapter;
    // �̶�������ص����ֵ�·����SD��Ŀ¼��  
    private static final String SD_PATH = "/mnt/sdcard/";  
    // ��Ÿ���������  
    //private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();  
    // �������������Ӧ�Ľ����  
    private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>(); 
    /*
    private Handler mHandler = new Handler() {  
        public void handleMessage(Message msg) {  
            if (msg.what == 1) {  
                String url = (String) msg.obj;  
                int length = msg.arg1;  
                ProgressBar bar = ProgressBars.get(url);  
                if (bar != null) {  
                    // ���ý��������ȡ��length���ȸ���  
                    bar.incrementProgressBy(length);  
                    if (bar.getProgress() == bar.getMax()) {  
                        Toast.makeText(OwnerMsgCacheFragment.this.getActivity(), "������ɣ�", 0).show();  
                        // ������ɺ������������map�е�������  
                        LinearLayout layout = (LinearLayout) bar.getParent();  
                        layout.removeView(bar);  
                        ProgressBars.remove(url);  
                        downloaders.get(url).delete(url);  
                        downloaders.get(url).reset();  
                        downloaders.remove(url);  
                    }  
                }  
            }  
        }  
    };  */
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.owner_msg_path_fragment,
                container, false);
        ListView mList = (ListView) view.findViewById(R.id.path_list);
        mCashAdapter = new CacheAdapter(getActivity());
        mList.setAdapter(mCashAdapter);
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());  
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());  
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadVideoManager.DOWNLOAD_NOTIFY_PROGRESS);
        filter.addAction(DownloadVideoManager.DOWNLOAD_NOTIFY_INIT);
        getActivity().registerReceiver(mBroadcasterReceiver, filter);
    }

    private BroadcastReceiver mBroadcasterReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.d("renjun1", "receiver intent");
            if (intent.getAction().equals(
                    DownloadManagerService.DOWNLOAD_NOTIFY_PROGRESS)) {
                // VideoItem vitem = (VideoItem)
                // intent.getSerializableExtra("videoRes");
                VideoItem mV = null;
                mV = (VideoItem) intent.getSerializableExtra("download_progress");
                int mProgress = mV.getmProgress();
                String moveid = mV.getMoveId();
                // Log.d("renjun1","vitem uri"+vitem.getVideoUri());
                Log.d("renjun3","mProgress: "+mProgress+"moveid: "+moveid);
                mCashAdapter.updateDownloadItem(mProgress,moveid);
            } else if (intent.getAction().equals(
                    DownloadVideoManager.DOWNLOAD_NOTIFY_INIT)) {
                mCashAdapter.initItems();
            }
        }

    };

}