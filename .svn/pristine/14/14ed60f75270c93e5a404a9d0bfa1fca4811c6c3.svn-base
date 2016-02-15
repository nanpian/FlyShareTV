package cn.fxdata.tv.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import cn.fxdata.tv.R;

/**
 * Created by Administrator on 2015/7/11 0011.
 */
public class PopupSeekBarWindow extends PopupWindow implements
        SeekBar.OnSeekBarChangeListener {
    private LayoutInflater mInflater;
    private VerticalSeekBar mSeekBar;
    private LinearLayout mSeekLayout;
    private AudioManager mAudioManager;
    private int mMaxVolume;

    public PopupSeekBarWindow(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        mSeekLayout = (LinearLayout) mInflater.inflate(
                R.layout.voice_seekbar_layout, null);
        // 获取高度和宽度
        mSeekLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mSeekBar = (VerticalSeekBar) mSeekLayout
                .findViewById(R.id.video_voice_seekbar);
        mAudioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekBar.setMax(mMaxVolume);
        mSeekBar.setOnSeekBarChangeListener(this);
        this.setContentView(mSeekLayout);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setAnimationStyle(android.R.style.Animation_Dialog);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void show(View view) {
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mSeekBar != null) {
            mSeekBar.setProgress(volume);
        }
        this.update();
        this.showAsDropDown(view,
                (view.getWidth() - mSeekLayout.getMeasuredWidth()) / 2,
                -(view.getHeight() + mSeekLayout.getMeasuredHeight()));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        mSeekBar.setProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
