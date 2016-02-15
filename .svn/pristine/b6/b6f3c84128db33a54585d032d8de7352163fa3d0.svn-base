
package com.android.volley;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.cache.BitmapImageCache;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.image.NetworkImageView;
import com.android.volley.toolbox.BaseImageLoader;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class VolleyTools {
    /**
     * Log or request TAG
     */
    public static final String VolleyTAG = "VolleyPatterns";
    public static RequestQueue requestQueue;
    private static ImageLoader mImageLoader;
    private static VolleyTools tools;

    int memoryCacheSize = 5 * 1024 * 1024; // 5MB
    int diskCacheSize = 50 * 1024 * 1024; // 50MB

    private VolleyTools(Context context) {
        File diskCacheDir = new File(context.getCacheDir(), "volley");
        // 1.初始化请求序列
        requestQueue = Volley.newRequestQueue(context, new DiskCache(
                diskCacheDir, diskCacheSize));
        mImageLoader = new ImageLoader(requestQueue, new BitmapImageCache(
                memoryCacheSize), context.getResources(), context.getAssets());
    }

    public static VolleyTools getInstance(Context context) {
        if (tools == null) {
            tools = new VolleyTools(context);
        }
        return tools;
    }

    public void display(String url, ImageView img, int errorRes) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageListener listener = mImageLoader.getImageListener(img, errorRes,
                errorRes);
        mImageLoader.get(url, listener);
    }

    public BaseImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? VolleyTAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        requestQueue.add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     * 
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(VolleyTAG);

        requestQueue.add(req);
    }

    /**
     * 取消网络请求 Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     * 
     * @param tag
     */
    public void cancelPendingRequests() {
        if (requestQueue != null) {
            VolleyLog.v("正在取消网络操作……");
            requestQueue.cancelAll(VolleyTAG);
        }
    }
}
