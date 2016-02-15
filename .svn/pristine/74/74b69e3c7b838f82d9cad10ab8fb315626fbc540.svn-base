package cn.fxdata.tv.adapter;

import cn.fxdata.tv.R;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class ImageWallAdapter extends BaseAdapter {
    private Context context;
    private int[] arrayImage;
    private int width;

    public ImageWallAdapter(Context context, int[] arrayImage) {
        this.context = context;
        this.arrayImage = arrayImage;
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        width = display.getWidth();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayImage.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arrayImage[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(context).inflate(
                R.layout.row_vedio_image_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setLayoutParams(new LayoutParams(width / 2 - 20,
                LayoutParams.WRAP_CONTENT));
        image.setBackgroundResource(arrayImage[position]);
        return view;
    }

}
