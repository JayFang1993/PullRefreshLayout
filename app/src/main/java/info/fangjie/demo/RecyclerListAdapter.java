package info.fangjie.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by FangJie on 2017/3/2.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>{

  private Context context;

  private int count=20;

  public void setCount(int count) {
    this.count = count;
  }

  public RecyclerListAdapter(Context context){
    this.context=context;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText((position+1)+": This is Title");
  }

  @Override public int getItemCount() {
    return count;
  }

  class ViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public ViewHolder(View itemView) {
      super(itemView);
      textView=(TextView) itemView.findViewById(R.id.tv_item_title);

    }
  }
}

