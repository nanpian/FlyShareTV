package cn.fxdata.tv.activity.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.image.ImageLoader;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgntech.core.view.pullrefresh.PullToRefreshBase;
import com.zgntech.core.view.pullrefresh.PullToRefreshListView;
import com.zgntech.core.view.pullrefresh.PullToRefreshBase.OnRefreshListener;

import cn.fxdata.tv.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import cn.fxdata.tv.adapter.HotCommentAdapter;
import cn.fxdata.tv.adapter.HotCommentAdapter.OnUserClickZanClickListener;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.bean.HotCommentReturn;
import cn.fxdata.tv.bean.MovieDetail;
import cn.fxdata.tv.bean.HotCommentReturn.MovieComments;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.http.VolleyRequestManager;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.utils.StringUtils;

/**
 * 预告详情页面
 */

public class ForcastVideoDetailActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "forcastvideodetail";
    private ListView commentListView;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ScrollView scrollview;
    private ImageView backImageView;
    private Button sendButton;
    private EditText commentEditText;
    private ImageView movieThumbUI;
    private TextView movieEndTimeUI;
    private Button movieWarnMeUI;
    private TextView movieNameUI;
    private ImageView movieFollowUI;
    private TextView movieAreaOldUI;
    private TextView movieTypeUI;
    private TextView movieDetailUI;
    private Context context;
    private String movieId;
    private String movieName;
    private String movieType;
    private String movieArea;
    private boolean isMovieRemarked;
    private String movieDetail;
    private PullToRefreshListView pullToRefreshListView;
    public List<MovieComments> hot_comments = new ArrayList<HotCommentReturn.MovieComments>();
    public static boolean isZan = false;
    private Boolean isRefreshTime = false;
    private Boolean isSendingComment = false;
    private boolean isFollow = false;

    // 评论数据总共的页数
    public int totalPage = 0;
    // 评论数据当前页数
    public int currentPage = 0;

    private HotCommentAdapter hotCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.act_video_forcast_detail);

        movieThumbUI = (ImageView) findViewById(R.id.movie_thumb_fc);
        movieEndTimeUI = (TextView) findViewById(R.id.movie_end_time);
        movieWarnMeUI = (Button) findViewById(R.id.btn_warn_me);
        movieWarnMeUI.setOnClickListener(this);
        movieWarnMeUI.setTag(false);
        movieNameUI = (TextView) findViewById(R.id.name);
        movieFollowUI = (ImageView) findViewById(R.id.btn_follow_me);
        movieFollowUI.setOnClickListener(this);
        movieTypeUI = (TextView) findViewById(R.id.movie_type);
        movieAreaOldUI = (TextView) findViewById(R.id.movie_old_area);
        movieDetailUI = (TextView) findViewById(R.id.movie_detail);

        // 得到上下拉刷新的view
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.frag_fornotice_comment_listview);
        // 从上下拉刷新view中得到listview
        commentListView = pullToRefreshListView.getRefreshableView();
        // 设置一些listview属性，比如分割线为透明
        commentListView.setFadingEdgeLength(0);
        commentListView.setDividerHeight(0);
        commentListView.setSelector(getResources().getDrawable(R.drawable.transparent));
        // 评论中可以点赞和取消赞

        // 将评论的listview和adater相适配
        hotCommentAdapter = new HotCommentAdapter(this, hot_comments);
        hotCommentAdapter.setOnUserClickZanClickListener(new OnUserClickZanClickListener() {

            @Override
            public void onZanClick(int position) {
                // TODO Auto-generated method stub
                Log.d(TAG, "user click zan");
                // showToast("赞");
                // 首先得到comment id,movie id ,赞接口需要
                if (isRefreshTime == true) {
                    showToast("请稍后点赞");
                    return;
                }
                synchronized (isRefreshTime) {
                    isRefreshTime = true;
                    MovieComments movieComment = hot_comments.get(position);
                    if (movieComment != null) {
                        String commentId = movieComment.getComment_id();
                        if (!StringUtils.isNullOrEmpty(commentId) && !StringUtils.isNullOrEmpty(movieId)) {
                            zanRequest(commentId, position);
                        }
                    }

                }
            }
        });

        commentListView.setAdapter(hotCommentAdapter);

        // 上下拉刷新view上下拉回调
        pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            // 下拉回调，得到第一页评论数据
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshCommentData();
            }

            // 上拉回调，得到更多的评论数据
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreCommentData();
            }
        });

        // 刷新预告详情数据，得到movie id
        onGetIntent();
        // 进入首页，就进行第一次刷新评论数据
        pullToRefreshListView.doPullRefreshing(true, Constants.DEFAULT_VALUE.DEFAULT_LIST_LAOD_DELAYMILLIS);

        // scrollview = (ScrollView) findViewById(R.id.mScrollView);
        backImageView = (ImageView) findViewById(R.id.btn_back);
        backImageView.setOnClickListener(this);
        sendButton = (Button) findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);
        commentEditText = (EditText) findViewById(R.id.comment_edit);
        commentEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendText(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // UiTools.setListViewHeightBasedOnChildren(commentListView);
        // scrollview.smoothScrollTo(0, 0);
    }

    /**
     * 5s后允许再次点赞或评论
     */
    private void timer() {
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                isRefreshTime = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt, 5 * 1000);// 10秒后 允许
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 得到之前跳转传过来的movie id，更新预告简介，并把movie id全局化，为之后的加载评论数据做准备
     */
    private void onGetIntent() {
        Intent i = getIntent();
        if (i.hasExtra(Constants.MovieExtra.EXTRA_MOVIE_ID)) {
            movieId = i.getStringExtra(Constants.MovieExtra.EXTRA_MOVIE_ID);
            Log.d(TAG, "forcast video movieID: " + movieId);
            VolleyTools tools = VolleyTools.getInstance(this.getContext());
            tools.addToRequestQueue(new StringRequest(Method.GET, Constants.ServerConfig.PREVIEW_URL + "&movie_id=" + movieId, getMoviePrevListener), ForcastVideoDetailActivity.TAG);

            if (i.hasExtra(Constants.MovieExtra.EXTRA_MOVIE_THUMB)) {
                String thumb = i.getStringExtra(Constants.MovieExtra.EXTRA_MOVIE_THUMB);
                Log.d(TAG, "forcast video thumb: " + thumb);
                if (!TextUtils.isEmpty(thumb)) {
                    ImageListener imageListener = ImageLoader.getImageListener(movieThumbUI, R.drawable.none, R.drawable.none);
                    VolleyRequestManager.getInstance().getImageLoader().get(thumb, imageListener);
                }
            }
        } else {
            Log.e(TAG, "no has extra_movie_id");
            updateUI(false);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_warn_me:
            boolean isChecked = (Boolean) v.getTag();
            if (isChecked) {
                // 不在提醒这个电影
                ((Button) v).setText(R.string.fv_warn_me);
                // do something

            } else {
                // 提醒这个电影
                ((Button) v).setText(R.string.fv_had_warned);
                // do something

            }
            v.setTag(!isChecked);
            break;
        case R.id.btn_back: // 返回
            Log.d(TAG, "back onClick");
            finish();
            break;
        case R.id.btn_send:
            String send = commentEditText.getEditableText().toString();
            sendText(send);
            break;
        case R.id.btn_follow_me:
            if (isFollow) { // 弃追
                dropCatchRequest(movieId);
            } else { // 追剧
                catchMeRequest(movieId);
            }
            break;
        }
    }

    /**
     * 追剧接口
     */
    private void catchMeRequest(final String movieId) {
        VolleyTools tools = VolleyTools.getInstance(this.getContext());
        tools.addToRequestQueue(new StringRequest(Method.POST, Constants.ServerConfig.ZHUI_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    if (error_code == 0) {
                        setFollowUI(true);
                    } else if (error_code == 20001) {
                        setFollowUI(true);
                        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
                    } else if (error_code == 2000) {
                        setFollowUI(false);
                        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    setFollowUI(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                setFollowUI(false);
                Toast.makeText(getContext(), "追剧失败，请重试", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError");
            }

        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", getUserId());
                map.put("movie_id", movieId);
                return map;
            }
        }, tools.VolleyTAG);
    }

    private void dropCatchRequest(final String movieId) {
        VolleyTools tools = VolleyTools.getInstance(this.getContext());
        tools.addToRequestQueue(new StringRequest(Method.POST, Constants.ServerConfig.DROP_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    if (error_code == 0) {
                        setFollowUI(false);
                    } else {
                        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "dropCatchRequest: " + e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getContext(), "弃剧失败，请重试", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError");
            }

        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", getUserId());
                map.put("movie_id", movieId);
                return map;
            }
        }, tools.VolleyTAG);
    }

    /**
     * volley请求封装，用于刷新评论数据
     */
    public void sendVolleyRequest(Map<String, String> params, Listener<String> listener) {
        if (checkNet()) {
            Map<String, String> reqParams = getReqParam(params);
            com.zgntech.core.util.StringParamsRequest request = new com.zgntech.core.util.StringParamsRequest(Method.GET, Constants.ServerConfig.REQUEST_URL, listener, reqParams);
            Log.d(TAG, "send request: " + request.toString());
            // request.setRetryPolicy(getRetryPolicy());
            VolleyRequestManager.getInstance().addRequest(request, this);
        }
    }

    /**
     * 评论接口
     */
    private void sendCommentRequest(String movieString, String content) {

        isSendingComment = true;

        Map<String, String> param = new HashMap<String, String>();
        param.put("ac", "comment");
        param.put("user_id", getUserId());
        param.put("content", content);
        param.put("movie_id", movieString);
        sendVolleyRequest(param, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    isSendingComment = false;
                    JSONObject jsonObject = new JSONObject(response);
                    Integer error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    Log.d(TAG, "debug zan: error code is -->> " + error_code + " \n error msg is -->> " + error_msg);
                    if (error_code == 0) {
                        if (jsonObject != null) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            if (jsonData != null) {
                                String result = jsonData.getString("result");
                                if (result.equals("1")) {
                                    showToast("评论成功");
                                    pullToRefreshListView.doPullRefreshing(true, Constants.DEFAULT_VALUE.DEFAULT_LIST_LAOD_DELAYMILLIS);
                                }
                            }
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(error_msg)) {
                        showToast(error_msg);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    isSendingComment = false;
                    showToast("评论失败!");
                }

            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                isSendingComment = false;
                showToast("评论失败!");
            }
        });
    }

    /**
     * 赞接口
     */
    private synchronized void zanRequest(String commentId, final int postion) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ac", "praise");
        param.put("user_id", getUserId());
        param.put("comment_id", commentId);
        sendVolleyRequest(param, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "zanResponse:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Integer error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    Log.d(TAG, "debug zan: error code is -->> " + error_code + " \n error msg is -->> " + error_msg);
                    if (error_code == 0) {
                        if (jsonObject != null) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            if (jsonData != null) {
                                String result = jsonData.getString("result");
                                if (result.equals("1")) {
                                    showToast("点赞成功");
                                    MovieComments movieComments = hot_comments.get(postion);
                                    String oldTimes = hot_comments.get(postion).praise_times;
                                    hot_comments.get(postion).praise_times = String.valueOf(Integer.parseInt(oldTimes) + 1);
                                    hotCommentAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                    }
                    isRefreshTime = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("点赞失败!");
                    isRefreshTime = false;
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                isRefreshTime = false;
                showToast("点赞失败!");
            }
        });
    }

    /**
     * 用上下拉view的下拉刷新通过Volley请求获取电影评论数据
     */
    private void refreshCommentData() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ac", "commentList");
        param.put("user_id", getUserId());
        param.put("page", "1");
        param.put("records", "10");
        param.put("movie_id", movieId);
        Log.d(TAG, "The movie_id is -->> " + movieId);
        sendVolleyRequest(param, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "The response data string -- >> " + response);
                pullToRefreshListView.onPullDownRefreshComplete();
                try {
                    HotCommentReturn hotCommentListDataReturn = cn.fxdata.tv.utils.MJsonUtil.gson.fromJson((String) response, new TypeToken<HotCommentReturn>() {
                    }.getType());
                    hot_comments.clear();
                    if (hotCommentListDataReturn.getError_code() == 0) {
                        if (hotCommentListDataReturn.getData() != null && hotCommentListDataReturn.getData().getHot_comment() != null && hotCommentListDataReturn.getData().getHot_comment().size() > 0) {
                            hot_comments.addAll(hotCommentListDataReturn.getData().getHot_comment());
                            totalPage = hotCommentListDataReturn.getData().getTotal_page();
                            Log.d(TAG, "The hot comments size -->> " + hot_comments.size());
                            currentPage = 1;
                            commentListView.setAdapter(hotCommentAdapter);
                            hotCommentAdapter.setHotComments(hot_comments);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                pullToRefreshListView.onPullDownRefreshComplete();
            }

        });
    }

    /**
     * 上拉评论pulltofresh view加载更多评论数据
     */
    private void loadMoreCommentData() {
        Map<String, String> param = new HashMap<String, String>();

        param.put("ac", "commentList");
        param.put("user_id", getUserId());
        param.put("page", String.valueOf(currentPage + 1));
        param.put("records", "10");
        param.put("movie_id", movieId);

        Log.d(TAG, "The movie_id is -->> " + movieId);

        // 如果下拉没有刷新到数据，就不能上拉刷新
        if (currentPage == 0) {
            pullToRefreshListView.onPullUpRefreshComplete();
            return;
        }
        // 如果下拉刷新的页数，大于总页数，也不能上拉刷新
        if (currentPage > totalPage) {
            pullToRefreshListView.onPullUpRefreshComplete();
            return;
        }
        // 网络请求响应
        sendVolleyRequest(param, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "The response data string -- >> " + response);
                pullToRefreshListView.onPullDownRefreshComplete();
                pullToRefreshListView.onPullUpRefreshComplete();
                try {
                    HotCommentReturn hotCommentListDataReturn = cn.fxdata.tv.utils.MJsonUtil.gson.fromJson((String) response, new TypeToken<HotCommentReturn>() {
                    }.getType());
                    // 每次请求到数据后，清空之前的数据，只显示首页
                    hot_comments.clear();
                    if (hotCommentListDataReturn.getError_code() == 0) {
                        if (hotCommentListDataReturn.getData() != null && hotCommentListDataReturn.getData().getHot_comment() != null && hotCommentListDataReturn.getData().getHot_comment().size() > 0) {
                            // 加入增加的评论数据
                            hot_comments.addAll(hotCommentListDataReturn.getData().getHot_comment());
                            // 通知listview刷新数据
                            hotCommentAdapter.setHotComments(hot_comments);
                            commentListView.setSelection(hot_comments.size());
                        } else {
                            pullToRefreshListView.setHasMoreDataHeader(false);
                            showToast("没有更多评论数据！");
                        }
                    }
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {
                pullToRefreshListView.onPullDownRefreshComplete();
                pullToRefreshListView.onPullUpRefreshComplete();
                Log.d(TAG, "The response data string -- >> " + error.toString());
                // 此时最好还要显示重试按钮
            }

        });
    }

    /**
     * volley请求的重试策略
     */
    protected RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    private void sendText(String send) {

        synchronized (isSendingComment) {
            if (isSendingComment) {
                showToast("正在发送评论中...请稍后");
                return;
            }
            if (TextUtils.isEmpty(send)) {
                Toast.makeText(context, "发送内容不能未空", Toast.LENGTH_SHORT).show();
                return;
            }
            // 发送评论请求到网络
            sendCommentRequest(movieId, send);
            // Toast.makeText(context, "评论成功: send", 1000).show();
        }
    }

    private void updateUI(boolean success) {
        if (success) {
            movieEndTimeUI.setText(this.getString(R.string.fv_end_time, "06:12:21"));
            movieNameUI.setText(movieName);
            movieAreaOldUI.setText(this.getString(R.string.fv_video_old_position, "2014", movieArea));
            movieTypeUI.setText(this.getString(R.string.fv_video_type, movieType));
            movieDetailUI.setText(this.getString(R.string.fv_video_content, movieDetail));
            if (isMovieRemarked) {
                movieWarnMeUI.setText(R.string.fv_had_warned);
                movieWarnMeUI.setTag(true);
            } else {
                movieWarnMeUI.setText(R.string.fv_warn_me);
                movieWarnMeUI.setTag(false);
            }
        }
    }

    private void setFollowUI(boolean isFollow) {
        this.isFollow = isFollow;
        if (isFollow) {
            movieFollowUI.setImageResource(R.drawable.vedio_followme_pressed);
        } else {
            movieFollowUI.setImageResource(R.drawable.vedio_followme_normal);
        }
    }

    private Listener<String> getMoviePrevListener = new Listener<String>() {
        @Override
        public void onSuccess(String response) {
            Log.d(TAG, "the response --> " + response);
            MovieDetail movie = MJsonUtil.gson.fromJson(response, new TypeToken<MovieDetail>() {
            }.getType());
            if (movie.getError_code() == 0) {
                MovieDetail.MovieDetailData data = movie.getDetail();
                movieName = data.getName();
                movieType = data.getType();
                movieArea = data.getArea();
                movieDetail = data.getDetail();
                isMovieRemarked = data.getRemark();
                updateUI(true);
            } else {
                Log.e(TAG, movie.getError_msg());
                updateUI(false);
            }
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);
            Log.d(TAG, "the response --> " + error.toString());
            updateUI(false);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            Log.d(TAG, "cancelled");
        }
    };

}
