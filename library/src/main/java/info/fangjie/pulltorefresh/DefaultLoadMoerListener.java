package info.fangjie.pulltorefresh;

/**
 * Created by FangJie on 2017/3/3.
 */

public abstract class DefaultLoadMoerListener implements LoadMoreListener {

  @Override public void onReset() {}
  @Override public void onPull(int offset) {}
  @Override public void onLoadingComplete() {}
}
