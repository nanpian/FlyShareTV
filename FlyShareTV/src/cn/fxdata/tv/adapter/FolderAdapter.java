package cn.fxdata.tv.adapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cn.fxdata.tv.R;
import cn.fxdata.tv.bean.ImageFloder;
import cn.fxdata.tv.utils.ImageUtill;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter {
    private ArrayList<ImageFloder> fileItem = new ArrayList<ImageFloder>();
    private Context mContext;
    private int imageScale;
    ImageUtill imUt;

    public FolderAdapter(Context context, ArrayList<ImageFloder> mItems,
            int scale) {
        mContext = context;
        fileItem = mItems;
        imageScale = scale;
        imUt = new ImageUtill();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fileItem.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return fileItem.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View contentview, ViewGroup arg2) {
        // TODO Auto-generated method stub
        String filename = fileItem.get(arg0).getName();
        String firstpath = fileItem.get(arg0).getFirstImagePath();
        ImageView firstimage;
        TextView flodername;
        if (contentview == null) {
            contentview = LayoutInflater.from(mContext).inflate(
                    R.layout.floder_item, null);

        }
        firstimage = (ImageView) contentview.findViewById(R.id.first_image);
        flodername = (TextView) contentview.findViewById(R.id.file_name);
        flodername.setText(filename);
        new loadFirstImageTask(firstimage, imageScale, imageScale)
                .execute(firstpath);
        return contentview;
    }

    private class loadFirstImageTask extends AsyncTask<String, Integer, Bitmap> {
        private int viewx;
        private int viewy;
        private final View mTarget;

        public loadFirstImageTask(View target, int viewX, int viewY) {
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

        }
    }

}
