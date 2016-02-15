package cn.fxdata.tv.activity.video;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.request.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.zgntech.core.manager.UIFragmentManager;

import cn.fxdata.tv.R;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.fxdata.tv.manager.DownloadVideoManager;
import cn.fxdata.tv.service.DownloadManagerService;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.utils.ToastUtils;
import cn.fxdata.tv.utils.share.OnekeyShare;
import cn.fxdata.tv.view.StateImageView.ImageStateChangeInterface;
import cn.fxdata.tv.view.video.FullScreenVideoView;
import cn.fxdata.tv.view.video.VideoControllerView;
import cn.fxdata.tv.view.widget.FlowRadioGroup.OnCheckedChangeListener;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.bean.movie.Movie;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.db.DownloadUtil;
import cn.fxdata.tv.db.FavDbUtil;
import cn.fxdata.tv.db.FlyShareTVdataHelper;
import cn.fxdata.tv.download.DLVideoItem;
import cn.fxdata.tv.download.DownloadStatus;
import cn.fxdata.tv.download.FxDownloadService;
import cn.fxdata.tv.download.FxDownloadTask;
import cn.fxdata.tv.download.FxDownloadTaskManager;
import cn.fxdata.tv.fragment.detail.CommentFragment;
import cn.fxdata.tv.fragment.detail.DetailFragment;
import cn.fxdata.tv.fragment.detail.EpisodeFragment;
import cn.fxdata.tv.view.widget.FlowRadioGroup;
import cn.sharesdk.framework.ShareSDK;

/**
 * 视频详情页面
 */

public class ForplayVideoViewActivity2 extends BaseActivity implements FullScreenVideoView.ToggleFullScreen, FullScreenVideoView.BackClickInterface, ImageStateChangeInterface, View.OnClickListener {

