package com.zgntech.core.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.zgntech.core.view.pullrefresh.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 * 
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

	/** ListView */
	private ListView mListView;
	/** 用于滑到底部自动加载的Footer */
	private FooterLoadingLayout mLoadMoreFooterLayout;
	/** 用于滑到顶部自动加载的Header */
	private LoadingLayout mLoadMoreHeaderLayout;
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;
	/** 滚动状态 回调接口 */
	private IScrollStateCallback mIScrollStateCallback;
	/** View 高度，为0表示没有为listview添加headview */
	private int Height = 0;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshListView(Context context) {
		this(context, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setPullLoadEnabled(false);
	}

	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView listView = new ListView(context);
		mListView = listView;
		listView.setOnScrollListener(this);

		return listView;
	}

	/**
	 * 给lListView添加头部View，非刷新View
	 * 
	 * @param view
	 */
	public void addHeaderView(final View view) {
		if (view == null || mListView == null)
			return;
		mListView.addHeaderView(view);
		ViewTreeObserver vto2 = view.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				Height = view.getHeight();
			}
		});
	}		

	/**
	 * 设置是否有更多数据的标志
	 * 
	 * @param hasMoreData
	 *            true表示还有更多的数据，false表示没有更多数据了
	 */
	public void setHasMoreDataHeader(boolean hasMoreData) {
		if (!hasMoreData) {
			if (null != mLoadMoreHeaderLayout) {
				mLoadMoreHeaderLayout.setState(State.NO_MORE_DATA);
			}

			LoadingLayout headerLoadingLayout = getHeaderLoadingLayout();
			if (null != headerLoadingLayout) {
				headerLoadingLayout.setState(State.NO_MORE_DATA);
			}
		} else {
			if (null != mLoadMoreHeaderLayout) {
				mLoadMoreHeaderLayout.setState(State.NONE);
			}

			LoadingLayout headerLoadingLayout = getHeaderLoadingLayout();
			if (null != headerLoadingLayout) {
				mLoadMoreHeaderLayout.setState(State.NONE);
			}
		}
	}

	/**
	 * 设置是否有更多数据的标志
	 * 
	 * @param hasMoreData
	 *            true表示还有更多的数据，false表示没有更多数据了
	 */
