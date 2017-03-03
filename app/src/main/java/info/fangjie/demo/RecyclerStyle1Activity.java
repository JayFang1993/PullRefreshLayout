package info.fangjie.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import info.fangjie.pulltorefresh.DefaultLoadMoerListener;
import info.fangjie.pulltorefresh.DefaultRefreshListener;
import info.fangjie.pulltorefresh.PullRefreshLayout;

public class RecyclerStyle1Activity extends AppCompatActivity {

  private RecyclerView recyclerview;
  private RecyclerListAdapter adapter;
  private PullRefreshLayout pullrefreshlayout;
  private TextView textViewHeader, textViewBottom;

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.what == 1) {
        pullrefreshlayout.setRefresh(false);
      } else {
        pullrefreshlayout.setLoading(false);
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_style1);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("Recycler Style1");
    adapter = new RecyclerListAdapter(this);
    recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    recyclerview.setLayoutManager(new LinearLayoutManager(this));
    pullrefreshlayout = (PullRefreshLayout) findViewById(R.id.pullrefreshlayout);
    textViewHeader = (TextView) findViewById(R.id.tv_refresh_header);
    textViewBottom = (TextView) findViewById(R.id.tv_refresh_footer);

    recyclerview.setAdapter(adapter);
    pullrefreshlayout.setRefreshListener(new DefaultRefreshListener() {
      @Override public void onPullOverTarget() {
        textViewHeader.setText("松开刷新");
      }

      @Override public void onPullBelowTarget() {
        textViewHeader.setText("再拉一点就可以刷新了");
      }

      @Override public void onPullBegin() {
        textViewHeader.setText("再拉一点就可以刷新了");
      }

      @Override public void onRefreshing() {
        textViewHeader.setText("正在刷新");
        new Thread() {
          @Override public void run() {
            super.run();
            try {
              Thread.sleep(3000);
              handler.sendEmptyMessage(1);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          ;
        }.start();
      }
    });

    pullrefreshlayout.setLoadMoreListener(new DefaultLoadMoerListener() {

      @Override public void onPullBegin() {
        textViewBottom.setText("再拉一点");
      }

      @Override public void onPullBelowTarget() {
        textViewBottom.setText("再拉一点");
      }

      @Override public void onPullOverTarget() {
        textViewBottom.setText("松开就可以加载了");
      }

      @Override public void onLoading() {
        textViewBottom.setText("正在加载");
        new Thread() {
          @Override public void run() {
            super.run();
            try {
              Thread.sleep(3000);
              handler.sendEmptyMessage(2);
              adapter.setCount(adapter.getItemCount() + 20);
              adapter.notifyDataSetChanged();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          ;
        }.start();
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
