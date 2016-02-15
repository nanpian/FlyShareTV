package cn.fxdata.tv.view.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.fxdata.tv.R;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.view.widget.PopupSeekBarWindow;

public class VideoControllerView extends FrameLayout implements OnClickListener {
    private static final String TAG = "VideoControllerView";

    private MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private SeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mUseFastForward;
    private boolean mFromXml;
    private boolean mListenersSet;
    private View.OnClickListener mNextListener, mPrevListener;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private ImageButton mPauseButton;
    private ImageButton mFfwdButton;
    private ImageButton mRewButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private ImageButton mFullscreenButton;
    private Handler mHandler = new MessageHandler(this);
    // 播放控制
    private TextView mTextRsolution;
    private TextView mTextSelections;
    public LinearLayout mResolutionSet;
    private TextView mTextASpeed;
    private TextView mTextBSpeed;
    private TextView mTextCSpeed;
    private TextView mTextDSpeed;
    public FrameLayout mContentSelectLayout;
    private ListView mContentList;
    private GridView mContentGrid;
    private LinearLayout mVolumeSetLayout;
    private LinearLayout mBrightnessSetLayout;
    private SeekBar mBrightnessSeekBar;
    private RelativeLayout mTitleLayout;
    private LinearLayout mControlLayout;
    private SeekBar mVolumeSeekBar;
    private LinearLayout mFwdSetLayout;
    private SeekBar mFwdSetSeekBar;
    private TextView mTextFwdCurTime;
    private TextView mTextFwdTime;
    private TextView mTextFwd;
    public int mMinDistance = 0;
    private float mTouchStartX = 0;
    private float mTouchStartY = 0;
    private float mInitPosX = 0;
    private float mLastPosX = 0;
    private float mInitPosY = 0;
    private float mLastPosY = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private DisplayMetrics mDisplayMetrics;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume;
    private float mBrightness = -1f;
    private int mVideoPosition = -1;
    private ImageButton mVideoVoice;
    private ImageButton mVideoNext;
    private PopupSeekBarWindow mVoiceWindow;
    // 设置标题
    private TextView mTitleView;
    // 左上角的回退按钮
    private ImageView btnBackImageView;

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
        mGestureDetector = new GestureDetector(getContext(),
                new VolumeBrightnessDetector());
        mAudioManager = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mMinDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        mWidth = mDisplayMetrics.heightPixels;
        mHeight = mDisplayMetrics.widthPixels;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
        mGestureDetector = new GestureDetector(getContext(),
                new VolumeBrightnessDetector());
        mAudioManager = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mMinDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        mWidth = mDisplayMetrics.heightPixels;
        mHeight = mDisplayMetrics.widthPixels;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context) {
        this(context, true);

        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
        updateFullScreen();
    }

    /**
     * Set the view that acts as the anchor for the control view. This can for
     * example be a VideoView, or your Activity's main view.
     * 
     * @param view
     *            The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(ViewGroup view) {
        mAnchor = view;
        FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // 将当前的view加入surfaceview/mRootView
        mAnchor.addView(this, tlp);
        removeAllViews();
        makeControllerView();
    }

    public void initForwardSetLayout(View v) {
        mFwdSetLayout = (LinearLayout) v.findViewById(R.id.forward_set_layout);
        mFwdSetSeekBar = (SeekBar) v.findViewById(R.id.forward_seekbar);
        mFwdSetSeekBar.setMax(1000);
        mTextFwdCurTime = (TextView) v.findViewById(R.id.forward_time_current);
        mTextFwdTime = (TextView) v.findViewById(R.id.forward_time);
        mTextFwd = (TextView) v.findViewById(R.id.forward_text);
        setForwardProgess();
    }

    public void initBrightnessBar(View v) {
        mBrightnessSetLayout = (LinearLayout) v
                .findViewById(R.id.brightness_set_layout);
        mBrightnessSeekBar = (SeekBar) v.findViewById(R.id.brightness_seekbar);
        // 亮度调节在0-1之间，放大100倍
        mBrightnessSeekBar.setMax(100);
        mBrightnessSeekBar
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        WindowManager.LayoutParams lpa = ((Activity) mContext)
                                .getWindow().getAttributes();
                        lpa.screenBrightness = progress / 100;
                        if (lpa.screenBrightness > 1.0f)
                            lpa.screenBrightness = 1.0f;
                        else if (lpa.screenBrightness < 0.01f)
                            lpa.screenBrightness = 0.01f;
                        ((Activity) mContext).getWindow().setAttributes(lpa);
                        mBrightnessSeekBar.setProgress(progress);
                    }
                });
    }

    public void initVolumeBar(View v) {
        mVolumeSetLayout = (LinearLayout) v
                .findViewById(R.id.volume_set_layout);
        mVolumeSeekBar = (SeekBar) mVolumeSetLayout
                .findViewById(R.id.volume_seekbar);
        mVolumeSeekBar.setMax(mMaxVolume);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mVolumeSeekBar.setProgress(mVolume);
        mVolumeSeekBar
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        mAudioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC, progress, 0);
                        mVolumeSeekBar.setProgress(progress);
                    }
                });
    }

    public void initTileView(View v) {
        mTextRsolution = (TextView) v.findViewById(R.id.vedio_resolution);
        mTextSelections = (TextView) v.findViewById(R.id.vedio_selections);
        mTitleLayout = (RelativeLayout) v.findViewById(R.id.video_title_bar);
        mControlLayout = (LinearLayout) v
                .findViewById(R.id.play_control_layout);

        initVolumeBar(v);
        initBrightnessBar(v);
        initForwardSetLayout(v);
        mBrightnessSetLayout.setVisibility(View.GONE);
        mVolumeSetLayout.setVisibility(View.GONE);
        mFwdSetLayout.setVisibility(View.GONE);

        mResolutionSet = (LinearLayout) v
                .findViewById(R.id.resolution_set_layout);
        mTextASpeed = (TextView) mResolutionSet
                .findViewById(R.id.aspeed_resolution);
        mTextBSpeed = (TextView) mResolutionSet
                .findViewById(R.id.bspeed_resolution);
        mTextCSpeed = (TextView) mResolutionSet
                .findViewById(R.id.cspeed_resolution);
        mTextDSpeed = (TextView) mResolutionSet
                .findViewById(R.id.dspeed_resolution);
        if (mTextASpeed != null) {
            mTextASpeed.requestFocus();
            mTextASpeed.setOnClickListener(this);
        }
        if (mTextBSpeed != null) {
            mTextBSpeed.requestFocus();
            mTextBSpeed.setOnClickListener(this);
        }
        if (mTextCSpeed != null) {
            mTextCSpeed.requestFocus();
            mTextCSpeed.setOnClickListener(this);
        }
        if (mTextDSpeed != null) {
            mTextDSpeed.requestFocus();
            mTextDSpeed.setOnClickListener(this);
        }
        mResolutionSet.setVisibility(View.GONE);

        mContentSelectLayout = (FrameLayout) v
                .findViewById(R.id.content_set_layout);
        mContentList = (ListView) mContentSelectLayout
                .findViewById(R.id.content_select_list);
        mContentGrid = (GridView) mContentSelectLayout
                .findViewById(R.id.content_select_grid);
        // 测试数据
        initListData();
        mContentSelectLayout.setVisibility(View.GONE);

        if (mTextRsolution != null) {
            mTextRsolution.requestFocus();
            mTextRsolution.setOnClickListener(mResolutionListener);
        }
        if (mTextSelections != null) {
            mTextSelections.requestFocus();
            mTextSelections.setOnClickListener(mSelectionListener);
        }
    }

    private List<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();

    public void initListData() {
        int[] images = new int[] { R.drawable.hot_image_01,
                R.drawable.hot_image_02, R.drawable.hot_image_03,
                R.drawable.hot_image_04, R.drawable.hot_image_01,
                R.drawable.hot_image_02, R.drawable.hot_image_03,
                R.drawable.hot_image_04, R.drawable.hot_image_01,
                R.drawable.hot_image_02, R.drawable.hot_image_03,
                R.drawable.hot_image_04, R.drawable.hot_image_01,
                R.drawable.hot_image_02, R.drawable.hot_image_03,
                R.drawable.hot_image_04 };
        for (int i = 0; i < 12; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("name", "电视剧");
            map.put("description", "倚天屠龙记");
            mDatas.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), mDatas,
                R.layout.content_select_item_layout, new String[] { "image",
                        "name", "description" }, new int[] { R.id.image,
                        R.id.name, R.id.description });
        mContentList.setAdapter(adapter);

    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     * 
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.video_control_layout, null);
        mTitleView = (TextView) mRoot.findViewById(R.id.vedio_title);

        initControllerView(mRoot);
        initTileView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mFullscreenButton = (ImageButton) v.findViewById(R.id.fullscreen);
        if (mFullscreenButton != null) {
            mFullscreenButton.requestFocus();
            mFullscreenButton.setOnClickListener(mFullscreenListener);
        }

        mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        if (mFfwdButton != null) {
            mFfwdButton.setOnClickListener(mFfwdListener);
            if (!mFromXml) {
                // mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE :
                // View.GONE);
            }
        }

        mRewButton = (ImageButton) v.findViewById(R.id.rew);
        if (mRewButton != null) {
            mRewButton.setOnClickListener(mRewListener);
            if (!mFromXml) {
                // mRewButton.setVisibility(mUseFastForward ? View.VISIBLE :
                // View.GONE);
            }
        }

        // By default these are hidden. They will be enabled when
        // setPrevNextListeners() is called
        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);
        }
        mNextButton.setVisibility(View.INVISIBLE);
        mPrevButton = (ImageButton) v.findViewById(R.id.prev);
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton.setVisibility(View.GONE);
        }
        mPrevButton.setVisibility(View.INVISIBLE);

        mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        btnBackImageView = (ImageView) mRoot.findViewById(R.id.btn_back);
        if (btnBackImageView != null) {
            btnBackImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doBackButtonClick();
                }
            });
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mVideoVoice = (ImageButton) v.findViewById(R.id.video_voice);
        if (mVideoVoice != null) {
            mVideoVoice.setOnClickListener(mVideoVoiceListener);
        }
        mVideoNext = (ImageButton) v.findViewById(R.id.video_next);
        if (mVideoNext != null) {
            mVideoNext.setOnClickListener(mVideoNextListenter);
        }
        installPrevNextListeners();
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        if (mPlayer == null) {
            return;
        }

        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
            if (mRewButton != null && !mPlayer.canSeekBackward()) {
                mRewButton.setEnabled(false);
            }
            if (mFfwdButton != null && !mPlayer.canSeekForward()) {
                mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't
            // disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     * 
     * @param timeout
     *            The timeout in milliseconds. Use 0 to show the controller
     *            until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            // 将当前的view加入surfaceview/mRootView
            // mAnchor.addView(this, tlp);
            this.addView(mRoot, tlp);
            mShowing = true;
        }
        updatePausePlay();
        updateFullScreen();

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null || mRoot == null) {
            return;
        }
        try {
            // mRoot
            // mAnchor.removeView(this);
            hideVolumeLayout();
            hideBrightnessLayout();
            hideForwardLayout();
            hideVoiceWindow();
            this.removeView(mRoot);
            mHandler.removeMessages(SHOW_PROGRESS);
        } catch (IllegalArgumentException ex) {
            Log.w("MediaController", "already removed");
        }
        mShowing = false;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void setForwardProgess() {
        if (mPlayer == null || mDragging) {
            return;
        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mFwdSetSeekBar != null) {
            if (duration > 0) {
                mFwdSetSeekBar.setProgress(mProgress.getProgress());
            }
            mFwdSetSeekBar.setSecondaryProgress(mProgress
                    .getSecondaryProgress());
        }
        if (mTextFwdTime != null)
            mTextFwdTime.setText(stringForTime(duration));
        if (mTextFwdCurTime != null)
            mTextFwdCurTime.setText(stringForTime(position));
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;

        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.i("jiangtao4", "in videocontrolview onTouchEvent!!");
        // if (mGestureDetector.onTouchEvent(event)){
        // return true;
        // }
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mInitPosX = mLastPosX = event.getX();
            mInitPosY = mLastPosY = event.getY();
            // 每次按下时都重新初始化值
            mVolume = -1;
            mBrightness = -1f;
            mVideoPosition = -1;
            // setForwardProgess();
            break;
        case MotionEvent.ACTION_MOVE:
            mTouchStartX = event.getX();
            mTouchStartY = event.getY();
            boolean bConsume = true;
            boolean yOrentation = Math.abs(mLastPosY - mTouchStartY) > 60 ? true
                    : false;
            boolean xOrentation = Math.abs(mLastPosX - mTouchStartX) > (60 * (mWidth / (mHeight * 1.0f))) ? true
                    : false;
            boolean isYup = (mLastPosY - mTouchStartY) > 0 ? true : false;

            if (mInitPosX < mWidth / 3 && yOrentation && !xOrentation) {
                if (mPlayer != null && mPlayer.isFullScreen()) {
                    ajustVolume(isYup);
                }
            } else if (mInitPosX > (mWidth * 2 / 3) && !xOrentation
                    && yOrentation) {
                if (mPlayer != null && mPlayer.isFullScreen()) {
                    ajustBrightness(isYup);
                }
            } else if (xOrentation && !yOrentation
                    && (mLastPosX - mTouchStartX) < 0) {
                if (mPlayer != null && mPlayer.isFullScreen()) {
                    ajustVedioForward(true);
                }
            } else if (xOrentation && !yOrentation
                    && (mLastPosX - mTouchStartX) > 0) {
                if (mPlayer != null && mPlayer.isFullScreen()) {
                    ajustVedioForward(false);
                }
            } else {
                // 都大于的情况不进入
                bConsume = false;
            }
            if (bConsume) {
                mLastPosY = event.getY();
                mLastPosX = event.getX();
            }
            break;
        case MotionEvent.ACTION_UP:
            if (isShowing()) {
                hide();
            } else {
                show();
            }
            break;
        }

        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlayer == null) {
            return true;
        }

        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mResolutionListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "reeeeeeeeee", Toast.LENGTH_SHORT)
                    .show();
            if (isViewVisiable(mResolutionSet)) {
                setResolutionHide(mTextRsolution.getText().toString());
            } else {
                if (isViewVisiable(mContentSelectLayout)) {
                    setContentHide();
                }
                setResolutionShow();
            }
        }
    };

    private View.OnClickListener mSelectionListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isViewVisiable(mContentSelectLayout)) {
                setContentHide();
            } else {
                if (isViewVisiable(mResolutionSet)) {
                    setResolutionHide(mTextRsolution.getText().toString());
                }
                setContentShow();
            }
        }
    };

    public static boolean isViewVisiable(View v) {
        boolean isVisiable = false;
        if (v == null) {
            return isVisiable;
        }
        if (v.getVisibility() == View.VISIBLE) {
            isVisiable = true;
        }
        return isVisiable;
    }

    private View.OnClickListener mFullscreenListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("jiangtao4", "fullscreen is clicked");
            // 是否全屏
            doToggleFullscreen();
            show(sDefaultTimeout);
        }
    };

    public void updatePausePlay() {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.ic_media_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.ic_media_play);
        }
    }

    public void updateFullScreen() {
        if (mRoot == null || mFullscreenButton == null || mPlayer == null) {
            return;
        }

        if (mPlayer.isFullScreen()) {
            // mFullscreenButton.setImageResource(R.drawable.ic_media_fullscreen_shrink);
            mFullscreenButton.setImageResource(R.drawable.fullscreen);
            mTextRsolution.setVisibility(View.VISIBLE);
            mTextSelections.setVisibility(View.VISIBLE);
            mVideoVoice.setVisibility(View.VISIBLE);
            mVideoNext.setVisibility(View.VISIBLE);
        } else {
            mFullscreenButton.setImageResource(R.drawable.fullscreen);
            mTextRsolution.setVisibility(View.GONE);
            mTextSelections.setVisibility(View.GONE);
            // 如果分辨率设置显示，则隐藏
            if (mResolutionSet != null && isViewVisiable(mResolutionSet)) {
                mResolutionSet.setVisibility(View.GONE);
            }
            if (mContentSelectLayout != null
                    && isViewVisiable(mContentSelectLayout)) {
                mContentSelectLayout.setVisibility(View.GONE);
            }
            if (mVideoVoice != null && isViewVisiable(mVideoVoice)) {
                mVideoVoice.setVisibility(View.GONE);
            }
            if (mVideoNext != null && isViewVisiable(mVideoNext)) {
                mVideoNext.setVisibility(View.GONE);
            }
            hideVoiceWindow();
        }
    }

    private void doPauseResume() {
        if (mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void doToggleFullscreen() {
        if (mPlayer == null) {
            return;
        }

        mPlayer.toggleFullScreen();
    }

    private void doBackButtonClick() {
        if (mPlayer == null) {
            return;
        }

        mPlayer.doBackButtonClick();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by
    // onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the
    // dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch
    // notifications,
    // we will simply apply the updated position without suspending regular
    // updates.
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress,
                boolean fromuser) {
            if (mPlayer == null) {
                return;
            }

            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    public void setTitle(String mTitle) {
        if (mTitleView != null) {
            mTitleView.setText(mTitle);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && mPrevListener != null);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoControllerView.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoControllerView.class.getName());
    }

    private View.OnClickListener mRewListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mPlayer == null) {
                return;
            }

            int pos = mPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mFfwdListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mPlayer == null) {
                return;
            }

            int pos = mPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mVideoVoiceListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i("aa", "video voice button click!!");
            mVoiceWindow = new PopupSeekBarWindow(getContext());
            mVoiceWindow.show(mVideoVoice);
        }
    };

    private View.OnClickListener mVideoNextListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next,
            View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;

        if (mRoot != null) {
            installPrevNextListeners();

            if (mNextButton != null && !mFromXml) {
                // mNextButton.setVisibility(View.VISIBLE);
            }
            if (mPrevButton != null && !mFromXml) {
                // mPrevButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        boolean isFullScreen();

        void toggleFullScreen();

        void doBackButtonClick();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            mView = new WeakReference<VideoControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoControllerView view = mView.get();
            if (view == null || view.mPlayer == null) {
                return;
            }

            int pos;
            switch (msg.what) {
            case FADE_OUT:
                // 分辨率选择和选集选择出现时，不隐藏该bar
                if ((!isViewVisiable(view.mResolutionSet) && !isViewVisiable(view.mContentSelectLayout))) {
                    view.hide();
                }
                break;
            case SHOW_PROGRESS:
                pos = view.setProgress();
                if (!view.mDragging && view.mShowing
                        && view.mPlayer.isPlaying()) {
                    msg = obtainMessage(SHOW_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.aspeed_resolution:
            setResolutionHide(mTextASpeed.getText().toString());
            break;
        case R.id.bspeed_resolution:
            setResolutionHide(mTextBSpeed.getText().toString());
            break;
        case R.id.cspeed_resolution:
            setResolutionHide(mTextCSpeed.getText().toString());
            break;
        case R.id.dspeed_resolution:
            setResolutionHide(mTextDSpeed.getText().toString());
            break;
        default:
            break;
        }
    }

    public void setResolutionHide(String text) {
        if (TextUtils.isEmpty(text) || mTextRsolution == null
                || mResolutionSet == null) {
            return;
        }
        mTextRsolution.setText(text);
        mResolutionSet.clearAnimation();
        AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(
                getContext(), R.anim.resolution_set_anim_hide);
        mResolutionSet.startAnimation(animation);
        // view设置为gone时，动画无效果
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mResolutionSet.setVisibility(View.GONE);
                show(sDefaultTimeout);
            }
        });
    }

    public void setContentHide() {
        if (mContentSelectLayout == null || mContentList == null
                || mContentGrid == null) {
            return;
        }
        mContentSelectLayout.clearAnimation();
        AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(
                getContext(), R.anim.resolution_set_anim_hide);
        mContentSelectLayout.startAnimation(animation);
        // view设置为gone时，动画无效果
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContentSelectLayout.setVisibility(View.GONE);
                show(sDefaultTimeout);
            }
        });
    }

    public void setResolutionShow() {
        if (mResolutionSet == null) {
            return;
        }
        mResolutionSet.clearAnimation();
        mResolutionSet.startAnimation(AnimationUtils.loadAnimation(
                getContext(), R.anim.resolution_set_anim_show));
        mResolutionSet.setVisibility(View.VISIBLE);
    }

    public void setContentShow() {
        if (mContentSelectLayout == null) {
            return;
        }
        mContentSelectLayout.clearAnimation();
        mContentSelectLayout.startAnimation(AnimationUtils.loadAnimation(
                getContext(), R.anim.resolution_set_anim_show));
        mContentSelectLayout.setVisibility(View.VISIBLE);
    }

    public class VolumeBrightnessDetector extends SimpleOnGestureListener {
        private DisplayMetrics mDisplayMetrics;
        int mWidth = 0;
        int mHeight = 0;

        public VolumeBrightnessDetector() {
            mDisplayMetrics = getContext().getResources().getDisplayMetrics();
            mWidth = mDisplayMetrics.heightPixels;
            mHeight = mDisplayMetrics.widthPixels;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // 每次按下时都重新初始化值
            mVolume = -1;
            mBrightness = -1f;
            setForwardProgess();
            return super.onDown(e);
        }

        // 处理单击
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isShowing()) {
                hide();
            } else {
                show();
            }
            return super.onSingleTapUp(e);
        }

        // 滑动控制音量和亮度
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            Log.i("jiangtao4", "in onscroll!!");
            int initPosX = (int) e1.getX();
            int lastPosX = (int) e2.getX();
            int initPosY = (int) e1.getY();
            int lastPosY = (int) e2.getY();

            boolean yOrentation = Math.abs(lastPosY - initPosY) > mMinDistance ? true
                    : false;
            boolean xOrentation = Math.abs(lastPosX - initPosX) > mMinDistance ? true
                    : false;

            if (initPosX < mWidth / 3 && yOrentation && !xOrentation) {
                // ajustVolume((initPosY - lastPosY)/(mHeight*1.0f));
                // ajustVolume(distanceY/(mHeight*1.0f));
            } else if (initPosX > (mWidth * 2 / 3) && !xOrentation
                    && yOrentation) {
                // ajustBrightness((initPosY - lastPosY)/(mHeight*1.0f));
            } else if (xOrentation && !yOrentation && distanceX < 0) {
                ajustVedioForward(true);
            } else if (xOrentation && !yOrentation && distanceX > 0) {
                ajustVedioForward(false);
            }

            // distanceY 每次的滑动距离
            // 右侧和左侧屏幕的1/3处可以滑动
            Log.i("jiangtao4", "mWidth is : " + mWidth + "distancY is : "
                    + distanceY);
            // if (initPosX < mWidth/3) {
            // if ((Math.abs(distanceY) > Math.abs(distanceX)) &&
            // mPlayer.isFullScreen() ) {
            // ajustVolume((initPosY - lastPosY)/(mHeight*1.0f));
            // }else if ((Math.abs(distanceX) > Math.abs(distanceY)) &&
            // mPlayer.isFullScreen() && distanceX < 0 ){
            // ajustVedioForward(true);
            // }
            // }else if (initPosX > (mWidth*2/3)) {
            // if ((Math.abs(distanceY) > Math.abs(distanceX)) &&
            // mPlayer.isFullScreen() ) {
            // ajustBrightness((initPosY - lastPosY)/(mHeight*1.0f));
            // }else if ((Math.abs(distanceX) > Math.abs(distanceY)) &&
            // mPlayer.isFullScreen() && distanceX > 0){
            // ajustVedioForward(false);
            // }
            // }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void showVolumeLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.GONE);
        }
        if (mResolutionSet != null) {
            mResolutionSet.setVisibility(View.GONE);
        }
        if (mBrightnessSetLayout != null) {
            mBrightnessSetLayout.setVisibility(View.GONE);
        }
        if (mContentSelectLayout != null) {
            mContentSelectLayout.setVisibility(View.GONE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.GONE);
        }
        if (mFwdSetLayout != null) {
            mFwdSetLayout.setVisibility(View.GONE);
        }
        if (mVolumeSetLayout != null) {
            mVolumeSetLayout.setVisibility(View.VISIBLE);
        }

        if (!isShowing()) {
            show();
        }
    }

    public void showForwardLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.GONE);
        }
        if (mResolutionSet != null) {
            mResolutionSet.setVisibility(View.GONE);
        }
        if (mVolumeSetLayout != null) {
            mVolumeSetLayout.setVisibility(View.GONE);
        }
        if (mContentSelectLayout != null) {
            mContentSelectLayout.setVisibility(View.GONE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.GONE);
        }
        if (mBrightnessSetLayout != null) {
            mBrightnessSetLayout.setVisibility(View.GONE);
        }
        if (mFwdSetLayout != null) {
            mFwdSetLayout.setVisibility(View.VISIBLE);
        }
        if (!isShowing()) {
            show();
        }
    }

    public void hideForwardLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.VISIBLE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.VISIBLE);
        }

        if (mFwdSetLayout != null) {
            mFwdSetLayout.setVisibility(View.GONE);
        }
    }

    public void hideVoiceWindow() {
        if (mVoiceWindow != null && mVoiceWindow.isShowing()) {
            mVoiceWindow.dismiss();
        }
    }

    public void showBrightnessLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.GONE);
        }
        if (mResolutionSet != null) {
            mResolutionSet.setVisibility(View.GONE);
        }
        if (mVolumeSetLayout != null) {
            mVolumeSetLayout.setVisibility(View.GONE);
        }
        if (mContentSelectLayout != null) {
            mContentSelectLayout.setVisibility(View.GONE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.GONE);
        }
        if (mFwdSetLayout != null) {
            mFwdSetLayout.setVisibility(View.GONE);
        }
        if (mBrightnessSetLayout != null) {
            mBrightnessSetLayout.setVisibility(View.VISIBLE);
        }
        if (!isShowing()) {
            show();
        }
    }

    public void hideBrightnessLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.VISIBLE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.VISIBLE);
        }

        if (mBrightnessSetLayout != null) {
            mBrightnessSetLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 与showVoandBtLayout相反
     */
    public void hideVolumeLayout() {
        if (mTitleLayout != null) {
            mTitleLayout.setVisibility(View.VISIBLE);
        }
        if (mControlLayout != null) {
            mControlLayout.setVisibility(View.VISIBLE);
        }

        if (mVolumeSetLayout != null) {
            mVolumeSetLayout.setVisibility(View.GONE);
        }
    }

    public void ajustVedioForward(boolean isForward) {
        showForwardLayout();
        // int position = 0;
        int duration = 0;
        if (mVideoPosition < 0) {
            // position = mPlayer.getCurrentPosition();
            mVideoPosition = mPlayer.getCurrentPosition();
        }
        duration = mPlayer.getDuration();
        Log.i("jiant", "position1 is: " + mVideoPosition + "duration is : "
                + duration);
        if (isForward) {
            mVideoPosition += 1500;// 每次前进
            if (mVideoPosition > duration) {
                mVideoPosition = duration;
            }
            mTextFwd.setText("快进");
        } else {
            mTextFwd.setText("后退");
            mVideoPosition -= 1500;
            if (mVideoPosition < 0) {
                mVideoPosition = 0;
            }
        }
        Log.i("jiant", "position2 is: " + mVideoPosition);
        if (mFwdSetSeekBar != null && mPlayer != null) {
            if (duration > 0) {
                long pos = 1000L * mVideoPosition / duration;
                mPlayer.seekTo(mVideoPosition);
                Log.i("jiant", "position3 is: " + mVideoPosition);
                Log.i("jiant", "pos is: " + pos);
                mFwdSetSeekBar.setProgress((int) pos);
                mProgress.setProgress((int) pos);
                mTextFwdCurTime.setText(stringForTime(mVideoPosition));
                mTextFwdTime.setText(stringForTime(duration));
                if (mCurrentTime != null)
                    mCurrentTime.setText(stringForTime(mVideoPosition));
                Log.i("jiant", "time is : " + stringForTime(mVideoPosition));
            }
        }
    }

    public void ajustBrightness(boolean isUp) {
        showBrightnessLayout();
        if (mBrightness < 0) {
            // mBrightness =
            // ((Activity)mContext).getWindow().getAttributes().screenBrightness;
            mBrightness = mBrightnessSeekBar.getProgress() / (100 * 1.0f);
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow()
                .getAttributes();
        if (isUp) {
            mBrightness += 0.05f;
        } else {
            mBrightness -= 0.05f;
        }
        lpa.screenBrightness = mBrightness;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;

        ((Activity) mContext).getWindow().setAttributes(lpa);

        mBrightnessSeekBar.setProgress((int) (lpa.screenBrightness * 100));
    }

    int volmue = 0;

    public void ajustVolume(boolean isUp) {
        showVolumeLayout();
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volmue = mVolume;
            if (mVolume < 0) {
                mVolume = 0;
            }
        }

        if (isUp) {
            volmue += 1;
        } else {
            volmue -= 1;
        }

        if (volmue > mMaxVolume) {
            volmue = mMaxVolume;
        }
        if (volmue < 0) {
            volmue = 0;
        }

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volmue, 0);
        mVolumeSeekBar.setProgress(volmue);
    }
}