package cn.fxdata.tv.fragment.home;

import java.util.ArrayList;
import java.util.List;

import cn.fxdata.tv.activity.video.ForcastVideoDetailActivity;
import cn.fxdata.tv.activity.video.ForplayVideoViewActivity;
import cn.fxdata.tv.adapter.MoviesNowAdapter;
import cn.fxdata.tv.adapter.MoviesPrevAdapter;
import cn.fxdata.tv.base.BaseTitleBarFragment;
import cn.fxdata.tv.bean.NewMoviesReturn;
import cn.fxdata.tv.bean.NewMoviesReturn.Movies;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.utils.DateTimeUtil;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.view.widget.*;

import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.image.ImageLoader;
import com.android.volley.request.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshStaggeredView;
import com.zgntech.core.view.HorizontalListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import cn.fxdata.tv.R;

/**
 * @description：TODO<请描述这个类是干什么的>
 * @author：lyu
 * @date:2015-5-30
 * @version：V1.0
 */
public class LatestVedioFragment extends BaseTitleBarFragment {
    private static final String TAG = "LatestVideoFragment";

    public VolleyTools volleyTools;

    // 今日更新列表
    private List<Movies> list_now = new ArrayList<Movies>();
    private MoviesNowAdapter moviesNowAdapter;
    // 预告列表
    private List<Movies> list_pre = new ArrayList<Movies>();
    private MoviesPrevAdapter moviewPrevAdapter;

    private PullToRefreshStaggeredView vedioPullToRefreshListView;
    private View listHeader;
    private CountdownClock mCdClock;
    private HorizontalListView horizontalListView;

    private StaggeredGridView staggeredGridView;

    private ImageView connectError;

    // fragement可视的时候懒加载，fragment准备好的标志
    private boolean isPrepared = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitleText(getActivity().getString(R.string.latest_video_title));
        setRightImageView(R.drawable.icon_person_avatar);
        setRight2ImageView(R.drawable.icon_history_clock);
        setContentLayout(R.layout.frag_main_latest_vedio);

