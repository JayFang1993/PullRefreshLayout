package info.fangjie.pulltorefresh;

/**
 * Created by FangJie on 2017/3/2.
 */

public interface RefreshListener {

  public void onPullBelowTarget();

  public void onPullOverTarget();

  public void onRefreshing();

  public void onPullBegin();

  public void onRefreshComplete();

  public void onReset();

  public void onPull(int offset);

}
