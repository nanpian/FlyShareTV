package cn.fxdata.tv.adapter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.fxdata.tv.R;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.fxdata.tv.activity.user.PhotoSelectActivity;
import cn.fxdata.tv.utils.ImageUtill;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> ImageItem = new ArrayList<String>();
    private ViewHolder viewHolder;
    private float scaleWidth = 1;
    private float scaleHeight = 1;
    private int mViewscale;
    SharedPreferences sharedPreferences;
    private Handler mHandler;
    public static final int SET_PHOTO_IMAGE = 2;
    public static final int CAMERA_CAPTURE = 3;
    ImageUtill imUt;

    public PhotoAdapter(Context context, ArrayList<String> Item, int viewscale,
            Handler handler) {
        mContext = context;
        ImageItem = Item;
        mViewscale = viewscale;
        sharedPreferences = mContext.getSharedPreferences(
                "PhotoSelectActivity", Context.MODE_PRIVATE);
        mHandler = handler;
        imUt = new ImageUtill();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ImageItem.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ImageItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final String path = ImageItem.get(position);
        final ViewHolder viewHolder;
        Bitmap bmp = null;
        if (convertView == null) {
            // convertView =
            // LayoutInflater.from(mContext).inflate(R.layout.photo_image,null);
            viewHolder = new ViewHolder();
            viewHolder.photoImage = new ImageView(mContext);
            viewHolder.photoImage.setLayoutParams(new LayoutParams(mViewscale,
                    mViewscale));
            viewHolder.photoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.photoImage.setPadding(2, 2, 2, 2);
            // viewHolder.photoImage.setBackgroundColor(R.color.black);
            convertView = viewHolder.photoImage;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0 && path.equals("camera_capture")) {
            viewHolder.photoImage.setImageResource(R.drawable.ic_switch_camera);
            viewHolder.photoImage
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            try {
                                File dir = new File(getSDPath());
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                Intent intent = new Intent(
                                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri u = Uri.fromFile(dir);
                                intent.putExtra(
                                        MediaStore.Images.Media.ORIENTATION, 0);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                                intent.putExtra("capturepath", getSDPath());
                                mHandler.obtainMessage(CAMERA_CAPTURE, intent)
                                        .sendToTarget();
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(mContext, "没有找到存储目录",
                                        Toast.LENGTH_LONG);
                            }

                        }
                    });
            ;
        } else {
            viewHolder.photoImage
                    .setImageResource(R.drawable.file_imagegrid_default);
            int viewX = mViewscale;
            int viewY = mViewscale;
            new loadImageTask(viewHolder.photoImage, viewX, viewY)
                    .execute(path);
        }
        // viewHolder.photoImage =
        // (ImageView)convertView.findViewById(R.id.photo_id);

        /*
         * if(bmp != null&&!bmp.isRecycled()){ bmp.recycle(); } bmp =
         * compressImage(path,viewX,viewY); //bmp =
         * BitmapFactory.decodeByteArray(decodeBitmap(path), 0,
         * decodeBitmap(path).length); photoImage.setImageBitmap(bmp);
         */
        return viewHolder.photoImage;
    }

    private class loadImageTask extends AsyncTask<String, Integer, Bitmap> {
        private int viewx;
        private int viewy;
        private final View mTarget;

        public loadImageTask(View target, int viewX, int viewY) {
            // TODO Auto-generated constructor stub
            this.mTarget = target;
            this.viewx = viewX;
            this.viewy = viewY;
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            Bitmap bmp = null;
            bmp = imUt.compressImage(arg0[0], viewx, viewy);

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Log.i(TAG, "onPostExecute(Result result) called");
            ((ImageView) mTarget).setImageBitmap(result);
            final Bitmap resultbmp = result;
            mTarget.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        Log.d("renjun1", "onclick save");
                        SaveCompressImage(resultbmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void SaveCompressImage(Bitmap bmp) throws Exception {
        String filepath = getSDPath();
        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
        File myCurrentFile = new File(filepath);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCurrentFile));
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        Log.d("renjun1", "onclick save sendmessage");
        mHandler.obtainMessage(SET_PHOTO_IMAGE, filepath).sendToTarget();
    }

    private String getSDPath() {
        // TODO Auto-generated method stub
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            String imagepath = Environment.getExternalStorageDirectory() + "/"
                    + "compressimage.png";
            return imagepath;
        }
        Toast.makeText(mContext, "请坚持SD卡", Toast.LENGTH_LONG);
        return sdDir.toString();
    }

    private class ViewHolder {
        private ImageView photoImage;
    }

}
