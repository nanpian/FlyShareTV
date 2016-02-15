package cn.fxdata.tv.fragment.detail;

import java.io.File;

import com.android.volley.RequestQueue;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;

import cn.fxdata.tv.R;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fxdata.tv.base.BaseFragment;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.http.VolleyRequestManager;

/**
 * Video detail description in video detail activity.
 */
public class DetailFragment extends BaseFragment {
    View mRoot = null;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_detail_detail, container, false);
        mRoot = view;
        Bundle bundle = getArguments();
        parseArguments(bundle);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        final Context context = getContext();
        File cacheDir = new File(context.getCacheDir(), "FxCache");
        mQueue = Volley.newRequestQueue(context, new DiskCache(cacheDir));
        mImageLoader = new ImageLoader(mQueue, null, null, null);
    }

    private void fillDetailItem(int textViewId, String string) {
        TextView tv = (TextView) mRoot.findViewById(textViewId);
        tv.setText(string);
    }

    private void updateMovieDetail(String name, String area, String type, String detail, String releaseTime, String movieThumb) {
        fillDetailItem(R.id.userName, name);
        fillDetailItem(R.id.district, area);
        fillDetailItem(R.id.detail_type, type);
        fillDetailItem(R.id.detail_desc, detail);
        fillDetailItem(R.id.time, releaseTime);
        ImageView iv = (ImageView) mRoot.findViewById(R.id.frag_detail_image);
        ImageListener imageListener = ImageLoader.getImageListener(iv, R.drawable.none, R.drawable.none);
        VolleyRequestManager.getInstance().getImageLoader().get(movieThumb, imageListener);
    }

    private void parseArguments(Bundle bundle) {
        if (null != bundle) {
            String area = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_AREA);
            String detail = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_DETAIL);
            String id = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_ID);
            String name = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_NAME);
            String url = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_PATH);
            String releaseTime = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_RELEASE_TIME);
            String type = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_TYPE);
            String movieThumb = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_THUMB);
            movieThumb = (movieThumb == null) ? "" : movieThumb;
            updateMovieDetail(name, area, type, detail, releaseTime, movieThumb);

            if (true) {
                StringBuilder sb = new StringBuilder();
                sb.append("MovieDetail { ");
                sb.append("\n");
                sb.append("   area : " + area);
                sb.append("\n");
                sb.append("   detail : " + detail);
                sb.append("\n");
                sb.append("   id : " + id);
                sb.append("\n");
                sb.append("   name : " + name);
                sb.append("\n");
                sb.append("   url : " + url);
                sb.append("\n");
                sb.append("   releaseTime : " + releaseTime);
                sb.append("\n");
                sb.append("   type : " + type);
                sb.append("\n");
                sb.append("   movieThumb : " + movieThumb);
                sb.append("\n");
                sb.append("}");
                Log.i("xixia", sb.toString());
            }
        }
    }
}
