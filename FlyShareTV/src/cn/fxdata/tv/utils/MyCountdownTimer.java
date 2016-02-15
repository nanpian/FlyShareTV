package cn.fxdata.tv.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Schedule a countdown until a time in the future, with regular notifications
 * on intervals along the way.
 * 
 * Example of showing a 30 second countdown in a text field:
 * 
 * <pre class="prettyprint">
 * new CountdownTimer(30000, 1000) {
 * 
 *     public void onTick(long millisUntilFinished) {
 *         mTextField.setText(&quot;seconds remaining: &quot; + millisUntilFinished / 1000);
 *     }
 * 
 *     public void onFinish() {
 *         mTextField.setText(&quot;done!&quot;);
 *     }
 * }.start();
 * </pre>
 * 
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete. This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
public abstract class MyCountdownTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private long mCountdownInterval;

    private long mStopTimeInFuture;

    private int what;

    /**
     * @param millisInFuture
     *            The number of millis in the future from the call to
     *            {@link #start()} until the countdown is done and
     *            {@link #onFinish()} is called.
     * @param countDownInterval
     *            The interval along the way to receive {@link #onTick(long)}
     *            callbacks.
     */
    public MyCountdownTimer(long millisInFuture, long countDownInterval,
            int what) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
        this.what = what;
    }

    public synchronized final void reset(long millisInFuture,
            long countDownInterval, int what) {
        if (Log.D) {
            Log.d("MyCountdownTimer", "reset(); mMillisInFuture="
                    + mMillisInFuture + "\tmCountdownInterval="
                    + mCountdownInterval);
        }
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
        this.what = what;
        start();
    }

    /**
     * Cancel the countdown.
     */
    public final void cancel(int what) {
        mHandler.removeMessages(what);
    }

    /**
     * Start the countdown.
     */
    public synchronized final MyCountdownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish(what);
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(what));
        return this;
    }

    /**
     * Callback fired on regular interval.
     * 
     * @param millisUntilFinished
     *            The amount of time until finished.
     */
    // public abstract void onTick(long millisUntilFinished);

    public abstract void onTick(long millisUntilFinished, int what);

    /**
     * Callback fired when the time is up.
     */
    // public abstract void onFinish();

    public abstract void onFinish(int what);

    private static final int MSG = 1;

    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            synchronized (MyCountdownTimer.this) {
                final long millisLeft = mStopTimeInFuture
                        - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish(what);
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(what), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft, what);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval
                            - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0)
                        delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(what), delay);
                }
            }
        }
    };
}
