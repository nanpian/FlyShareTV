package cn.fxdata.tv.fragment.home.hotplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.zgntech.core.util.UiTools;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.search.SearchSchoolActivity;
import cn.fxdata.tv.activity.video.ForplayVideoViewActivity;
import cn.fxdata.tv.adapter.HotPlayMovieAdapter;
import cn.fxdata.tv.adapter.ListViewAdapter;
import cn.fxdata.tv.bean.HotPlayMoviesReturn;
import cn.fxdata.tv.bean.HotPlayMoviesReturn.MoviesItem;
import cn.fxdata.tv.bean.movie.Movie;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.utils.SettingsProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public abstract class HotplayBaseFragment extends Fragment {

    private final String TAG = "HotplayBaseFragment";

    protected View mMainView;
    protected Context mContext;
    private static int[] images;
    private List<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();

    protected final String CLASS_VARIETY = "综艺";
    protected final String CLASS_DRAMA = "电视剧";
    protected final String CLASS_MOVIE = "电影";
    protected final String CLASS_ANIME = "动漫";

    private ArrayList<MoviesItem> mDataList = new ArrayList<MoviesItem>();
    private HotPlayMovieAdapter mDataAdapter;

    public abstract String getClassType();

    static {
        images = new int[] { R.drawable.hot_image_01, R.drawable.hot_image_02,
                R.drawable.hot_image_03, R.drawable.hot_image_04,
                R.drawable.hot_image_01, R.drawable.hot_image_02,
                R.drawable.hot_image_03, R.drawable.hot_image_04,
                R.drawable.hot_image_01, R.drawable.hot_image_02,
                R.drawable.hot_image_03, R.drawable.hot_image_04,
                R.drawable.hot_image_01, R.drawable.hot_image_02,
                R.drawable.hot_image_03, R.drawable.hot_image_04 };
    }

    public HotplayBaseFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void showActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void startForplayVideoActivity(int movieId, final String movieThumb) {
        VolleyTools tools = VolleyTools.getInstance(getActivity());
        // 根据不同学校的Id返回
        String requestUrl = Constants.ServerConfig.TODAY_MOVIE_URL
                + "&movie_id=" + movieId;
        StringRequest hotRequest = new StringRequest(Method.GET, requestUrl,
                new Listener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "data response --> " + response.toString());
                        Movie movie = MJsonUtil.gson.fromJson(response,
                                new TypeToken<Movie>() {
                                }.getType());
                        if (movie.getErrorCode() == 0) {
                            if (movie.getData() != null
                                    && movie.getData().getFilmUrl() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(
                                        Constants.MovieExtra.EXTRA_MOVIE_PATH,
                                        movie.getData().getFilmUrl());
                                bundle.putString(
                                        Constants.MovieExtra.EXTRA_MOVIE_ID,
                                        movie.getData().getMovieId());
                                bundle.putString(
                                        Constants.MovieExtra.EXTRA_MOVIE_THUMB,
                                        movieThumb);
                                Log.i(TAG, "FilmUrl : "
                                        + movie.getData().getFilmUrl());
                                showActivity(ForplayVideoViewActivity.class,
                                        bundle);
                            }
                        } else {
                            Log.i(TAG, "ERROR CODE");
                            showActivity(ForplayVideoViewActivity.class);
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        Log.d(TAG, "the response --> " + error.toString());
                        showActivity(ForplayVideoViewActivity.class);
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        Log.d(TAG, "cancelled");
                        showActivity(ForplayVideoViewActivity.class);
                    }
                });
        tools.addToRequestQueue(hotRequest, tools.VolleyTAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_one, container, false);
        /*
         * GridView hotGridView = (GridView)
         * mMainView.findViewById(R.id.gv_hots); for (int i = 0; i < 12; i++) {
         * Map<String, Object> map = new HashMap<String, Object>();
         * map.put("image", images[i]); map.put("name", "快乐大本营");
         * map.put("description", "小kimi哭着喊着要妈妈"); mDatas.add(map); }
         * SimpleAdapter adapter = new SimpleAdapter(getActivity(), mDatas,
         * R.layout.row_hot_video, new String[] { "image", "name", "description"
         * }, new int[] { R.id.image, R.id.name, R.id.description });
         * hotGridView.setAdapter(adapter);
         * UiTools.setGridViewHeightBasedOnChildren(hotGridView);
         */
        /*
         * ListView listView = (ListView) mMainView.findViewById(R.id.list);
         * SimpleAdapter adapter = new SimpleAdapter(mContext, mlistItems,
         * R.layout.listview_item, new String[] { "name", "sex" }, new int[] {
         * R.id.name, R.id.download }); listView.setAdapter(adapter);
         */
        GridView hotGridView = (GridView) mMainView.findViewById(R.id.gv_hots);
        mDataAdapter = new HotPlayMovieAdapter(mContext);
        hotGridView.setAdapter(mDataAdapter);
        hotGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                // start the video detail fragment
                final int movieId = (Integer) view.getTag(R.id.tag_movie_id);
                final String movieThumb = (String) view
                        .getTag(R.id.tag_movie_thumb);
                Log.i("xixia", "movieId : " + movieId);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... unused) {
                        startForplayVideoActivity(movieId, movieThumb);
                        return null;
                    }
                }.execute();
            }
        });
        loadData(getSchoolId());
        return mMainView;
    }

    private int getSchoolId() {
        int schoolId = SettingsProvider.getIntCustomDefault(getActivity(),
                SettingsProvider.SETTINGS_HOT_PLAY_SCHOOL_ID, 2);
        return schoolId;
    }

    private void loadData(int schoolId) {
        mDataList.clear();

        VolleyTools tools = VolleyTools.getInstance(getActivity());
        // 根据不同学校的Id返回
        String requestUrl = Constants.ServerConfig.HOT_PALY_URL + "&school_id="
                + schoolId;
        StringRequest hotRequest = new StringRequest(Method.GET, requestUrl,
                new Listener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "data response --> " + response.toString());
                        HotPlayMoviesReturn hotPlayMoviesReturn = MJsonUtil.gson
                                .fromJson(response,
                                        new TypeToken<HotPlayMoviesReturn>() {
                                        }.getType());
                        if (hotPlayMoviesReturn.getError_code() == 0) {
                            int size = hotPlayMoviesReturn.getData().size();
                            Log.i(TAG, "size is : " + size);
                            for (int i = 0; i < size; i++) {
                                ArrayList<MoviesItem> list = (ArrayList<MoviesItem>) hotPlayMoviesReturn
                                        .getData().get(i);
                                MoviesItem item = list.get(0);
                                if (TextUtils.isEmpty(item.getClasses())) {
                                    continue;
                                }
                                if (item.getClasses().equals(getClassType())) {
                                    mDataList.addAll(list);
                                    mDataAdapter.setDataAndNotify(mDataList);
                                    break;
                                }
                            }
                        } else {
                            Log.i(TAG, "ERROR CODE");
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        Log.d(TAG, "the response --> " + error.toString());
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        Log.d(TAG, "cancelled");
                    }
                });
        tools.addToRequestQueue(hotRequest, tools.VolleyTAG);
    }
}