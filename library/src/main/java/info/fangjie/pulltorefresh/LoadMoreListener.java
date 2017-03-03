package info.fangjie.pulltorefresh;

/**
 * Created by FangJie on 2017/3/3.
 */

public interface LoadMoreListener {

  public void onLoading();

  public void onLoadingComplete();

  public void onPull(int offset);

  public void onPullBegin();

  public void onPullBelowTarget();

  public void onPullOverTarget();

  public void onReset();
}
