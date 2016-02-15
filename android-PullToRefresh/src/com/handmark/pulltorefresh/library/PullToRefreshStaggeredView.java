/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
 
 //Read this 
 //https://app.yinxiang.com/shard/s8/sh/73f9439f-894c-4dfd-9b37-7e922d02f825/475ffd37cea2a48713e862a049ce043a
 
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.internal.EmptyViewMethodAccessor;

public class PullToRefreshStaggeredView extends PullToRefreshAdapterViewBase<StaggeredGridView> {

  public PullToRefreshStaggeredView(Context context) {
      super(context);
  }

  public PullToRefreshStaggeredView(Context context, AttributeSet attrs) {
      super(context, attrs);
  }

  public PullToRefreshStaggeredView(Context context, Mode mode) {
      super(context, mode);
  }

  public PullToRefreshStaggeredView(Context context, Mode mode, AnimationStyle style) {
      super(context, mode, style);
  }

  @Override
  public final Orientation getPullToRefreshScrollDirection() {
      return Orientation.VERTICAL;
  }

  @Override
  public void setAdapter(ListAdapter adapter) {
      ((StaggeredGridView) mRefreshableView).setAdapter(adapter);
  }

  @Override
  protected final StaggeredGridView createRefreshableView(Context context, AttributeSet attrs) {
      final StaggeredGridView gv;
      if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
          gv = new InternalStaggeredGridViewSDK9(context, attrs);
      } else {
          gv = new InternalStaggeredGridView(context, attrs);
      }

      // Use Generated ID (from res/values/ids.xml)
      gv.setId(R.id.gridview);
      return gv;
  }

  class InternalStaggeredGridView extends StaggeredGridView implements EmptyViewMethodAccessor {

      public InternalStaggeredGridView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }

      @Override
      public void setEmptyView(View emptyView) {
          PullToRefreshStaggeredView.this.setEmptyView(emptyView);
      }

      @Override
      public void setEmptyViewInternal(View emptyView) {
          super.setEmptyView(emptyView);
      }
  }

  @TargetApi(9)
  final class InternalStaggeredGridViewSDK9 extends InternalStaggeredGridView {

      public InternalStaggeredGridViewSDK9(Context context, AttributeSet attrs) {
          super(context, attrs);
      }

      @Override
      protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
              int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

          final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                  scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

          // Does all of the hard work...
          OverscrollHelper.overScrollBy(PullToRefreshStaggeredView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

          return returnValue;
      }
  }
}