package cn.fxdata.tv.view;

import cn.fxdata.tv.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/16 0016.
 */
public class StateImageView extends ImageView implements View.OnClickListener {

    private int mStateOnImage;
    private int mStateOffImage;
    private boolean mIsStateOn = false;
    private ImageStateChangeInterface mImageInterface;
    private final static String TAG = "StateImageView";

    public StateImageView(Context context) {
        super(context);
    }

    public StateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.StateImageView);
        mStateOnImage = a.getResourceId(R.styleable.StateImageView_state_on,
                R.drawable.btn_followme_pressed);
        mStateOffImage = a.getResourceId(R.styleable.StateImageView_state_off,
                R.drawable.btn_followme_normal);
        Log.d(TAG, "The state is --->>> " + mIsStateOn);
        if (mIsStateOn) {
            setImageResource(mStateOnImage);
        } else {
            setImageResource(mStateOffImage);
        }

        setOnClickListener(this);

    }

    public boolean getImgSelectState() {
        return mIsStateOn;
    }

    public void setImgSelectState(boolean isStateOn) {
        mIsStateOn = isStateOn;
        if (isStateOn) {
            setImageResource(mStateOnImage);
        } else {
            setImageResource(mStateOffImage);
        }
    }

    public void setImgStateChangeListener(
            ImageStateChangeInterface selectInterface) {
        this.mImageInterface = selectInterface;
    }

    public interface ImageStateChangeInterface {
        public void onImageStateChange(boolean isSelected, View view);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        // 当前为选择状态
        Log.d(TAG, "The selected status is " + getImgSelectState());
        if (getImgSelectState()) {
            mIsStateOn = false;
            setImageResource(mStateOffImage);
            if (mImageInterface != null) {
                mImageInterface.onImageStateChange(mIsStateOn, v);
            }
        } else {
            mIsStateOn = true;
            setImageResource(mStateOnImage);
            if (mImageInterface != null) {
                mImageInterface.onImageStateChange(mIsStateOn, v);
            }
        }
    }
}
