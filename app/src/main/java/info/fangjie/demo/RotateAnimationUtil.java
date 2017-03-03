package info.fangjie.demo;

import android.animation.ValueAnimator;
import android.widget.ImageView;

/**
 * Created by FangJie on 2017/3/3.
 */

public class RotateAnimationUtil {

  public static final int TIME=200;

  public static void startRotateAnimation(final ImageView view,int from,int to){
    ValueAnimator anim = ValueAnimator.ofInt(from, to);
    anim.setDuration(TIME);
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        view.setRotation((int) animation.getAnimatedValue());
      }
    });
    anim.start();
  }
}
