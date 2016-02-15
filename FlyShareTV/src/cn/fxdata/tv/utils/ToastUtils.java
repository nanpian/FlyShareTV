package cn.fxdata.tv.utils;

import cn.fxdata.tv.R;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/6/16 0016.
 */
public class ToastUtils {

    public static void ToastAdd(Context context, String message) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout toastLayout = (LinearLayout) inflater.inflate(
                R.layout.toast_select_layout, null);
        toastLayout.setBackgroundResource(R.drawable.btn_send_bg);
        TextView textView = (TextView) toastLayout
                .findViewById(R.id.toast_text);
        textView.setText(message);
        int width = getScreenWidth(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width / 3, width / 8);
        layoutParams.setMargins(20, 20, 20, 20);
        textView.setLayoutParams(layoutParams);

        toast.setView(toastLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void ToastRemove(Context context, String message) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout toastLayout = (LinearLayout) inflater.inflate(
                R.layout.toast_select_layout, null);
        toastLayout.setBackgroundResource(R.drawable.btn_send_bg_gray);
        TextView textView = (TextView) toastLayout
                .findViewById(R.id.toast_text);
        textView.setText(message);
        int width = getScreenWidth(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width / 3, width / 8);
        layoutParams.setMargins(20, 20, 20, 20);
        textView.setLayoutParams(layoutParams);

        toast.setView(toastLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static int getScreenWidth(Context context) {
        int width = 0;
        // 获取LayoutInflater对象
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        // 获得屏幕的宽度
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

        return width;
    }
}
