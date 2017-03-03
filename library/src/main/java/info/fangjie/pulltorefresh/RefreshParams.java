package info.fangjie.pulltorefresh;

import android.view.MotionEvent;

/**
 * Created by FangJie on 2017/3/2.
 */

public class RefreshParams {

  private float downY;
  private float lastY;
  private float currentY;

  private int offsetTop;  // 下拉偏移量
  private boolean offsetTopChanged = false;

  private int offsetBottom;
  private boolean offsetBottomChanged = false;

  public int getOffsetTop() {
    return offsetTop;
  }

  public void setOffsetTop(int offsetTop) {
    this.offsetTop = offsetTop;
  }

  public void setOffsetBottom(int offsetBottom) {
    this.offsetBottom = offsetBottom;
  }

  public int getOffsetBottom() {
    return offsetBottom;
  }

  public static final int TOP = 1;
  public static final int BOTTOM = 2;

  public void pressDown(MotionEvent motionEvent) {
    downY = motionEvent.getY();
    lastY = downY;
    currentY = downY;
  }

  public void movePos(MotionEvent motionEvent) {
    offsetTopChanged = false;
    offsetBottomChanged = false;
    lastY = currentY;
    currentY = motionEvent.getY();
  }
  /**
   * @param direction 内容区域滑到最顶部，内容区域滑到最底部
   */
  public void movePos(MotionEvent motionEvent, int direction) {
    offsetTopChanged = false;
    offsetBottomChanged = false;
    lastY = currentY;
    currentY = motionEvent.getY();
    if (direction == BOTTOM && (offsetBottom > 0 || (offsetTop == 0 && offsetBottom == 0
        && (lastY - currentY) > 0))) { // 操作的是上拉
      if ((lastY - currentY) > 0) {   // 上拉上滑
        offsetBottom += (lastY - currentY);
        offsetBottomChanged = true;
        scrollDirection = BOTTOM_TO_TOP;
      } else {
        offsetBottom -= (currentY - lastY);
        offsetBottomChanged = true;
        offsetBottom = (offsetBottom < 0 ? 0 : offsetBottom);
        scrollDirection = TOP_TO_BOTTOM;
      }
    }
    if (direction == TOP && (offsetTop > 0 || (offsetBottom == 0 && offsetTop == 0
        && (currentY - lastY) > 0))) {// 操作的是下拉
      if ((lastY - currentY) > 0) { // 下拉上滑
        offsetTop -= (lastY - currentY);
        offsetTopChanged = true;
        offsetTop = (offsetTop < 0 ? 0 : offsetTop);
        scrollDirection = BOTTOM_TO_TOP;
      } else {  // 下拉下滑
        offsetTop += (currentY - lastY);
        offsetTopChanged = true;
        scrollDirection = TOP_TO_BOTTOM;
      }
    }
  }

  public boolean isOffsetBottomChanged() {
    return offsetBottomChanged;
  }

  private int scrollDirection = TOP_TO_BOTTOM; // 向上滑  向下滑
  public final static int TOP_TO_BOTTOM = 1;
  public final static int BOTTOM_TO_TOP = 2;

  public int getScrollDirection() {
    return scrollDirection;
  }

  public boolean isOffsetTopChanged() {
    return offsetTopChanged;
  }
}