//	public void setHasMoreData(boolean hasMoreData) {
//		if (!hasMoreData) {
//			if (null != mLoadMoreFooterLayout) {
//				mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
//				//removeFooterView(mLoadMoreFooterLayout);
//			}
//
//			LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
//			if (null != footerLoadingLayout) {
//				removeFooterView(footerLoadingLayout);
//				footerLoadingLayout.setState(State.NO_MORE_DATA);
//			}
//		} else {
//			addFooterView();
//			if (null != mLoadMoreFooterLayout) {
//				mLoadMoreFooterLayout.setState(State.NONE);
//			}
//
//			LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
//			if (null != footerLoadingLayout) {
//				footerLoadingLayout.setState(State.NONE);
//			}
//		}
//	}	
	//备份
	public void setHasMoreData(boolean hasMoreData) {
		if (!hasMoreData) {
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.setState(State.HAS_NO_DATA);
				removeFooterView(mLoadMoreFooterLayout);
			}

			LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
			if (null != footerLoadingLayout) {
				removeFooterView(footerLoadingLayout);
				footerLoadingLayout.setState(State.HAS_NO_DATA);
			}
		} else {
			addFooterView();
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.setState(State.NONE);
			}

			LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
			if (null != footerLoadingLayout) {
				footerLoadingLayout.setState(State.NONE);
			}
		}
	}				

	/**
	 * 移除底部提示View
	 */
	private void removeFooterView(View view) {
		if (view.getParent() != null) {
			mListView.removeFooterView(view);
		}
	}

	private void addFooterView() {
		if (mListView.getFooterViewsCount() == 0 && null == mLoadMoreFooterLayout.getParent()) {
			mListView.addFooterView(mLoadMoreFooterLayout, null, false);
		}
	}

	/**
	 * 设置 没有任何数据
	 */
	public void setHasNoData() {
		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.HAS_NO_DATA);
		}
		LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
		if (null != footerLoadingLayout) {
			footerLoadingLayout.setState(State.HAS_NO_DATA);
		}
	}

	public void setLoadFooterImageView(ImageView imageView) {
		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setmImageView(imageView);
		}
	}
	
	/**
	 * 判断是否已经添加了提示图片
	 * 
	 * @return
	 */
	public boolean hasLoadFooterImageView() {
		if (null != mLoadMoreFooterLayout) {
			return mLoadMoreFooterLayout.getmImageView() != null;
		}
		return false;
	}

	/**
	 * 设置滑动的监听器
	 * 
	 * @param l
	 *            监听器
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	public void setScrollStateCallback(IScrollStateCallback callback) {
		this.mIScrollStateCallback = callback;
	}

	@Override
	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	@Override
	protected void startLoading() {
		super.startLoading();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.REFRESHING);
		}
	}

	@Override
	public void onPullUpRefreshComplete() {
		super.onPullUpRefreshComplete();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.RESET);
		}
	}

	@Override
	public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
		super.setScrollLoadEnabled(scrollLoadEnabled);

		if (scrollLoadEnabled) {
			// 设置Footer
			if (null == mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
			}

			if (null == mLoadMoreFooterLayout.getParent()) {
				mListView.addFooterView(mLoadMoreFooterLayout, null, false);
			}
			mLoadMoreFooterLayout.show(true);
		} else {
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.show(false);
			}
		}
	}

	@Override
	public LoadingLayout getHeaderLoadingLayout() {
		if (isScrollLoadEnabled()) {
			return mLoadMoreFooterLayout;
		}

		return super.getFooterLoadingLayout();
	}

	@Override
	public LoadingLayout getFooterLoadingLayout() {
		if (isScrollLoadEnabled()) {
			return mLoadMoreFooterLayout;
		}

		return super.getFooterLoadingLayout();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isScrollLoadEnabled() && hasMoreData()) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (isReadyForPullUp()) {
					startLoading();
				}
			}
		}

		if (null != mScrollListener) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}

		if (mIScrollStateCallback != null) {
			switch (scrollState) {
//			case OnScrollListener.SCROLL_STATE_IDLE:// 滑动结束
//				mIScrollStateCallback.onScrollFinished();
//				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 开始滑动
				mIScrollStateCallback.onScrollStart();
				break;
//			case OnScrollListener.SCROLL_STATE_FLING:
//				mIScrollStateCallback.onScrollFling();
//				break;
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (null != mScrollListener) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
		return new RotateLoadingLayout(context);
	}

	/**
	 * 表示是否还有更多数据
	 * 
	 * @return true表示还有更多数据
	 */
	private boolean hasMoreData() {
		if ((null != mLoadMoreFooterLayout)
				&& ((mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA) || (mLoadMoreFooterLayout.getState() == State.HAS_NO_DATA))) {
			return false;
		}

		return true;
	}

	/**
	 * 判断第一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		Adapter adapter = mListView.getAdapter();

		if (Height == 0 && (null == adapter || adapter.isEmpty())) {
			return true;
		}

		int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;

		if (mostTop >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 判断最后一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = mListView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
			final int childCount = mListView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mListView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mListView.getBottom();
			}
		}

		return false;
	}

	/**
	 * TODO<滑动状态回调接口>
	 * 
	 * @author Yanghe
	 * @data: 2014-4-2 上午10:07:58
	 * @version: V1.0
	 */
	public interface IScrollStateCallback {
		/**
		 * 开始滑动
		 */
		public void onScrollStart();
		/**
		 * 停止滑动
		 */
		public void onScrollFinished();
		/**
		 * 手指离开屏幕
		 */
		public void onScrollFling();
	}
}
