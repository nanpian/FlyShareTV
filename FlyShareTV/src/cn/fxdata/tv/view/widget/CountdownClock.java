package cn.fxdata.tv.view.widget;

import cn.fxdata.tv.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountdownClock extends LinearLayout {

    private Context mContext;
    private static long COUNT_DOWN_INTERVAL = 1000L;
    private CountdownClockListener mListener;

    private TextView hour_h;
    private TextView hour_l;
    private TextView mins_h;
    private TextView mins_l;

    public CountdownClock(Context context) {
        this(context, null);
    }

    public CountdownClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View v = inflate(mContext, R.layout.count_down_clock, this);
        hour_h = (TextView) v.findViewById(R.id.cd_clock_hh_h);
        hour_l = (TextView) v.findViewById(R.id.cd_clock_hh_l);
        mins_h = (TextView) v.findViewById(R.id.cd_clock_mm_h);
        mins_l = (TextView) v.findViewById(R.id.cd_clock_mm_l);

        Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
                "font/RobotoCondensed-Light.ttf");
        hour_h.setTypeface(mTypeFace);
        hour_l.setTypeface(mTypeFace);
        mins_h.setTypeface(mTypeFace);
        mins_l.setTypeface(mTypeFace);
    }

    public void Start(final Object key, long time) {
        setClockText(time);

        new CountDownTimer(time, COUNT_DOWN_INTERVAL) {

            public void onTick(final long millisUntilFinished) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setClockText(millisUntilFinished);
                    }

                });

            }

            public void onFinish() {
                if (mListener != null) {
                    mListener.onFinish(null);
                }

            }
        }.start();
    }

    public void setClockListener(CountdownClockListener l) {
        this.mListener = l;
    }

    private void setClockText(long ms) {
        long mins = ms / 60000;
        int mm = (int) (mins % 60);
        int hh = (int) (mins / 60);

        mins_h.setText(String.valueOf(mm / 10));
        mins_l.setText(String.valueOf(mm % 10));

        hour_h.setText(String.valueOf((hh / 10) % 10));
        hour_l.setText(String.valueOf(hh % 10));
    }

    static public interface CountdownClockListener {
        void onFinish(Object Key);
    }
}
