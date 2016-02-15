package cn.fxdata.tv.activity.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cn.fxdata.tv.R;
import cn.fxdata.tv.R.id;
import cn.fxdata.tv.R.layout;
import cn.fxdata.tv.adapter.FolderAdapter;
import cn.fxdata.tv.adapter.PhotoAdapter;
import cn.fxdata.tv.bean.ImageFloder;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoSelectActivity extends Activity {
    private static final String TAG = "PhotoSelectActivity";
    private static final String CAMERA_TAG = "camera_capture";
    private static final int UPDATE_IMAGE_VIEW = 1;
    public static final int SET_PHOTO_OK = 0;
    private ArrayList<String> mItems = new ArrayList<String>();
    private ArrayList<String> tempItem = new ArrayList<String>();
    private GridView photoView;
    private PhotoAdapter mPhotoAdapter;
    public Handler handler;
    private int screenWidth;
    private int screenHeight;
    private String capturepath;
    private TextView titleView;
    private RelativeLayout settingview;
    private TextView menuText;
    private WindowManager mWgr;
    private PopupWindow mPopupWindow;
    private ArrayList<ImageFloder> imageItem = new ArrayList<ImageFloder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_photo_select);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
        // R.layout.common_title_bar);
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText("图片");
        settingview = (RelativeLayout) findViewById(R.id.setting_icon);
        settingview.setVisibility(View.GONE);
        photoView = (GridView) findViewById(R.id.photo_list);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATE_IMAGE_VIEW) {
                    updateImageView();
                } else if (msg.what == PhotoAdapter.SET_PHOTO_IMAGE) {
                    Intent intent = getIntent();
                    intent.putExtra("selectedpath", (String) msg.obj);
                    setResult(SET_PHOTO_OK, intent);
                    Log.d("renjun1", "selected:" + (String) msg.obj);
                    finish();
                } else if (msg.what == PhotoAdapter.CAMERA_CAPTURE) {
                    Intent mIntent = (Intent) msg.obj;
                    Bundle data = mIntent.getExtras();
                    capturepath = data.getString("capturepath");
                    startActivityForResult(mIntent, 1);
                }
            }
        };
        getPhotoFile();
        // initWindowMenu();
    }

    private void initWindowMenu() {
        // TODO Auto-generated method stub
        mWgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        menuText = new TextView(this);
        menuText.setLayoutParams(new LayoutParams(screenWidth, 130));
        menuText.setText("所有图片");
        menuText.setBackgroundColor(R.color.black);
        // menuText.setAlpha(60);
        menuText.setGravity(Gravity.CENTER_VERTICAL);
        menuText.setPadding(12, 0, 0, 0);
        menuText.setTextColor(R.color.white);
        menuText.setOnClickListener(mListener);
        wmParams.width = screenWidth;
        wmParams.height = 130;
        wmParams.x = 0;
        wmParams.y = screenHeight - 130;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWgr.addView(menuText, wmParams);

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            getPopupWindowInstance();
            showPopupWindowAtup(v);
        }

    };

    private void getPopupWindowInstance() {
        // TODO Auto-generated method stub
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            // showPopupWindowAtup(v);
        }
    }

    protected void showPopupWindowAtup(View v) {
        // TODO Auto-generated method stub
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.GRAY);
        ListView tv = new ListView(this);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tv.setBackgroundColor(Color.WHITE);
        int firsImagescale = screenWidth / 4;
        FolderAdapter mFolderAdapter = new FolderAdapter(this, imageItem,
                firsImagescale);
        tv.setAdapter(mFolderAdapter);
        layout.addView(tv);

        mPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
                2 * screenHeight / 3, true);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setOnDismissListener(onDismissListener);
        photoView.setAlpha(0.5f);
        // WindowManager.LayoutParams params=getWindow().getAttributes();
        // params.alpha=0.4f;
        // getWindow().setAttributes(params);

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        View view = ((ViewGroup) findViewById(android.R.id.content))
                .getChildAt(0);

        mPopupWindow.showAtLocation(view, Gravity.TOP, location[0], location[1]
                - mPopupWindow.getHeight());
    }

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            photoView.setAlpha(1f);
            // WindowManager.LayoutParams params=getWindow().getAttributes();
            // params.alpha=1f;
            // getWindow().setAttributes(params);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        updateImageView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                Uri u = Uri.parse(MediaStore.Images.Media.insertImage(
                        getContentResolver(), capturepath, null, null));
                handler.obtainMessage(PhotoAdapter.SET_PHOTO_IMAGE, capturepath)
                        .sendToTarget();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateImageView() {
        // TODO Auto-generated method stub
        if (mItems != null) {
            tempItem.clear();
            tempItem.addAll(mItems);
        }
        int Imageviewscale = screenWidth / 3;
        mPhotoAdapter = new PhotoAdapter(this, tempItem, Imageviewscale,
                handler);
        photoView.setAdapter(mPhotoAdapter);

    }

    private void getPhotoFile() {
        // TODO Auto-generated method stub
        mItems.add(CAMERA_TAG);
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
        }

        Thread loadImageThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoSelectActivity.this
                        .getContentResolver();
                Cursor mCursor = null;
                try {
                    mCursor = mContentResolver.query(mImageUri, null,
                            MediaStore.Images.Media.MIME_TYPE + "=? or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[] { "image/jpeg", "image/png" },
                            MediaStore.Images.Media.DATE_MODIFIED);
                    Log.d(TAG, "mCursor.count: " + mCursor.getCount());
                    if (mCursor != null && mCursor.getCount() > 0) {
                        mCursor.moveToFirst();
                    }
                    while (mCursor.moveToNext()) {
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        mItems.add(path);
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;
                        String dirPath = parentFile.getAbsolutePath();
                        ImageFloder imageFloder = null;
                        if (isContainFloder(dirPath) != null) {
                            imageFloder = isContainFloder(dirPath);
                            imageFloder.addOneImage();
                        } else {

                            imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                            imageFloder.addOneImage();
                            imageItem.add(imageFloder);
                        }
                    }
                } catch (Exception e) {
                } finally {
                    if (mCursor != null) {
                        mCursor.close();
                    }
                }
                handler.sendEmptyMessage(UPDATE_IMAGE_VIEW);
            }

        });
        loadImageThread.start();

    }

    private ImageFloder isContainFloder(String dirPath) {
        for (int i = 0; i < imageItem.size(); i++) {
            if (imageItem.get(i).getDir().equals(dirPath)) {
                return imageItem.get(i);
            }
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d("renjun1", "destory");
        super.onDestroy();
        if (menuText != null) {
            Log.d("renjun1", "1111111111111");
            // 移除悬浮窗口
            mWgr.removeView(menuText);
        }
    }
}
