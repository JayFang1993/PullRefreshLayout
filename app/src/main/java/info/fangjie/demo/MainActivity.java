package info.fangjie.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by FangJie on 2017/3/3.
 */

public class MainActivity extends AppCompatActivity {

  private Button recyclerViewStyle1;
  private Button recyclerViewStyle2;
  private Button recyclerViewStyle3;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerViewStyle1=(Button)findViewById(R.id.btn_recycler_style1);
    recyclerViewStyle2=(Button)findViewById(R.id.btn_recycler_style2);

    recyclerViewStyle1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,RecyclerStyle1Activity.class);
        startActivity(intent);
      }
    });

    recyclerViewStyle2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,RecyclerStyle2Activity.class);
        startActivity(intent);
      }
    });



  }
}
