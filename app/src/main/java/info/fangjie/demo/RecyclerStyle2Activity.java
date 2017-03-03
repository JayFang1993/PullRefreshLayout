package info.fangjie.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import info.fangjie.pulltorefresh.DefaultLoadMoerListener;
import info.fangjie.pulltorefresh.DefaultRefreshListener;
import info.fangjie.pulltorefresh.PullRefreshLayout;

public class RecyclerStyle2Activity extends AppCompatActivity {

  private RecyclerView recyclerview;
  private RecyclerListAdapter adapter;
  private PullRefreshLayout pullrefreshlayout;
  private TextView textViewHeaderTitle,textViewHeaderContent;
  private ProgressBar progressBarHeader,progressBarFooter;
  private ImageView imageViewArrow,imageViewArrowFooter;
  private Handler handler=new Handler(){
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.what==1){
        pullrefreshlayout.setRefresh(false);
      }else {
        pullrefreshlayout.setLoading(false);
      }

    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_style2);
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("Recycler Style2");

    adapter=new RecyclerListAdapter(this);
    recyclerview=(RecyclerView) findViewById(R.id.recyclerview);
    pullrefreshlayout=(PullRefreshLayout)findViewById(R.id.pullrefreshlayout);

    textViewHeaderTitle=(TextView)findViewById(R.id.tv_refresh_header_title);
    textViewHeaderContent=(TextView)findViewById(R.id.tv_refresh_header_content);
    imageViewArrowFooter=(ImageView)findViewById(R.id.iv_load_footer);
    progressBarHeader=(ProgressBar)findViewById(R.id.pb_refresh_header);
    progressBarFooter=(ProgressBar)findViewById(R.id.pb_load_footer);
    imageViewArrow=(ImageView)findViewById(R.id.iv_refresh_header_arrow);

    recyclerview.setLayoutManager(new LinearLayoutManager(this));
    recyclerview.setAdapter(adapter);
    pullrefreshlayout.setRefreshListener(new DefaultRefreshListener() {
      @Override public void onPullOverTarget() {
        textViewHeaderTitle.setText("松开刷新");
        RotateAnimationUtil.startRotateAnimation(imageViewArrow,180,0);
      }

      @Override public void onPullBelowTarget() {
        textViewHeaderTitle.setText("下拉刷新");
        RotateAnimationUtil.startRotateAnimation(imageViewArrow,0,180);
      }

      @Override public void onRefreshing() {
        textViewHeaderTitle.setText("正在刷新");
        imageViewArrow.setVisibility(View.GONE);
        progressBarHeader.setVisibility(View.VISIBLE);
        new Thread(){
          @Override public void run() {
            super.run();
            try {
              /**
               * 模拟异步拉取数据
               */
              Thread.sleep(3000);
              handler.sendEmptyMessage(1);
            }catch (Exception e){
              e.printStackTrace();
            }
          };
        }.start();
      }

      @Override public void onPullBegin() {
        textViewHeaderTitle.setText("下拉刷新");
        imageViewArrow.setVisibility(View.VISIBLE);
        progressBarHeader.setVisibility(View.GONE);
        imageViewArrow.setRotation(180);
      }
    });

    pullrefreshlayout.setLoadMoreListener(new DefaultLoadMoerListener() {
      @Override public void onLoading() {
        progressBarFooter.setVisibility(View.VISIBLE);
        imageViewArrowFooter.setVisibility(View.GONE);
        new Thread(){
          @Override public void run() {
            super.run();
            try {
              /**
               * 模拟异步拉取数据
               */
              Thread.sleep(3000);
              handler.sendEmptyMessage(2);
              adapter.setCount(adapter.getItemCount()+20);
              adapter.notifyDataSetChanged();
            }catch (Exception e){
              e.printStackTrace();
            }
          };
        }.start();
      }

      @Override public void onPullBegin() {
        progressBarFooter.setVisibility(View.GONE);
        imageViewArrowFooter.setVisibility(View.VISIBLE);
        imageViewArrowFooter.setRotation(0);
      }

      @Override public void onPullBelowTarget() {
        RotateAnimationUtil.startRotateAnimation(imageViewArrowFooter,180,0);
      }

      @Override public void onPullOverTarget() {
        RotateAnimationUtil.startRotateAnimation(imageViewArrowFooter,0,180);
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
