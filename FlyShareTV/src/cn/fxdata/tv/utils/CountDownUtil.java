package cn.fxdata.tv.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

public class CountDownUtil {

    public static final String TAG = "FXTimer";

    private MyCountdownTimer myCountdownTimer;
    private int what = MIAOSHA_BEGINING;
    private boolean isStop = true;

    /** 即将开始 */
    public final static int MIAOSHA_WILLBEGIN = 1;
    /** 秒杀进行中 */
    public final static int MIAOSHA_BEGINING = 2;
    /** 活动结束 */
    public final static int MIAOSHA_FINISH = 3;

    public void setHMS(long m) {
        if (myCountdownTimer != null) {
            myCountdownTimer.reset(m, 1000, what);
        }
    }

    public long getCountdownTime(long startRemainTime, long endRemainTime) {
        long countdownTime = 0;
        if (startRemainTime > 0) {// TODO
            countdownTime = startRemainTime;
            what = MIAOSHA_WILLBEGIN;
        } else if (endRemainTime > 0 && startRemainTime < 0) {
            countdownTime = endRemainTime;
            what = MIAOSHA_BEGINING;
        } else if (endRemainTime < 0 && startRemainTime < 0) {
            countdownTime = 1;
            what = MIAOSHA_FINISH;
        }
        return countdownTime;
    }

    /**
     * 倒计时钟
     * 
     * @param startRemainTime
     * @param endRemainTime
     * @return
     */
    public void setCountdown(long startRemainTime, final long endRemainTime,
            final CountDownListener listener) {
        final long countdownTime = getCountdownTime(startRemainTime,
                endRemainTime);
        Log.d(TAG, " -->>setCountdown countdownTime=" + countdownTime);
        if (myCountdownTimer == null) {
            myCountdownTimer = new MyCountdownTimer(countdownTime, 1000, what) {// TODO
                @Override
                public void onTick(long millisUntilFinished, int what) {
                    final long[] hms = toHMS(millisUntilFinished);
                    if (listener != null) {
                        listener.changed(this, millisUntilFinished, hms, what);
                    }
                }

                @Override
                public void onFinish(int what) {
                    if (listener != null) {
                        listener.finish(this, endRemainTime, what);
                    }
                    countdownCancel();
                }
            }.start();
        } else {
            myCountdownTimer.reset(countdownTime, 1000, what);
        }
        isStop = false;
    }

    public void countdownCancel() {
        if (myCountdownTimer != null) {
            isStop = true;
            myCountdownTimer.cancel(MIAOSHA_BEGINING);
            myCountdownTimer.cancel(MIAOSHA_WILLBEGIN);
            myCountdownTimer.cancel(MIAOSHA_FINISH);

        }
    }

    public void resetTime(long countdownTime) {
        if (myCountdownTimer != null && countdownTime > 0) {
            isStop = false;
            myCountdownTimer.reset(countdownTime, 1000, what);
        }
    }

    public long[] toHMS(long ms) {
        long s;// 秒
        long h;// 小时
        long m;// 分钟
        h = ms / 1000 / 60 / 60;
        m = (ms - h * 60 * 60 * 1000) / 1000 / 60;
        s = ms / 1000 - h * 60 * 60 - m * 60;
        h = h < 0 ? 0 : h;
        m = m < 0 ? 0 : m;
        s = s < 0 ? 0 : s;
        return new long[] { h, m, s };
    }

    public boolean isStop() {
        return isStop;
    }

    public interface CountDownListener {
        public boolean finish(MyCountdownTimer timer, long endRemainTime,
                int what);

        public void changed(MyCountdownTimer timer, long millisUntilFinished,
                long[] hms, int what);
    }
}
