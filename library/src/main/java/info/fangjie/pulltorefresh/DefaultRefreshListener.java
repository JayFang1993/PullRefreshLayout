package info.fangjie.pulltorefresh;

/**
 * Created by FangJie on 2017/3/3.
 */

public  abstract class DefaultRefreshListener implements RefreshListener {
  @Override public void onRefreshComplete() {}
  @Override public void onReset() {}
  @Override public void onPull(int offset) {}
}
