package cn.fxdata.tv.application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pinterest.DebugUtil;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.File;

import cn.fxdata.tv.R;
import cn.fxdata.tv.utils.LoginUserInfo;
import cn.fxdata.tv.utils.SharedPreferencePersonUtil;
import cn.fxdata.tv.utils.StringUtils;
import cn.sharesdk.framework.ShareSDK;

public class FxApplication extends Application {

    private static String TAG = "FxApplication";
    protected static FxApplication instance;
    public LoginUserInfo info = null;

    @Override
    public void onCreate() {
        Log.d(TAG,
                "FxApplication onCreate() -->> Process.myPid() "
                        + Process.myPid());
        super.onCreate();
        ImageLoader.getInstance().init(getSimpleImageLoaderConfig(this));
        // 判断是否有登陆信息
        getLoginInfo();
        initShareSdk();
    }

    private void initShareSdk() {
        ShareSDK.initSDK(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    public static synchronized FxApplication getInstance() {
        if (null == instance) {
            instance = new FxApplication();
        }
        return instance;
    }

    private ImageLoaderConfiguration getSimpleImageLoaderConfig(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 6) * 1024 * 1024;
        } else {
            memoryCacheSize = 4 * 1024 * 1024;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnFail(R.drawable.avatar_default)
                .resetViewBeforeLoading(false).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(false)
                .displayer(new SimpleBitmapDisplayer()).build();
        File path = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(400, 400)
                .diskCacheExtraOptions(400, 400, null)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new LruMemoryCache(memoryCacheSize))
                .memoryCacheSizePercentage(13)
                .diskCache(
                        new UnlimitedDiscCache(path, StorageUtils
                                .getCacheDirectory(this, true)))
                .imageDownloader(new BaseImageDownloader(this))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 6) * 1024 * 1024;
        } else {
            memoryCacheSize = 4 * 1024 * 1024;
        }
        // builder = JLog.W ? builder.writeDebugLogs() : builder;
        return builder.build();
    }

    public void setLoginInfo(LoginUserInfo info) {
        if (this.info == null)
            this.info = new LoginUserInfo();
        this.info.setUser_id(info.getUser_id());
        this.info.setAvatar(info.getAvatar());
        this.info.setMobile(info.getMobile());
        this.info.setRole(info.getRole());
        this.info.setPrivate_code(info.getPrivate_code());
        this.info.setUsername(info.getUsername());
        this.info.setEvent_visible(info.getEvent_visible());
    }

    public LoginUserInfo getLoginInfo() {
        if (info == null) {
            SharedPreferencePersonUtil sp = new SharedPreferencePersonUtil(
                    instance);
            if (!StringUtils.isNullOrEmpty(sp.getPersonUserId())) {
                info = new LoginUserInfo();
                info.setUsername(sp.getPersonName());
                info.setMobile(sp.getPersonMobile());
                // 设置wifi下缓存视频的开关
                // info.setEvent_visible(sp.getPersonMeetingVisible() == true ?
                // 1 : -1);
                info.setUser_id(sp.getPersonUserId());
                info.setAvatar(sp.getPersonUserAvatar());
                info.setRole(info.getRole());
                info.setPrivate_code(sp.getPersonUserPrivateCode());
            }
        }
        return info;
    }

}
