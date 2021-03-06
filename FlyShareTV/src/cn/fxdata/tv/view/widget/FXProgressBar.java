package cn.fxdata.tv.view.widget;

import cn.fxdata.tv.utils.DPIUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import cn.fxdata.tv.R;

/**
 * 定义网络请求时的旋转圈,以后推广到整个app使用
 * 
 */
public class FXProgressBar extends ProgressBar {

    public FXProgressBar(Context context) {
        super(context);
        init();
    }

    public FXProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FXProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final LayoutParams layoutParams = new LayoutParams(DPIUtil.dip2px(34),
                DPIUtil.dip2px(34));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(layoutParams);
        // this.setBackgroundResource(R.drawable.load_logo);
        this.setIndeterminateDrawable(this.getResources().getDrawable(
                R.drawable.progress_small));
        this.setIndeterminate(true);

    }
}