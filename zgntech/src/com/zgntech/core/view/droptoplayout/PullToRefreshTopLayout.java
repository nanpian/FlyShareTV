package com.zgntech.core.view.droptoplayout;


import com.zgntech.core.view.pullrefresh.PullToRefreshBase;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chenupt@gmail.com on 3/3/15.
 * Description : Used for Android-PullToRefresh
 */
public class PullToRefreshTopLayout extends PullToRefreshBase<DragTopLayout> {

    public PullToRefreshTopLayout(Context context) {
        super(context);
    }

    public PullToRefreshTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getRefreshableView().onFinishInflate();
    }

    @Override
    protected DragTopLayout createRefreshableView(Context context, AttributeSet attrs) {
        return new DragTopLayout(context, attrs);
    }

    @Override
    protected boolean isReadyForPullDown() {
        DragTopLayout refreshableView = getRefreshableView();
        return refreshableView.getState() == DragTopLayout.PanelState.EXPANDED;
    }
    @Override
    protected boolean isReadyForPullUp() {
        // TODO Auto-generated method stub
        return false;
    }
}

