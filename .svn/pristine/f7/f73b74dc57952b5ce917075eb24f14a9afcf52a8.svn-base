package cn.fxdata.tv.fragment.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgntech.core.util.UiTools;

import cn.fxdata.tv.activity.video.ForplayVideoViewActivity;
import cn.fxdata.tv.adapter.CommentAdapter;
import cn.fxdata.tv.adapter.EpisodeListAdapter;
import cn.fxdata.tv.base.BaseFragment;
import cn.fxdata.tv.bean.comment.Comment;
import cn.fxdata.tv.bean.episode.Episode;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.http.VolleyRequestManager;
import cn.fxdata.tv.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Video detail episode fragment in video detail activity.
 */
public class EpisodeFragment extends BaseFragment {

	private final static String TAG = "EpisodeFragment";
	
    private EpisodeListAdapter adapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ListView commentListView;
    private GridView episodeGridView;

    private ForplayVideoViewActivity mForplayVideoViewActivity = null;
    public void setForplayVideoViewActivity(ForplayVideoViewActivity f) {
    	mForplayVideoViewActivity = f;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_episode_comment, container, false);
        commentListView = (ListView) view.findViewById(R.id.video_detail_comment_listview);
        episodeGridView = (GridView) view.findViewById(R.id.episode_gridview);
        adapter = new EpisodeListAdapter(this.getActivity(), data);
        commentListView.setAdapter(adapter);
        UiTools.setListViewHeightBasedOnChildren(commentListView);

        /*for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemText", String.valueOf(i));// 按序号做ItemText
            lstImageItem.add(map);
        }
        gridAdapter = new SimpleAdapter(this.getActivity(), lstImageItem, R.layout.row_grid_episode_item, new String[] { "ItemText" }, new int[] { R.id.episode_grid_textview });

        // 添加并且显示
        episodeGridView.setAdapter(gridAdapter);
        // 添加消息处理
        episodeGridView.setOnItemClickListener((OnItemClickListener) new ItemClickListener());*/
        return view;
    }

    class ItemClickListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
                                                    // click happened
                View arg1,// The view within the AdapterView that was clicked
                int arg2,// The position of the view in the adapter
                long arg3// The row id of the item that was clicked
        ) {
            // 在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
            String index = (String) item.get("ItemText");
            Log.i(TAG, "index : " + index);
            if(null != mForplayVideoViewActivity) {
            	String path = mSetList.get(Integer.valueOf(index)).getLink();
            	if(!path.equals("")) {
                	mForplayVideoViewActivity.onNewMoviePathSet(path);	
            	}
            }
        }
    }
    
    ArrayList<HashMap<String, String>> lstImageItem = new ArrayList<HashMap<String,String>>();
    SimpleAdapter gridAdapter = null;
    
    private void updateSetAdapter() {
    	if(mSetList.size() < 1) {
    		return;
    	}
    	
    	// if(10 > mSetList.size()) {
    		/*for(int i=0; i< lstImageItem.size() - mSetList.size(); i++) {
    			lstImageItem.remove(lstImageItem.size()-1);
    		}*/
    		lstImageItem.clear();
            for (int i = 0; i <mSetList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemText", String.valueOf(i));// 按序号做ItemText
                lstImageItem.add(map);
            }
            gridAdapter = new SimpleAdapter(this.getActivity(), lstImageItem, R.layout.row_grid_episode_item, new String[] { "ItemText" }, new int[] { R.id.episode_grid_textview });

    	// }
    	gridAdapter.notifyDataSetChanged();
    	// 添加并且显示
        episodeGridView.setAdapter(gridAdapter);
        episodeGridView.setOnItemClickListener((OnItemClickListener) new ItemClickListener());
    }
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
        // 启动参数解析
        parseArguments(bundle);
        
        updateCurrentMovieEpisode();
	}
    
	String mMovieId = "";
	int mSetCount = -1;
	// 保存所有选集的列表
	java.util.List<cn.fxdata.tv.bean.episode.List> mSetList = new ArrayList<cn.fxdata.tv.bean.episode.List>();
	
	private void updateCurrentMovieEpisode() {
		// 更新当前影片的选集信息
        Map<String, String> param = new HashMap<String, String>();
        
        fillParams(param, Constants.EpisodeConstant.CONSTANT_EPISODE_SETS, mMovieId);
        
        sendVolleyRequest(param, Constants.ServerConfig.BASE_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "The response data string -- >> " + response);
                try {
                	Episode episodeResponse = cn.fxdata.tv.utils.MJsonUtil.gson
							.fromJson((String) response,
									new TypeToken<Episode>() {
									}.getType());
					
                	if(episodeResponse.getErrorCode() == 0) {
                		if(episodeResponse.getData().getList() != null 
                				&& episodeResponse.getData().getList().size() > 0) {
                			mSetCount = episodeResponse.getData().getCountSets();
                			mSetList = episodeResponse.getData().getList();
                			
                			updateSetAdapter();
                		}
                	}
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
            }

        });
	}
	
    public void sendVolleyRequest(Map<String, String> params, String server, Listener<String> listener) {
        if (checkNet()) {
            Map<String, String> reqParams = getReqParam(params);
            com.zgntech.core.util.StringParamsRequest request = 
            		new com.zgntech.core.util.StringParamsRequest(Method.GET, server, listener, reqParams);
            request.setRetryPolicy(getRetryPolicy());
            VolleyRequestManager.getInstance().addRequest(request, this);
        }
    }
	
    private void parseArguments(Bundle bundle) {
        if (null != bundle) {
            // 获取MovieId
            mMovieId = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_ID);
        } else {
            mMovieId = "5";
        }
        Log.i(TAG, "mMovieId : " + mMovieId);
    }
    
    private void fillParams(Map<String, String> map, String ac, String movieId) {
        if (ac != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_AC, ac);
        if (movieId != null)
            map.put(Constants.ConstantDefault.CONSTANT_DEFAULT_MOVIE_ID, movieId);
    }
}
