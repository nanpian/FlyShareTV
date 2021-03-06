package cn.fxdata.tv.http;

import java.io.File;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.cache.BitmapImageCache;
import com.android.volley.cache.DiskCache;
import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.fxdata.tv.application.FxApplication;
import cn.fxdata.tv.cache.BitmapCache;
import cn.fxdata.tv.cache.ImageCache;
import android.content.Context;

/**
 * Volley request single instance class.
 */
public class VolleyRequestManager {
    private static VolleyRequestManager requestManager;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public VolleyRequestManager(Context context) {
        mRequestQueue = getRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue,
                BitmapCache.getBitmapCache(), null, null);
    }

    /**
     * 获取单例实例
     * 
     * @return
     */
    public static synchronized VolleyRequestManager getInstance() {
        if (requestManager == null)
            requestManager = new VolleyRequestManager(
                    FxApplication.getInstance());
        return requestManager;
    }

    /**
     * 初始化
     * 
     * @param context
     */
    public static void init(Context context) {
        if (requestManager == null)
            requestManager = new VolleyRequestManager(context);
    }

    /**
     * 获取请求队列
     * 
     * @return
     */
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            File cacheDir = new File(context.getCacheDir(), "FxCache");
            mRequestQueue = Volley.newRequestQueue(context, new DiskCache(
                    cacheDir));
        }
        return mRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * 添加Volley请求
     * 
     * @param request
     * @param tag
     */
    public void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        getRequestQueue().add(request);
    }

    /**
     * 根据Tag删除未完成的请求
     * 
     * @param tag
     */
    public void cancelAll(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
