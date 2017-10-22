package co.tink.todoapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import co.tink.todoapp.R;
import co.tink.todoapp.activity.MainActivity;
import co.tink.todoapp.item.Item;

/**
 * Created by Cantador on 30.08.17.
 */

public class AdapterTodo extends RecyclerSwipeAdapter<AdapterTodo.ViewHolder> {

  private Context context;
  private List<Item> list;
  private int lastPosition = -1;
  private String format = "MMM d, yyyy";

  public AdapterTodo(Context context, List<Item> list) {
    this.context = context;
    this.list = list;
  }

  @Override
  public AdapterTodo.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todo, viewGroup, false);
    AdapterTodo.ViewHolder viewHolder = new AdapterTodo.ViewHolder(view);
    return viewHolder;
  }

  public int getItemCount() {
    return list.size();
  }

  @Override
  public void onBindViewHolder(final AdapterTodo.ViewHolder holder, int position) {
    View itemView = holder.itemView;
     final Item item = list.get(holder.getAdapterPosition());

    if (holder.getAdapterPosition() > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
      holder.itemView.startAnimation(animation);
      lastPosition = holder.getAdapterPosition();
    }
    holder.item.setBackgroundColor(context.getResources().getColor(android.R.color.white));
    holder.checkbox.setText(item.getTitle());
    holder.date.setText(DateFormat.format(format, item.getDate()));

    holder.date.setVisibility(item.getDone() ? View.VISIBLE : View.GONE);
    holder.checkbox.setChecked(item.getDone());
    holder.checkbox.setPaintFlags(item.getDone() ?
        holder.checkbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        :
        0);

    holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
    holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.wrapper));

    holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
      @Override
      public void onClose(SwipeLayout layout) {
        //when the SurfaceView totally cover the BottomView.
        holder.item.setBackgroundColor(context.getResources().getColor(android.R.color.white));
      }

      @Override
      public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
        //you are swiping.
      }

      @Override
      public void onStartOpen(SwipeLayout layout) {

      }

      @Override
      public void onOpen(SwipeLayout layout) {
        //when the BottomView totally show.
        holder.item.setBackgroundColor(context.getResources().getColor(R.color.colorAccent100));
      }

      @Override
      public void onStartClose(SwipeLayout layout) {

      }

      @Override
      public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
        //when user's hand released.
      }
    });


    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mItemManger.removeShownLayouts(holder.swipeLayout);
        ((MainActivity) context).removeItem(holder.getAdapterPosition());
        notifyDataSetChanged();
        mItemManger.closeAllItems();
      }
    });

    holder.checkbox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (holder.checkbox.isChecked()){
          Item itemNew = new Item(item.getTitle(), true, System.currentTimeMillis());
          ((MainActivity) context).setItem(holder.getAdapterPosition(), itemNew);
        } else {
          Item itemNew = new Item(item.getTitle(), false, item.getDate());
          ((MainActivity) context).setItem(holder.getAdapterPosition(), itemNew);
        }
        notifyDataSetChanged();
      }
    });

    mItemManger.bindView(holder.itemView, position);
  }

  @Override
  public int getSwipeLayoutResourceId(int position) {
    return R.id.swipe;
  }


  public static class ViewHolder extends RecyclerView.ViewHolder {
    protected ImageView deleteButton;
    protected TextView date;
    protected SwipeLayout swipeLayout;
    protected LinearLayout item;
    protected CheckBox checkbox;

    public ViewHolder(View view) {
      super(view);
      this.swipeLayout = itemView.findViewById(R.id.swipe);
      this.item = itemView.findViewById(R.id.item);
      this.checkbox = view.findViewById(R.id.checkbox);
      this.date = view.findViewById(R.id.date);
      this.deleteButton = view.findViewById(R.id.delete_button);
    }
  }

  @Override
  public void onViewDetachedFromWindow(AdapterTodo.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.itemView.clearAnimation();
  }
}