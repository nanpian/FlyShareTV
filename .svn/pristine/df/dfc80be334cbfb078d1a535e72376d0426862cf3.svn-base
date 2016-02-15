package cn.fxdata.tv.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import cn.fxdata.tv.utils.FileUtil;
import cn.fxdata.tv.utils.StringUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageFileCache {
    private static final String TAG = "-----ImageFileCache-----";
    private static final String WHOLESALE_CONV = "IMG";

    private static final int MB = 1024 * 1024;
    private static final int CACHE_SIZE = 50;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

    /** 图片质量，100表示最高 */
    private static final int BITMAP_QUALITY = 90;

    private static ImageFileCache imageFileCache;

    public static ImageFileCache getImageFileCache() {
        if (imageFileCache == null)
            imageFileCache = new ImageFileCache();

        return imageFileCache;
    }

    private ImageFileCache() {
        // 清理文件缓存
        removeCache(getDirectory());
    }

    /** 从缓存中获取图片 **/
    public Bitmap getBitmapFromCache(final String url) {
        String path = getDirectory() + convertUrlToFileName(url);

        File file = new File(path);

        if (file.exists()) {
            Bitmap bmp = null;
            BitmapFactory.Options opt = new BitmapFactory.Options();
            try {
                InputStream is = new FileInputStream(path);
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inJustDecodeBounds = false;

                // Bitmap bmp = BitmapDecodeUtil.decodePath(path);
                // Bitmap bmp = BitmapFactory.decodeFile(url, opt);
                bmp = BitmapFactory.decodeStream(is, null, opt);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }

    public Bitmap getBitmapFromCache(final String url, int width, int height) {
        String path = getDirectory() + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = null;
            BitmapFactory.Options opt = new BitmapFactory.Options();
            try {
                InputStream is = new FileInputStream(path);
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inJustDecodeBounds = false;
                // Bitmap bmp = BitmapDecodeUtil.decodePath(path);
                // bmp = BitmapFactory.decodeFile(url, opt);
                bmp = BitmapFactory.decodeStream(is, null, opt);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        } else {
            Log.e("bitmap", "file not exist");
        }
        return null;
    }

    /** 将图片存入文件缓存 **/
    public void saveBitmap(Bitmap bm, String url) {
        if (bm == null) {
            return;
        }
        if (isBitmapExist(url))
            return;
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD空间不足
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        // File dirFile = new File(dir);
        // if (!dirFile.exists())
        // dirFile.mkdirs();
        File file = new File(dir + filename);
        // Log.e("file", "path="+file.getAbsolutePath());
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "FileNotFoundException");
        } catch (IOException e) {
            Log.w(TAG, "IOException");
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 缓存图片是否存在
     * 
     * @param url
     * @return
     */
    private boolean isBitmapExist(String url) {
        String path = getDirectory() + convertUrlToFileName(url);
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除 所有缓存文件
     */
    public void clearCache() {
        File dir = new File(getDirectory());
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * 计算存储目录下的文件大小，
     * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除20%最近没有被使用的文件
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
        if (dirSize > CACHE_SIZE * MB
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.2 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }

        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
        return true;
    }

    /** 修改文件的最后修改时间 **/
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /** 计算sdcard上的剩余空间 **/
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /** 将url转成文件名 **/
    private String convertUrlToFileName(String url) {
        if (StringUtils.isNullOrEmpty(url) == false) {
            return ImageCache.getLogoKey(url);
        } else {
            return "";
        }
        // return MD5Util.getMD5String(url)+WHOLESALE_CONV;
    }

    /**
     * 检查文件是否存在，并删除
     */
    public void checkFileExistAndDelete(String url) {
        String fileName = getDirectory() + convertUrlToFileName(url);
        File file = new File(fileName);
        if (file != null && file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /** 获得缓存目录 **/
    private String getDirectory() {
        return FileUtil.getFolderCache();
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
