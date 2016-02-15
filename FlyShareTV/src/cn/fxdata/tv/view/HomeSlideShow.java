package cn.fxdata.tv.view;

import java.io.File;
import java.util.ArrayList;

import com.android.volley.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.Request.Method;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;
import com.google.gson.reflect.TypeToken;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.video.ForplayVideoViewActivity;
import cn.fxdata.tv.bean.hotplaybroadcast.Hotplaybroadcast;
import cn.fxdata.tv.bean.movie.Movie;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.utils.MJsonUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 首页的SlideShow View
 * 
 * @author zhaoxin5
 * 
 */
@SuppressLint("NewApi")
public class HomeSlideShow extends RelativeLayout implements
        HomeSlideShowPager.OnSingleTouchListener {

    private Context mContext = null;

    public HomeSlideShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        View root = LayoutInflater.from(context).inflate(
                R.layout.main_slide_show, this, true);
        mContext = context;
        Log.i(TAG, "HomeSlideShow Create 1");
        setupSlideShowData();
    }

    public HomeSlideShow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        View root = LayoutInflater.from(context).inflate(
                R.layout.main_slide_show, this, true);
        mContext = context;
        Log.i(TAG, "HomeSlideShow Create 2");
        setupSlideShowData();
    }

    private final static int sSlideShowTimely = 2500;
    private HomeSlideShowPager mHomeViewPager = null;
    private TextView mHomeIndicatortitle = null;
    // private ImageLoader mImageLoader = null;
    private HomePagerAdapter mHomePagerAdapter = null;
    private View mIndicator0, mIndicator1, mIndicator2;
    private final static String TAG = "HomeSlideShow";
    // private RequestQueue mQueue;

    private Handler mSlideShowHandler = new Handler();
    private Runnable mSlideShowRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            int next = mHomeViewPager.getCurrentItem() + 1;
            if (next >= mSlideShows.size())
                next = 0;
            mHomeViewPager.setCurrentItem(next, true);
            // Log.i(TAG, "timely change the slideshow current item : " + next);
            // mSlideShowHandler.postDelayed(this, sSlideShowTimely);
        }
    };

    private void setupSlideShowViews() {
        // 初始化Views
        // mHomeSlideShow = (RelativeLayout)
        // this.findViewById(R.id.mHome_slide_show);
        mHomeViewPager = (HomeSlideShowPager) this
                .findViewById(R.id.mHome_viewpager);
        mHomeViewPager.setOnSingleTouchListener(this);
        mHomeIndicatortitle = (TextView) this
                .findViewById(R.id.mHome_indicatortitle);
        mIndicator0 = this.findViewById(R.id.mHome_indicator_0);
        mIndicator1 = this.findViewById(R.id.mHome_indicator_1);
        mIndicator2 = this.findViewById(R.id.mHome_indicator_2);
    }

    private final class HomePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mSlideShows.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            if (position < getCount()) {
                ((ViewPager) container)
                        .removeView(mSlideShows.get(position).view);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) container).addView(mSlideShows.get(position).view);
            return mSlideShows.get(position).view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
    }

    private void updateTitleAndIndicator(int position) {
        mCurrentSlideShow = mSlideShows.get(position);
        String str = mSlideShows.get(position).title;
        mHomeIndicatortitle.setText(str);
        Drawable sel = this.getResources().getDrawable(R.drawable.dra_dot_red);
        Drawable unsel = this.getResources().getDrawable(
                R.drawable.dra_dot_white);
        switch (position) {
        case 0:
            mIndicator0.setBackground(sel);
            mIndicator1.setBackground(unsel);
            mIndicator2.setBackground(unsel);
            break;
        case 1:
            mIndicator0.setBackground(unsel);
            mIndicator1.setBackground(sel);
            mIndicator2.setBackground(unsel);
            break;
        case 2:
            mIndicator0.setBackground(unsel);
            mIndicator1.setBackground(unsel);
            mIndicator2.setBackground(sel);
            break;
        default:
            break;
        }
    }

    private class HomePageChangeListener implements OnPageChangeListener {
        /**
         * 当ViewPager中页面的状态发生改变时调用
         */
        public void onPageSelected(int position) {
            updateTitleAndIndicator(position);
            /*
             * mSlideShowHandler.removeCallbacks(mSlideShowRunnable);
             * mSlideShowHandler.postDelayed(mSlideShowRunnable,
             * sSlideShowTimely);
             */
            startAutoSlide();
        }

        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 1) {
                // 正在滑动
                /* mSlideShowHandler.removeCallbacks(mSlideShowRunnable); */
                stopAutoSlide();
            }
            if (arg0 == 2) {
                // 滑动结束
                /*
                 * mSlideShowHandler.removeCallbacks(mSlideShowRunnable);
                 * mSlideShowHandler.postDelayed(mSlideShowRunnable,
                 * sSlideShowTimely);
                 */
                startAutoSlide();
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    private void stopAutoSlide() {
        mSlideShowHandler.removeCallbacks(mSlideShowRunnable);
    }

    private void startAutoSlide() {
        mSlideShowHandler.removeCallbacks(mSlideShowRunnable);
        mSlideShowHandler.postDelayed(mSlideShowRunnable, sSlideShowTimely);
    }

    private void setupSlideShowData() {
        // mQueue = Volley.newRequestQueue(this);
        // mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        File cacheDir = new File(getContext().getCacheDir(), "FxCache");
        mQueue = Volley.newRequestQueue(getContext(), new DiskCache(cacheDir));
        mImageLoader = new ImageLoader(mQueue, null, null, null);

        setupSlideShowViews();
        setData();
        mSlideShowHandler.removeCallbacks(mSlideShowRunnable);
        mSlideShowHandler.postDelayed(mSlideShowRunnable, sSlideShowTimely);
    }

    @Override
    public void onSingleTouch(View v) {
        // TODO Auto-generated method stub
        if (null != mCurrentSlideShow) {
            Log.i(TAG, "movieId : " + mCurrentSlideShow.movieId + " , thumb : "
                    + mCurrentSlideShow.thumb);
            startForplayVideoActivity(
                    Integer.valueOf(mCurrentSlideShow.movieId),
                    mCurrentSlideShow.thumb);
        } else if (v instanceof HomeSlideShowPager) {
            // start the video detail fragment
            Intent intent = new Intent(mContext, ForplayVideoViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private ArrayList<SlideShow> mSlideShows = new ArrayList<HomeSlideShow.SlideShow>();

    private void setData() {
        // getBroadcastFromServer();

        /*
         * Drawable d =
         * getContext().getResources().getDrawable(R.drawable.hot_adv_image);
         * mSlideShows.add(new SlideShow(d, "First", 1)); mSlideShows.add(new
         * SlideShow(d, "Second", 2)); mSlideShows.add(new SlideShow(d, "Third",
         * 3));
         * 
         * mHomePagerAdapter = new HomePagerAdapter();
         * mHomeViewPager.setAdapter(mHomePagerAdapter);
         * mHomeViewPager.setOnPageChangeListener(new HomePageChangeListener());
         */
    }

    public void getBroadcastFromServer(int schoolId) {
        VolleyTools tools = VolleyTools.getInstance(getContext());
        // 根据不同学校的Id返回
        String requestUrl = Constants.ServerConfig.HOT_BROADCAST_URL
                + "&school_id=" + schoolId;
        StringRequest hotRequest = new StringRequest(Method.GET, requestUrl,
                new Listener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "data response --> " + response.toString());
                        Hotplaybroadcast hBroadCast = MJsonUtil.gson.fromJson(
                                response, new TypeToken<Hotplaybroadcast>() {
                                }.getType());
                        if (hBroadCast.getErrorCode() == 0) {
                            if (hBroadCast.getData() != null
                                    && hBroadCast.getData().getAdvertisement() != null
                                    && hBroadCast.getData().getAdvertisement()
                                            .size() > 0) {
                                mSlideShows.clear();
                                for (int i = 0; i < hBroadCast.getData()
                                        .getAdvertisement().size(); i++) {
                                    mSlideShows.add(new SlideShow(hBroadCast
                                            .getData().getAdvertisement()
                                            .get(i).getMovieId(), (i + 1),
                                            hBroadCast.getData()
                                                    .getAdvertisement().get(i)
                                                    .getThumb()));
                                }
                                if (null == mHomePagerAdapter) {
                                    mHomePagerAdapter = new HomePagerAdapter();
                                } else {
                                    mHomePagerAdapter.notifyDataSetChanged();
                                }
                                mHomeViewPager.setAdapter(mHomePagerAdapter);
                                mHomeViewPager.setCurrentItem(0);
                                mHomeViewPager
                                        .setOnPageChangeListener(new HomePageChangeListener());
                                startAutoSlide();
                                updateTitleAndIndicator(0);
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

    public void startForplayVideoActivity(int movieId, final String movieThumb) {
        VolleyTools tools = VolleyTools.getInstance(mContext);
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

    public void showActivity(Class<?> cls) {
        mContext.startActivity(new Intent(mContext, cls));
    }

    public void showActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }

    /** 记录当前的电影信息 */
    private SlideShow mCurrentSlideShow = null;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;

    class SlideShow {
        Drawable drawable;
        String title;
        int index;
        ImageView view;
        String thumb;
        String movieId;

        SlideShow(Drawable d, String t, int i) {
            drawable = d;
            title = t;
            index = i;
            view = new ImageView(getContext());
            view.setBackground(drawable);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }

        SlideShow(String id, int i, String thumb) {
            this.movieId = id;
            this.title = "MovieId " + id;
            index = i;
            this.thumb = thumb;
            view = new ImageView(getContext());
            view.setScaleType(ScaleType.FIT_XY);
            ImageListener imageListener = ImageLoader.getImageListener(view,
                    R.drawable.video_image_03, R.drawable.video_image_03);
            mImageLoader.get(thumb, imageListener);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
    }
}
