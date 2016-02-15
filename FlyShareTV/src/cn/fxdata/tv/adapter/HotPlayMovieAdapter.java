package cn.fxdata.tv.adapter;

import java.io.File;
import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;

import cn.fxdata.tv.R;
import cn.fxdata.tv.bean.HotPlayMoviesReturn.MoviesItem;
import cn.fxdata.tv.fragment.home.HotPlayFragment;
import cn.fxdata.tv.utils.StringUtils;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotPlayMovieAdapter extends BaseAdapter {

    private ArrayList<MoviesItem> mMoviesList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public HotPlayMovieAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        File cacheDir = new File(context.getCacheDir(), "FxCache");
        mQueue = Volley.newRequestQueue(context, new DiskCache(cacheDir));
        mImageLoader = new ImageLoader(mQueue, null, null, null);
    }

    public void setDataAndNotify(ArrayList<MoviesItem> moviesList) {
        if (null != moviesList) {
            Log.i(HotPlayFragment.TAG, "list size is : " + moviesList.size());
        }
        this.mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMoviesList == null ? 0 : mMoviesList.size();
    }

    @Override
    public MoviesItem getItem(int position) {
        if (mMoviesList != null) {
            return mMoviesList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.row_hot_video, null);
            holder.mThumbView = (ImageView) convertView
                    .findViewById(R.id.image);
            holder.mDescription = (TextView) convertView
                    .findViewById(R.id.description);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MoviesItem moviesItem = getItem(position);
        Log.i(HotPlayFragment.TAG, "moviesItem is : " + moviesItem);
        holder.mName.setText(moviesItem.getMovie_name());
        holder.mDescription.setText(moviesItem.getClasses());
        if (false == StringUtils.isNullOrEmpty(moviesItem.getThumb())) {
            holder.mThumbView.setTag(position);
            ImageListener imageListener = ImageLoader.getImageListener(
                    holder.mThumbView, R.drawable.video_image_03,
                    R.drawable.video_image_03);
            mImageLoader.get(moviesItem.getThumb(), imageListener);
        }
        convertView.setTag(R.id.tag_movie_id,
                Integer.valueOf(moviesItem.getMovie_id()));
        convertView.setTag(R.id.tag_movie_thumb, moviesItem.getThumb());
        return convertView;
    }

    public static class ViewHolder {
        public ImageView mThumbView;
        public TextView mName;
        public TextView mDescription;
    }
}
