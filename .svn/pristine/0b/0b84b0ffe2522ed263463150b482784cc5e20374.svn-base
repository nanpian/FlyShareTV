package com.zgntech.core.view.pullrefresh;

import com.zgntech.base.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class FooterLoadingLayout extends LoadingLayout {
	
	/**根布局*/
	private RelativeLayout mRelativeLayout;
    /**进度条*/
    private ProgressBar mProgressBar;
    /** 显示的文本 */
    private TextView mHintView;
    /** 显示无数据*/
    private ImageView mImageView;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     * 
     * @param context context
     */
    private void init(Context context) {
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_load_footer_progressbar);
        mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.pull_to_load_footer_layout);
        setState(State.RESET);
    }
    
    /**
	 * @return the mImageView
	 */
	public ImageView getmImageView() {
		return mImageView;
	}

	/**
	 * @param mImageView the mImageView to set
	 */
	public void setmImageView(ImageView mImageView) {
		if (this.mImageView != null) {
			mRelativeLayout.removeView(this.mImageView);
			this.mImageView = null;
		}
			
		this.mImageView = mImageView;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.pull_to_refresh_load_footer_margintop), 0, 0);
		
		mRelativeLayout.addView(this.mImageView, params);
	}
	
	public void setRootLayoutBg(int colorId) {
		mRelativeLayout.setBackgroundColor(colorId);
	}

	@Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (null != view) {
            return view.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 40);
    }
    
    @Override
    protected void onStateChanged(State curState, State oldState) {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.INVISIBLE);
        if (mImageView != null)
        	mImageView.setVisibility(View.GONE);
        
        super.onStateChanged(curState, oldState);
    }
    
    @Override
    protected void onReset() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onPullToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
    
    @Override
    protected void onNoMoreData() {
        mHintView.setVisibility(View.INVISIBLE);
        mHintView.setText(R.string.pushmsg_center_no_more_msg);
    }
    
    @Override
    protected void onHasNoData() {
    	if (mImageView != null) {
    		mImageView.setVisibility(View.VISIBLE);
    	}
    }
}
