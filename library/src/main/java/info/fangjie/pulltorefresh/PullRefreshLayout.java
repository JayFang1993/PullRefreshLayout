package info.fangjie.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

/**
 * Created by FangJie on 2017/3/2.
 */

public class PullRefreshLayout extends RelativeLayout {

  private boolean DEBUG = true;
  private static final String TAG = "PullRefreshLayout";
  private Context context;

  public PullRefreshLayout(Context context) {
    super(context);
    init(context);
  }

  public PullRefreshLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
    TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshLayout, 0, 0);
    if (arr != null) {
      enableLoad = arr.getBoolean(R.styleable.PullRefreshLayout_enableload, true);
      enableRefresh = arr.getBoolean(R.styleable.PullRefreshLayout_enablerefresh, true);
      topOffsetTarget = arr.getInt(R.styleable.PullRefreshLayout_topoffset, 0);
      bottomOffsetTarget = arr.getInt(R.styleable.PullRefreshLayout_bottomoffset, 0);
      arr.recycle();
    }
  }

  private void init(Context context) {
    this.context = context;
    refreshParams = new RefreshParams();
    pullToDownStatus = INIT;
    pullToUpStatus = INIT;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (getChildCount() == 1) {
      mContent = getChildAt(0);
    } else if (getChildCount() == 2) {
      mHeaderView = getChildAt(0);
      mContent = getChildAt(1);
    } else if (getChildCount() == 3) {
      mHeaderView = getChildAt(0);
      mContent = getChildAt(1);
      mFooterView = getChildAt(2);
    }
  }

  private View mHeaderView;
  private View mContent;
  private View mFooterView;
  private int mHeaderHeight, mFooterHeight, mContentHeight;

  private RefreshParams refreshParams;
  // 是否支持下拉刷新
  private boolean enableRefresh = true;
  // 是否支持上拉加载
  private boolean enableLoad = true;
  // 下拉的临界值
  private int topOffsetTarget;
  // 上拉的临界值
  private int bottomOffsetTarget;

  public void setEnableRefresh(boolean enableRefresh) {
    this.enableRefresh = enableRefresh;
  }

  public void setEnableLoad(boolean enableLoad) {
    this.enableLoad = enableLoad;
  }

  public void setTopOffsetTarget(int topOffsetTarget) {
    this.topOffsetTarget = topOffsetTarget;
  }

  public void setBottomOffsetTarget(int bottomOffsetTarget) {
    this.bottomOffsetTarget = bottomOffsetTarget;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (mHeaderView != null) {
      measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
      MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
      mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
      if (topOffsetTarget == 0) topOffsetTarget = mHeaderHeight;
    }

    if (mContent != null) {
      measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
      MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
      mContentHeight = mContent.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    if (mFooterView != null) {
      measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
      MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
      mFooterHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
      if (bottomOffsetTarget == 0) bottomOffsetTarget = mFooterHeight;
    }
  }

  private void measureContentView(View child, int parentWidthMeasureSpec,
      int parentHeightMeasureSpec) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
        getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);

    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
        getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);

    if (DEBUG) Log.d(TAG, "width:" + childWidthMeasureSpec + "-height:" + childHeightMeasureSpec);

    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    layoutChildren();
  }

  private void layoutChildren() {
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int offsetTop = refreshParams.getOffsetTop();
    int offsetBottom = refreshParams.getOffsetBottom();

    if (mHeaderView != null) {
      MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
      final int left = paddingLeft + lp.leftMargin;
      final int top = -(mHeaderHeight - paddingTop - lp.topMargin - offsetTop);
      final int right = left + mHeaderView.getMeasuredWidth();
      final int bottom = top + mHeaderView.getMeasuredHeight();
      mHeaderView.layout(left, top, right, bottom);
    }

    if (mContent != null) {
      MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
      final int left = paddingLeft + lp.leftMargin;
      final int top = paddingTop + lp.topMargin + offsetTop - offsetBottom;
      final int right = left + mContent.getMeasuredWidth();
      final int bottom = top + mContent.getMeasuredHeight();
      mContent.layout(left, top, right, bottom);
    }

    if (mFooterView != null) {
      MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
      final int left = paddingLeft + lp.leftMargin;
      final int top = paddingTop + lp.topMargin + mContentHeight + offsetTop - offsetBottom;
      final int right = left + mContent.getMeasuredWidth();
      final int bottom = top + mContent.getMeasuredHeight();
      mFooterView.layout(left, top, right, bottom);
    }
  }

  private int pullToDownStatus = INIT;
  private int pullToUpStatus = INIT;
  public static final int INIT = 0;
  public static final int PULL_NOT_TARGET = 1;
  public static final int PULL_OVER_TARGET = 2;
  public static final int REFRESH_ING = 3;

  public RefreshListener refreshListener;
  public LoadMoreListener loadMoreListener;

  public void setRefreshListener(RefreshListener refreshListener) {
    this.refreshListener = refreshListener;
  }

  public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
    this.loadMoreListener = loadMoreListener;
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        refreshParams.pressDown(ev);
        break;
      case MotionEvent.ACTION_MOVE:
        if (enableRefresh && !canChildScrollUp(mContent)) {
          refreshParams.movePos(ev, RefreshParams.TOP);
          if (refreshParams.isOffsetTopChanged()) {
            requestLayout();
            int offset = refreshParams.getOffsetTop();
            if (refreshListener != null) refreshListener.onPull(offset);
            /**
             * 开始下拉
             */
            if (pullToDownStatus == INIT
                && refreshParams.getScrollDirection() == RefreshParams.TOP_TO_BOTTOM) {
              pullToDownStatus = PULL_NOT_TARGET;
              if (refreshListener != null) refreshListener.onPullBegin();
            }
            /**
             * 下拉到临界值
             */
            else if (pullToDownStatus == PULL_NOT_TARGET
                && refreshParams.getScrollDirection() == RefreshParams.TOP_TO_BOTTOM
                && offset > topOffsetTarget) {
              pullToDownStatus = PULL_OVER_TARGET;
              if (refreshListener != null) refreshListener.onPullOverTarget();
            }
            /**
             * 下拉到临界值 上滑到小于临界值
             */
            else if (pullToDownStatus == PULL_OVER_TARGET
                && refreshParams.getScrollDirection() == RefreshParams.BOTTOM_TO_TOP
                && offset < topOffsetTarget) {
              pullToDownStatus = PULL_NOT_TARGET;
              if (refreshListener != null) refreshListener.onPullBelowTarget();
            }
            return false;
          }
        } else if (enableLoad && !canChildScrollDown(mContent)) {
          refreshParams.movePos(ev, RefreshParams.BOTTOM);
          if (refreshParams.isOffsetBottomChanged()) {
            requestLayout();
            int offset = refreshParams.getOffsetBottom();
            if (loadMoreListener != null) loadMoreListener.onPull(offset);
            /**
             * 开始上拉
             */
            if (pullToUpStatus == INIT
                && refreshParams.getScrollDirection() == RefreshParams.BOTTOM_TO_TOP) {
              pullToUpStatus = PULL_NOT_TARGET;
              if (loadMoreListener != null) loadMoreListener.onPullBegin();
            }
            /**
             * 上拉到临界值
             */
            else if (pullToUpStatus == PULL_NOT_TARGET
                && refreshParams.getScrollDirection() == RefreshParams.BOTTOM_TO_TOP
                && offset > bottomOffsetTarget) {
              pullToUpStatus = PULL_OVER_TARGET;
              if (refreshListener != null) loadMoreListener.onPullOverTarget();
            }
            /**
             * 上拉到临界值 然后下滑到小于临界值
             */
            else if (pullToUpStatus == PULL_OVER_TARGET
                && refreshParams.getScrollDirection() == RefreshParams.TOP_TO_BOTTOM
                && offset < bottomOffsetTarget) {
              pullToUpStatus = PULL_NOT_TARGET;
              if (refreshListener != null) loadMoreListener.onPullBelowTarget();
            }
            return false;
          }
        } else {
          refreshParams.movePos(ev);
          return super.dispatchTouchEvent(ev);
        }
        break;
      case MotionEvent.ACTION_UP:
        if (enableRefresh) {
          int offsetTop = refreshParams.getOffsetTop();
          if (offsetTop > 0) {
            if (offsetTop > topOffsetTarget) {
              refreshParams.setOffsetTop(topOffsetTarget);
              pullToDownStatus = REFRESH_ING;
              if (refreshListener != null) {
                refreshListener.onRefreshing();
              }
            } else {
              refreshParams.setOffsetTop(0);
              pullToDownStatus = INIT;
              if (refreshListener != null) {
                refreshListener.onReset();
              }
            }
            requestLayout();
          }
        }
        if (enableLoad) {
          int offsetBottom = refreshParams.getOffsetBottom();
          if (offsetBottom > 0) {
            if (offsetBottom > topOffsetTarget) {
              pullToDownStatus = REFRESH_ING;
              refreshParams.setOffsetBottom(topOffsetTarget);
              if (loadMoreListener != null) {
                loadMoreListener.onLoading();
              }
            } else {
              refreshParams.setOffsetBottom(0);
              pullToDownStatus = INIT;
              if (loadMoreListener != null) {
                loadMoreListener.onReset();
              }
            }
            requestLayout();
          }
        }

        break;
    }
    return super.dispatchTouchEvent(ev);
  }

  public void setRefresh(boolean refresh) {
    if (!refresh) {
      refreshParams.setOffsetTop(0);
      pullToDownStatus = INIT;
      if (refreshListener != null) {
        refreshListener.onRefreshComplete();
      }
    } else {
      refreshParams.setOffsetTop(topOffsetTarget);
      pullToDownStatus = REFRESH_ING;
      if (refreshListener != null) {
        refreshListener.onRefreshing();
      }
    }
    requestLayout();
  }

  public void setLoading(boolean loading) {
    if (!loading) {
      refreshParams.setOffsetBottom(0);
      pullToUpStatus = INIT;
      if (loadMoreListener != null) {
        loadMoreListener.onLoadingComplete();
      }
    } else {
      refreshParams.setOffsetBottom(topOffsetTarget);
      pullToUpStatus = REFRESH_ING;
      if (loadMoreListener != null) {
        loadMoreListener.onLoading();
      }
    }
    requestLayout();
  }

  /**
   * 判断子View是不是滑到最顶部
   */
  public static boolean canChildScrollUp(View view) {
    if (android.os.Build.VERSION.SDK_INT < 14) {
      if (view instanceof AbsListView) {
        final AbsListView absListView = (AbsListView) view;
        return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0
            || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
      } else {
        return view.getScrollY() > 0;
      }
    } else {
      return view.canScrollVertically(-1);
    }
  }

  /**
   * 判断子View是不是滑到最底部
   */
  public static boolean canChildScrollDown(View view) {
    if (android.os.Build.VERSION.SDK_INT < 14) {
      if (view instanceof AbsListView) {
        final AbsListView absListView = (AbsListView) view;
        return absListView.getChildCount() > 0 && (
            absListView.getLastVisiblePosition() < absListView.getCount() - 1
                || absListView.getChildAt(absListView.getCount() - 1).getPaddingBottom()
                < absListView.getPaddingBottom());
      } else {
        return view.getScrollY() < 0;
      }
    } else {
      return view.canScrollVertically(1);
    }
  }
}