    private final static String TAG = "ForplayVideoViewActivity";
    private Context context;
    private FlowRadioGroup mRadioGroup;
    private UIFragmentManager fragmentManager;
    private ImageView video_fullscreen;
    private RelativeLayout video_play_all;
    private cn.fxdata.tv.view.video.FullScreenVideoView video_play_view;
    private RelativeLayout video_down_layout;
    private FullScreenVideoView mVideoView;
    private static int fragmentId = 0;
    private ProgressBar bar;
    int height;
    int width;
    private ImageView mImgFollowMe;
    // 是否是收藏的图片标志显示
    private ImageView image_fav;
    private ImageView mImgFav;
    private FlyShareTVdataHelper dbHelper;
    private String videoUri;
    private ImageView mDownloadImage;
    private DownloadManagerService.DownloadBinder mBinder;
    public VideoControllerView videoControllerView;
    private String movieId;
    private boolean isCatch = false;// 此处应该是从网络请求来的，表示该视频是否被追
    private ImageView btnShare;
    private boolean isSelected = false;
    // 视频的名称,在getDetailListener里获取到
    public String title;
    private String mMovieThumb = null;
    // 视频详情类
    private Movie mCurrentMovieDetail = null;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.act_video_play_view_detail);
        // 拿到数据库实例
        dbHelper = new FlyShareTVdataHelper(this);
        initFragments();
        initView();
        Intent intent = new Intent(this, DownloadManagerService.class);
        // intent.setAction("cn.fxdata.tv.download_action");
        // startService(intent);
        // bindService(intent,conn,0);

        mVideoView.setOnToggleFullScreen(this);
        mVideoView.setBackClickInterface(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                bar.setVisibility(View.GONE);
            }
        });

        calculateVideoViewSize();
        mVideoView.setDimensions(width, height);
        videoControllerView = new cn.fxdata.tv.view.video.VideoControllerView(this);
        mVideoView.setMediaController(videoControllerView);
        videoUri = null;
        if (getIntent().getExtras() == null) {
            ToastUtils.ToastAdd(getContext(), "数据为空");
            mVideoView.setVideoURI(Uri.parse("http://fx.72zhe.com/static/fx/sample.mp4"));
            videoUri = "http://fx.72zhe.com/static/fx/sample.mp4";
        } else if (getIntent().hasExtra(Constants.MovieExtra.EXTRA_MOVIE_PATH)) {
            if (getIntent().hasExtra(Constants.MovieExtra.EXTRA_MOVIE_ID)) {
                movieId = getIntent().getExtras().getString(Constants.MovieExtra.EXTRA_MOVIE_ID);
                Log.d(TAG, "forplay video movieID: " + movieId);
                VolleyTools tools = VolleyTools.getInstance(this.getContext());
                tools.addToRequestQueue(new StringRequest(Method.GET, Constants.ServerConfig.MOVIE_DETAIL_URL + "&movie_id=" + movieId, getMovieDetailListener), tools.VolleyTAG);

                // 判断是否是收藏的视频,显示不同的收藏状态图片
                boolean isFav = FavDbUtil.getInstance(getContext()).isFavVideo(movieId);
                isSelected = isFav;
                Log.d(TAG, "The fav video is -->> " + isFav);
                if (isFav) {
                    image_fav.setImageResource(R.drawable.vedio_fav_pressed);
                } else {
                    image_fav.setImageResource(R.drawable.vedio_fav_normal);
                }
            } else {
                String path = getIntent().getExtras().getString(Constants.MovieExtra.EXTRA_MOVIE_PATH);
                Log.i(TAG, "path : " + path);
                mVideoView.setVideoURI(Uri.parse(path));
                videoUri = path;
                mVideoView.requestFocus();
                // updateShareTVdatabase(videoUri,
                // mVideoView.getCurrentPosition());
                // mVideoView.start();
                int mPostion = getIntent().getIntExtra(Constants.MovieExtra.EXTRA_MOVIE_POSTION, 0);
                Log.d(TAG, "mPostion: " + mPostion);
                mVideoView.seekTo(mPostion);
                mVideoView.start();
            }
            if (getIntent().hasExtra(Constants.MovieExtra.EXTRA_MOVIE_THUMB)) {
                mMovieThumb = getIntent().getExtras().getString(Constants.MovieExtra.EXTRA_MOVIE_THUMB);
            }
        } else if (getIntent().hasExtra(Constants.MovieExtra.EXTRA_MOVIE_ID)) {
            movieId = getIntent().getExtras().getString(Constants.MovieExtra.EXTRA_MOVIE_ID);
            Log.d(TAG, "forplay video movieID: " + movieId);
            VolleyTools tools = VolleyTools.getInstance(this.getContext());
            tools.addToRequestQueue(new StringRequest(Method.GET, Constants.ServerConfig.MOVIE_DETAIL_URL + "&movie_id=" + movieId, getMovieDetailListener), tools.VolleyTAG);

            // 判断是否是收藏的视频,显示不同的收藏状态图片
            boolean isFav = FavDbUtil.getInstance(getContext()).isFavVideo(movieId);
            isSelected = isFav;
            Log.d(TAG, "The fav video is -->> " + isFav);
            if (isFav) {
                image_fav.setImageResource(R.drawable.vedio_fav_pressed);
            } else {
                image_fav.setImageResource(R.drawable.vedio_fav_normal);
            }
        } else {
            ToastUtils.ToastAdd(getContext(), "视频播放地址为空");
            /*
             * mVideoView.setVideoURI(Uri.parse(
             * "http://fx.72zhe.com/static/fx/sample.mp4")); videoUri =
             * "http://fx.72zhe.com/static/fx/sample.mp4";
             * 
             * mVideoView.requestFocus(); updateShareTVdatabase(videoUri,
             * mVideoView.getCurrentPosition()); mVideoView.start();
             */
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("renjun1", "activity destory 1111");
        // unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBinder = (DownloadManagerService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    };

    private void updateShareTVdatabase(Movie movie, int postion) {
        // TODO Auto-generated method stub
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            Log.d("renjun1", "111111111");
            // SQLiteDatabase db = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();
            mCursor = db.query(FlyShareTVdataHelper.TBL_HISTORY_VIDEOS, new String[] { "_id,_uri" }, "_id=?", new String[] { movie.getData().getMovieId() }, null, null, null);
            ContentValues values = new ContentValues();
            String nowTime = getCurrentTime();
            String filename = movie.getData().getMovieName();
            String thumb = movie.getData().getThumb();
            Log.d("renjun1", "filename:" + filename);
            Log.d("renjun1", "videoUri " + videoUri);
            Log.d("renjun1", "nowTime " + nowTime);
            Log.d("renjun1", "postion:" + postion);
            values.put("_uri", movie.getData().getFilmUrl());
            values.put("_id", movie.getData().getMovieId());
            values.put("_time", nowTime);
            values.put("_pos", postion);
            values.put("_name", filename);
            values.put("_thumb", thumb);
            if (mCursor == null || mCursor.getCount() == 0) {
                Log.d("renjun1", "22222222");
                dbHelper.insert(values);
            } else {
                Log.d("renjun1", "3333333333");
                dbHelper.update(values, movie.getData().getMovieId());
            }
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

    }

    private String getNamefromUri(String Uri) {
        // TODO Auto-generated method stub
        String mName = Uri.substring(Uri.lastIndexOf("/") + 1);
        Log.d("renjun1", "mName: " + mName);
        return mName.substring(0, mName.lastIndexOf("."));
    }

    private String getCurrentTime() {
        // TODO Auto-generated method stub
        long now = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(now);
        String mData = null;
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DATE);
        if (month <= 9) {
            if (day <= 9) {
                mData = year + "" + "0" + month + "0" + day;
            } else {
                mData = year + "" + "0" + month + day;
            }
        } else {
            mData = year + "" + month + "" + day;
        }

        return mData;
    }

    private void initView() {

        mImgFollowMe = (ImageView) findViewById(R.id.image_notice);
        mImgFollowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCatch) {
                    catchMeRequest();
                } else {
                    dropCatch();
                }
            }
        });
        // mImgFav = (ImageView) findViewById(R.id.image_fav);
        // mImgFav.setImgStateChangeListener(this);

        mRadioGroup = (FlowRadioGroup) this.findViewById(R.id.video_detail_radiogroup);
        mRadioGroup.setOnCheckedChangeListener(mRadioCheckedChangeListener);
        mRadioGroup.getRadioButton(0).setChecked(true);
        bar = (ProgressBar) findViewById(R.id.videoplayerPreloader);
        video_down_layout = (RelativeLayout) findViewById(R.id.video_down_layoutx);
        mVideoView = (cn.fxdata.tv.view.video.FullScreenVideoView) findViewById(R.id.video_viewx);
        mDownloadImage = (ImageView) findViewById(R.id.image_download);
        mDownloadImage.setOnClickListener(this);
        btnShare = (ImageView) findViewById(R.id.image_share);
        btnShare.setOnClickListener(this);
        image_fav = (ImageView) findViewById(R.id.image_fav);
        image_fav.setOnClickListener(this);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Log.d("renjun1", "download 11111111111");
            // DownloadVideoManager mDmger = new DownloadVideoManager(context);
            VideoItem vitem = new VideoItem();
            vitem.setVideoUri(videoUri);
            /*
             * Intent intent = new Intent(); //intent.setClass(context,
             * DownloadManagerService.class); intent.setClass(context,
             * OwnerMsgActivity.class);
             * intent.setAction("shareTv_download_progress"); Bundle bud = new
             * Bundle(); bud.putSerializable("videoRes", vitem);
             * intent.putExtras(bud); startActivity(intent);
             */
            // mBinder.addVideoToDownQuee(vitem);
            // vitem.setVideoUri(videoUri);
            // mDmger.setRemotePath(videoUri);
            // mDmger.addVideoToDownQuee(vitem);
        }
    };

    private void catchMeRequest() {
        VolleyTools tools = VolleyTools.getInstance(this.getContext());
        tools.addToRequestQueue(new StringRequest(Method.POST, Constants.ServerConfig.FOLLOW_ME_URL2, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess" + response);
                isCatch = true;
                mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_pressed);
                ToastUtils.ToastAdd(getContext(), "追到手拉，快去来追我列表看看");
                // Toast.makeText(getContext(), "追剧成功",
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                isCatch = false;
                mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_normal);
                ToastUtils.ToastAdd(getContext(), "追剧失败");
                // Toast.makeText(getContext(), "追剧失败，请重试",
                // Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError");
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", getUserId());
                return map;
            }
        }, tools.VolleyTAG);
    }

    private void dropCatch() {
        VolleyTools tools = VolleyTools.getInstance(this.getContext());
        tools.addToRequestQueue(new StringRequest(Method.POST, Constants.ServerConfig.DROP_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "the response --> " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("error_code") == 0) {
                        isCatch = false;
                        mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_normal);
                        ToastUtils.ToastAdd(getContext(), "弃追成功！");
                    } else {
                        isCatch = true;
                        mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_pressed);
                        ToastUtils.ToastAdd(getContext(), "弃追失败，请重试！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                isCatch = true;
                mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_pressed);
                Toast.makeText(getContext(), "弃追失败，请重试！", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "the response --> " + error.toString());
            }

            @Override
            public void onCancel() {
                isCatch = true;
                mImgFollowMe.setBackgroundResource(R.drawable.vedio_followme_pressed);
                Toast.makeText(getContext(), "弃追失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("chase_id", movieId);
                return map;
            }
        }, tools.VolleyTAG);
    }

    private CommentFragment mCommentFragment = new CommentFragment();
    private EpisodeFragment mEpisodeFragment = new EpisodeFragment();
    private DetailFragment mDetailFragment = new DetailFragment();

    private void initFragments() {
        fragmentManager = new UIFragmentManager(this, R.id.video_detail_content, getSupportFragmentManager());
        fragmentManager.put("video_detail_comments", mCommentFragment);
        fragmentManager.put("video_detail_channel", mEpisodeFragment);
        fragmentManager.put("video_detail_description", mDetailFragment);
    }

    /*
     * 有可能在radioCheckedChangeListener中早于mCurrentMovieDetail
     * 初始化完成就调用了,但是mCurrentMovieDetail为null,会出错, 因此用这个标志作为判断
     */
    boolean mNeedShowChildFragmentLater = false;

    private void showChildFragment(int id) {
        if (null == mCurrentMovieDetail) {
            mNeedShowChildFragmentLater = true;
            return;
        }
        switch (id) {
        case R.id.video_detail_comment:
            if (null != mCommentFragment && null == mCommentFragment.getArguments()) {
                // 将MovieId传递给评论Fragment
                Bundle bundle = new Bundle();
                // 这里的调用有可能在mCurrentMovieDetail初始化前,
                // 因此需要在mCurrentMovieDetail调用完成后再调用它一次
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_ID, mCurrentMovieDetail.getData().getMovieId());
                mCommentFragment.setArguments(bundle);
            }
            fragmentManager.show("video_detail_comments");
            fragmentId = 0;
            break;
        case R.id.video_detail_channel:
            fragmentManager.show("video_detail_channel");
            fragmentId = 1;
            break;
        case R.id.video_detail_description:
            if (null != mCurrentMovieDetail && null != mDetailFragment && null == mDetailFragment.getArguments()) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_AREA, mCurrentMovieDetail.getData().getArea());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_DETAIL, mCurrentMovieDetail.getData().getDetail());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_ID, mCurrentMovieDetail.getData().getMovieId());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_NAME, mCurrentMovieDetail.getData().getMovieName());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_PATH, mCurrentMovieDetail.getData().getFilmUrl());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_RELEASE_TIME, mCurrentMovieDetail.getData().getReleaseTime());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_TYPE, mCurrentMovieDetail.getData().getType());
                bundle.putString(Constants.MovieExtra.EXTRA_MOVIE_THUMB, mMovieThumb);
                mDetailFragment.setArguments(bundle);
            }
            fragmentManager.show("video_detail_description");
            fragmentId = 2;
            break;
        }
    }

    /**
     * tab button checked listener
     */
    private OnCheckedChangeListener mRadioCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            showChildFragment(checkedId);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("jiangtao4", "activity keydown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            // if the screen orientation is landscape now
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // set it portait!!!
                setScreenOrientation(false, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            } else {
                updateShareTVdatabase(mCurrentMovieDetail, mVideoView.getCurrentPosition());
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void calculateVideoViewSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (metrics.widthPixels < metrics.heightPixels) {
            height = (int) (metrics.heightPixels * (3 / (8 * 1.0f)));
            width = metrics.widthPixels;
        } else {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // calculateVideoViewSize();
        // mVideoView.setDimensions(width, height);
        // mVideoView.getHolder().setFixedSize(width, height);
        Log.i(TAG, " == onConfigurationChanged");
        String screen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横屏" : "竖屏";
        Toast.makeText(this, screen, Toast.LENGTH_LONG).show();
        bar.refreshDrawableState();
    }

    public void setScreenOrientation(boolean isFullScreen, int screenOritation) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (isFullScreen) {
            Log.i("jiangtao4", "activity onToggleFullScreen");
            video_down_layout.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mVideoView.setLayoutParams(layoutParams);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            mVideoView.setDimensions(height, width);
        } else {
            video_down_layout.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200);
            mVideoView.setLayoutParams(layoutParams);
            // 屏幕旋转后，宽高相反
            height = (int) (metrics.widthPixels * (3 / (8 * 1.0f)));
            width = metrics.heightPixels;
            mVideoView.setDimensions(width, height);
        }
        mVideoView.setIsFullScreen(isFullScreen);
        setRequestedOrientation(screenOritation);
    }

    @Override
    public void onToggleFullScreen(boolean isFullScreen) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (isFullScreen) {
            setScreenOrientation(isFullScreen, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setScreenOrientation(isFullScreen, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onImageStateChange(boolean isSelected, View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
        case R.id.image_notice:
            if (isSelected) {
                ToastUtils.ToastAdd(this, "追到手拉，快去来追我列表看看");
            } else {
                ToastUtils.ToastRemove(this, "我被甩啦");
            }
            break;
        case R.id.image_fav:
            if (isSelected) {
                ToastUtils.ToastAdd(this, "收藏成功");
            } else {
                ToastUtils.ToastRemove(this, "取消收藏");
            }
            // updateFavState(videoUri, isSelected);
            break;
        }
    }

    private void updateFavState(String uri, boolean isSelected) {
        // TODO Auto-generated method stub
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            Log.d("renjun1", "111111111");
            db = dbHelper.getWritableDatabase();
            mCursor = db.query("play_history", new String[] { "_id,_uri" }, "_uri=?", new String[] { videoUri }, null, null, null);
            ContentValues values = new ContentValues();

            Log.d("renjun1", "uri " + uri);
            values.put("_uri", videoUri);
            values.put("_fav", isSelected ? 1 : 0);
            if (mCursor == null || mCursor.getCount() == 0) {
                Log.d("renjun1", "22222222");
                dbHelper.insert(values);
            } else {
                Log.d("renjun1", "3333333333");
                dbHelper.update(values, uri);
            }
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    protected void onPause() {
        mVideoView.pause();
        updateShareTVdatabase(mCurrentMovieDetail, mVideoView.getCurrentPosition());
        Log.d(TAG, "mVideoView.getCurrentPosition():" + mVideoView.getCurrentPosition());
        Log.d(TAG, "moveid:" + mCurrentMovieDetail.getData().getMovieId());
        super.onPause();

    }

    private Listener<String> getMovieDetailListener = new Listener<String>() {
        @Override
        public void onSuccess(String response) {
            Log.d(TAG, "the response --> " + response);
            Movie movie = MJsonUtil.gson.fromJson(response, new TypeToken<Movie>() {
            }.getType());
            if (movie.getErrorCode() == 0) {
                mCurrentMovieDetail = movie;
                videoUri = movie.getData().getFilmUrl();
                String title = movie.getData().getMovieName();
                Log.d(TAG, "reponse video url: " + videoUri + " the name -->> " + title);
                mVideoView.setVideoURI(Uri.parse(videoUri), title);

                mVideoView.requestFocus();
                // updateShareTVdatabase(movie,
                // mVideoView.getCurrentPosition());
                int mPostion = getIntent().getIntExtra(Constants.MovieExtra.EXTRA_MOVIE_POSTION, 0);
                Log.d(TAG, "mPostion: " + mPostion);
                mVideoView.seekTo(mPostion);
                mVideoView.start();

                // 需要更新底部的Fragment的信息
                if (mNeedShowChildFragmentLater) {
                    mNeedShowChildFragmentLater = false;
                    Log.d(TAG, "show child fragment later");
                    showChildFragment(mRadioGroup.getCheckedRadioButtonId());
                }
            } else {
                Log.d(TAG, movie.getErrorMsg());
            }
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);
            Log.d(TAG, "the response --> " + error.toString());

            showToast("网络连接错误,无法播放!");

            /*
             * mVideoView.setVideoURI(Uri.parse(
             * "http://fx.72zhe.com/static/fx/sample.mp4")); videoUri =
             * "http://fx.72zhe.com/static/fx/sample.mp4";
             */

            // mVideoView.requestFocus();
            // updateShareTVdatabase(videoUri, mVideoView.getCurrentPosition());
            // mVideoView.start();
        }

        @Override
        public void onCancel() {
            super.onCancel();
            Log.d(TAG, "cancelled");
        }
    };

    @Override
    public void onBackClicked(boolean isFullScreen) {
        // TODO Auto-generated method stub
        finish();
    }

    /**
     * 注意，在这里统一所有的按钮点击处理！！！
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.image_share:
            Log.d(TAG, "share....");
            showShare();
            break;
        case R.id.image_download:
            Log.d(TAG, "download....");
            ToastUtils.ToastAdd(getContext(), "已加入下载队列");
            
            String url = mCurrentMovieDetail.getData().getFilmUrl();
            String id = mCurrentMovieDetail.getData().getMovieId();
            String moviename = mCurrentMovieDetail.getData().getMovieName();
            Log.d(TAG, "Download ------ url -->> " + url + " id --->> " + id + " moviename -->> " + moviename);
            FxDownloadTask fxDownloadTask = new FxDownloadTask(0, 0, 0, url, moviename, id, getApplicationContext(), null, 0);
            // 存入数据库
            fxDownloadTask = fxDownloadTask.getDownloadTaskInfo(url);
            // 判断是否可以从网络得到视频size
            if (fxDownloadTask==null) {
                ToastUtils.ToastAdd(context, "请求视频长度出错，无法下载");
                return ;
            }
            // 判断状态是否是下载中、下载失败等等
            if (fxDownloadTask.state!= DownloadStatus.INIT || fxDownloadTask.state!=DownloadStatus.FAILED ) {
                ToastUtils.ToastAdd(context, "此视频曾经下载过，请勿重复添加");
                return ;
            }


            Log.d(TAG, "Download url -->> " + url +  " status -->> " + fxDownloadTask.state);

            if(FxDownloadTaskManager.getInstance(getApplicationContext()).addDownloadTask(fxDownloadTask)){
                ToastUtils.ToastAdd(context, "此视频已在下载队列，请勿重复添加");
                return;
            }
            // 启动下载管理线程，如果之前启动了，就不用启动了
            FxDownloadService.getService(getApplicationContext()).startManagerThread();
            ToastUtils.ToastAdd(context, "开始正式下载");
/*            VideoItem vitem = new VideoItem();
            vitem.setVideoUri(mCurrentMovieDetail.getData().getFilmUrl());
            vitem.setfileName(mCurrentMovieDetail.getData().getMovieName());
            vitem.setMoveId(mCurrentMovieDetail.getData().getMovieId());
            //DownloadVideoManager mDmger = new DownloadVideoManager(getApplication());
            //mDmger.addVideoToDownQuee(vitem);
            
            Intent intent = new Intent(); 
            intent.setClass(context,DownloadManagerService.class); 
            intent.setAction("cn.fxdata.tv.download_action"); 
            Bundle bud = new Bundle(); 
            bud.putSerializable("videoRes", vitem);
            intent.putExtras(bud); 
            startService(intent);*/
            // mBinder.addVideoToDownQuee(vitem);
            // vitem.setVideoUri(videoUri);
            // mDmger.setRemotePath(videoUri);
            // 
            break;
        case R.id.image_fav:
            Log.d(TAG, "favorate....");
            if (isSelected) {
                isSelected = false;
                if (mCurrentMovieDetail != null) {
                    FavDbUtil.getInstance(getContext()).removeFavVideo(movieId);
                    ToastUtils.ToastAdd(getContext(), "取消收藏");
                    image_fav.setImageResource(R.drawable.vedio_fav_normal);
                }
            } else {
                isSelected = true;
                String movieIdString = movieId;
                if (mCurrentMovieDetail.getData() != null && movieIdString != null) {
                    String movieName = mCurrentMovieDetail.getData().getMovieName();
                    String movieTime = mCurrentMovieDetail.getData().getReleaseTime() == null ? "" : mCurrentMovieDetail.getData().getReleaseTime();
                    // 获得影片的缩略图url，方便加载
                    String movieImageUrl = mCurrentMovieDetail.getData().getFilmUrl();
                    String praiseTimeString = "1212";
                    if (movieName != null) {
                        FavDbUtil.getInstance(getContext()).addUpdateFavVideo(movieIdString, movieName, movieTime, praiseTimeString, movieImageUrl);
                        ToastUtils.ToastAdd(getContext(), "收藏成功");
                        image_fav.setImageResource(R.drawable.vedio_fav_pressed);
                    } else {
                        ToastUtils.ToastAdd(getContext(), "电影名称为空,无法收藏");
                    }
                } else {
                    ToastUtils.ToastAdd(getContext(), "电影数据为空,无法收藏");
                }

            }
            break;

        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}