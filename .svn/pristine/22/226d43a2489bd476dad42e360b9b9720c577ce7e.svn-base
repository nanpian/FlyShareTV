package cn.fxdata.tv.adapter;

import java.io.File;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import cn.fxdata.tv.R;
import cn.fxdata.tv.bean.NewMoviesReturn.Movies;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.utils.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class MoviesNowAdapter_old extends BaseAdapter {

    private static final String TAG = "MoviesPreAdapter";
    private Context context;
    public List<Movies> moviesList;
    private LayoutInflater inflater;
    private OnMoviesListClickListener onMoviesListClickListener;
    private int width;
    /** 是否正在滑动 */
    private boolean onFling = false;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public void setOnFling(boolean fling) {
        this.onFling = fling;
    }

    public boolean getOnFling() {
        return this.onFling;
    }

    public MoviesNowAdapter_old(Context context, List<Movies> moviessList) {
        this.context = context;
        this.moviesList = moviessList;
        inflater = LayoutInflater.from(context);
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        width = display.getWidth();
        File cacheDir = new File(context.getCacheDir(), "FxCache");
        mQueue = Volley.newRequestQueue(context, new DiskCache(cacheDir));
        mImageLoader = new ImageLoader(mQueue, null, null, null);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return moviesList == null ? 0 : moviesList.size();
    }

    @Override
    public Movies getItem(int item) {
        // TODO Auto-generated method stub
        return moviesList == null ? null : moviesList.get(item);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView,
            ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_vedio_image_item, null);
            holder.movieImage = (ImageView) convertView
                    .findViewById(R.id.image);
            holder.movieImage.setLayoutParams(new LayoutParams(width / 2 - 20,
                    LayoutParams.WRAP_CONTENT));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movies movie = moviesList.get(position);
        Log.d(TAG, "This movie name is " + movie.getName() + " movie url is "
                + movie.getThumb());
        if (false == StringUtils.isNullOrEmpty(movie.getThumb())) {
            holder.movieImage.setTag(position);
            ImageListener imageListener = ImageLoader.getImageListener(
                    holder.movieImage, R.drawable.video_image_03,
                    R.drawable.video_image_03);
            mImageLoader.get(movie.getThumb(), imageListener);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView movieImage;
    }

    public interface OnMoviesListClickListener {
        void onCancelClick(int position);
    }

    public void setOnFollowMeItemClickListener(
            OnMoviesListClickListener listener) {
        this.onMoviesListClickListener = listener;
    }
}