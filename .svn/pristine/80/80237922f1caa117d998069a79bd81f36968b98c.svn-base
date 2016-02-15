package cn.fxdata.tv.view.widget;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountdownClock_v1 extends TextView {

    private static long COUNT_DOWN_INTERVAL = 1000L;

    private CountdownClockListener mListener;
    private final CountdownClock_v1 mThisView;
    private final Context mContext;

    public CountdownClock_v1(Context context) {
        super(context);
        mThisView = this;
        mContext = context;
    }

    public CountdownClock_v1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mThisView = this;
        mContext = context;
    }

    public CountdownClock_v1(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mThisView = this;
        mContext = context;
    }

    public void Start(final Object key, long time) {
        mThisView.setText(format(time));
        mThisView.setTag(key);
        new CountDownTimer(time, COUNT_DOWN_INTERVAL) {

            public void onTick(final long millisUntilFinished) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mThisView.setText(format(millisUntilFinished));
                    }

                });

            }

            public void onFinish() {
                if (mListener != null) {
                    mListener.onFinish(mThisView.getTag());
                }

            }
        }.start();
    }

    public void setClockListener(CountdownClockListener l) {
        this.mListener = l;
    }

    private String format(long ms) {
        long mins = ms / 60000;
        int mm = (int) (mins % 60);
        int hh = (int) (mins / 60);
        return String.format("%02d:%02d", hh, mm);
    }
}

interface CountdownClockListener {
    void onFinish(Object Key);
}