        vedioPullToRefreshListView = (PullToRefreshStaggeredView) layoutContent
                .findViewById(R.id.video_listview);
        connectError = (ImageView) layoutContent
                .findViewById(R.id.connect_error);
        connectError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "connect error click");
                vedioPullToRefreshListView.setVisibility(View.VISIBLE);
                connectError.setVisibility(View.GONE);
                vedioPullToRefreshListView.setRefreshing();
            }
        });

        listHeader = inflater.inflate(R.layout.view_listview_header, null);

        mCdClock = (CountdownClock) listHeader
                .findViewById(R.id.count_down_clock);
        mCdClock.setVisibility(View.GONE);
        horizontalListView = (com.zgntech.core.view.HorizontalListView) listHeader
                .findViewById(R.id.list_pre_view);
        moviewPrevAdapter = new MoviesPrevAdapter(getActivity(), list_pre);
        horizontalListView.setAdapter(moviewPrevAdapter);

        moviesNowAdapter = new MoviesNowAdapter(getActivity(), list_now);
        staggeredGridView = (StaggeredGridView) (vedioPullToRefreshListView)
                .getRefreshableView();
        staggeredGridView.addHeaderView(listHeader);
        staggeredGridView.setAdapter(moviesNowAdapter);

        vedioPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                        int header = staggeredGridView.getHeaderViewsCount();
                        Log.d("TAG", "forplay click position: " + position
                                + ", header count: " + header);
                        if (position - header <= 0)
                            return;
                        Intent intent = new Intent(getActivity(),
                                ForplayVideoViewActivity.class);
                        intent.putExtra(Constants.MovieExtra.EXTRA_MOVIE_ID,
                                list_now.get(position - header).getMovie_id());
                        getActivity().startActivity(intent);
                    }
                });

        vedioPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<StaggeredGridView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<StaggeredGridView> refreshView) {
                        getHomeData();
                    }

                });

        horizontalListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long id) {
                Log.d(TAG, "Header onItemClick, position = " + position);
                if (position < 0)
                    return;

                if (list_pre != null && list_pre.size() > 0) {
                    Movies movies = list_pre.get(position);
                    if (movies != null) {
                        Intent intent = new Intent(getActivity(),
                                ForcastVideoDetailActivity.class);
                        intent.putExtra(Constants.MovieExtra.EXTRA_MOVIE_ID,
                                list_pre.get(position).getMovie_id());
                        intent.putExtra(Constants.MovieExtra.EXTRA_MOVIE_THUMB,
                                list_pre.get(position).getThumb());
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

        isPrepared = true;
        // lazyLoad();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        lazyLoad();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "lastest vedio fragment destroy view");
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        Log.d(TAG, "the lazyload start");
        // 如果frgament没准备好，或者不可视，都直接return
        if (!isPrepared || !isVisible) {
            return;
        }

        Log.d(TAG, "the volley start");

        Log.d(TAG, "Fragment onvisible");
        new Handler(getActivity().getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                vedioPullToRefreshListView.setRefreshing();
            }
        }, 1000);

    }

    private void getHomeData() {

        vedioPullToRefreshListView.setVisibility(View.VISIBLE);
        connectError.setVisibility(View.GONE);

        volleyTools = VolleyTools.getInstance(this.getContext());
        volleyTools.addToRequestQueue(new StringRequest(Method.GET,
                Constants.ServerConfig.HOME_URL2, getMoviesListener),
                volleyTools.VolleyTAG);

    }

    private Listener<String> getMoviesListener = new Listener<String>() {
        private Object mQueue;
        private ImageLoader mImageLoader;

        @Override
        public void onSuccess(String response) {
            Log.d(TAG, "the response --> " + response);
            vedioPullToRefreshListView.onRefreshComplete();
            NewMoviesReturn moviesReturn = MJsonUtil.gson.fromJson(response,
                    new TypeToken<NewMoviesReturn>() {
                    }.getType());
            if (moviesReturn.getError_code() == 0) {
                moviesReturn.getError_msg();
                list_now = moviesReturn.getData().getList_now();
                list_pre = moviesReturn.getData().getList_pre();
                if (list_now != null && list_now.size() > 0) {
                    Log.d(TAG, "The list now size --> " + list_now.size());
                    list_now.add(0,
                            Movies.getMovieTime(R.drawable.video_image_01));
                    moviesNowAdapter.context = LatestVedioFragment.this
                            .getActivity();
                    moviesNowAdapter.moviesList = list_now;
                    moviesNowAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "The list now is null");
                }

                if (list_pre == null
                        || (list_pre != null && list_pre.size() == 0)) {
                    listHeader.setVisibility(View.GONE);
                    staggeredGridView.removeHeaderView(listHeader);
                } else {
                    staggeredGridView.removeHeaderView(listHeader);
                    staggeredGridView.addHeaderView(listHeader);
                }

                String preview_end = moviesReturn.getData().getPreview_end();
                Log.d(TAG, "countDownTime: " + preview_end);
                if (preview_end != null) {
                    long currTime = System.currentTimeMillis();
                    long prevTime = DateTimeUtil.StringToUnixTime(preview_end);
                    Log.d(TAG, "prevTime: " + prevTime + " curr:" + currTime);
                    if (prevTime - currTime > 1000 * 60)
                        mCdClock.Start(null, prevTime - currTime);
                    else {
                        Log.e(TAG, "prevTime < currTime");
                        mCdClock.Start(null, 0);
                    }
                    mCdClock.setVisibility(View.VISIBLE);
                }

                if (list_pre != null && list_pre.size() > 0) {
                    Log.d(TAG,
                            "The listpre pre size is -->> " + list_pre.size());
                    moviewPrevAdapter.context = LatestVedioFragment.this
                            .getActivity();
                    moviewPrevAdapter.moviesList = list_pre;
                    moviewPrevAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "The list pre is null");
                }

                if (list_now == null && list_pre == null) {
                    // show network error
                    vedioPullToRefreshListView.setVisibility(View.GONE);
                    connectError.setVisibility(View.VISIBLE);
                }
            } else {
                Log.d(TAG, "movies return error");
            }
        }

        @Override
        public void onError(VolleyError error) {
            vedioPullToRefreshListView.onRefreshComplete();
            super.onError(error);
            Log.d(TAG, "the response --> " + error.toString());
            vedioPullToRefreshListView.setVisibility(View.GONE);
            connectError.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancel() {
            vedioPullToRefreshListView.onRefreshComplete();
            super.onCancel();
            Log.d(TAG, "cancelled");
        }
    };
}
