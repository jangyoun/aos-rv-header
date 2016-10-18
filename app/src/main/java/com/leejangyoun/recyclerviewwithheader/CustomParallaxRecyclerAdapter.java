package com.leejangyoun.recyclerviewwithheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.List;

public class CustomParallaxRecyclerAdapter extends ParallaxRecyclerAdapter<Fruits> {

    private final int VIEW_ITEM     = 0;
    private final int VIEW_GROUP    = 1;
    private final int VIEW_SLIDE    = 2;
    private final int VIEW_PROGRESS = 3;


    Context mContext;
    List mData;
    LayoutInflater mLayoutInflater;

    //=======================================================================
    // METHOD : CustomParallaxRecyclerAdapter
    //=======================================================================
    public CustomParallaxRecyclerAdapter(Context context, List<Fruits> data) {
        super(data);
        mData = data;
        mContext = context;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //=======================================================================
    // METHOD : onCreateViewHolderImpl
    //=======================================================================
    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<Fruits> adapter, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_cell_item, viewGroup, false);
            viewHolder = new ItemHolder(view);

        } else if(viewType == VIEW_GROUP) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_cell_group, viewGroup, false);
            viewHolder = new GroupHolder(view);

        } else if(viewType == VIEW_PROGRESS) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer_progress, viewGroup, false);
            viewHolder = new ProgressHolder(view);

        }

        return viewHolder;
    }

    //=======================================================================
    // METHOD : onBindViewHolderImpl
    //=======================================================================
    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder holder, ParallaxRecyclerAdapter<Fruits> adapter, int position) {
        if(position == adapter.getData().size() - 1)
            if(listener != null) listener.onScrollToBottom();


        if(holder instanceof ItemHolder) {
            Fruits item = adapter.getData().get(position);
            ((ItemHolder)holder).txt.setText(item.getItem().getTitle());
            Glide.with(mContext).load(item.getItem().getThumb()).into(((ItemHolder)holder).img);

        } else if(holder instanceof GroupHolder) {
            Fruits item = adapter.getData().get(position);
            List<Fruits.Fruit> gArr = item.getGroupArr();

            ((GroupHolder)holder).groupTxt.setText(item.getGroupTitle());

            LinearLayout linear = ((GroupHolder)holder).linear;
            linear.removeAllViews();
            for (Fruits.Fruit gItem : gArr) {
                View innerView = mLayoutInflater.inflate(R.layout.cell_in_group, null);

                TextView tv = (TextView)innerView.findViewById(R.id.txt);
                tv.setText(gItem.getTitle());

                ImageView iv = (ImageView)innerView.findViewById(R.id.img);
                Glide.with(mContext).load(gItem.getThumb()).into(iv);

                linear.addView(innerView);
            }
        }
    }


    //=======================================================================
    // METHOD : getItemCountImpl
    //=======================================================================
    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<Fruits> adapter) {
        return mData.size();
    }

    // =======================================================================
    // METHOD : getItemViewType
    // =======================================================================
    @Override
    public int getItemViewType(int position) {

        if(position == 0) // header
            return VIEW_SLIDE;

        Fruits.TYPE itemType = getData().get(position-1).getType();
        if(itemType == Fruits.TYPE.ITEM) {
            return VIEW_ITEM;

        } else if(itemType == Fruits.TYPE.GROUP) {
            return VIEW_GROUP;

        } else if(itemType == Fruits.TYPE.PROGRESS) {
            return VIEW_PROGRESS;
        }

        return VIEW_ITEM;
    }

    //=======================================================================
    // METHOD : ItemHolder
    //=======================================================================
    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView txt;
        private ImageView img;

        public ItemHolder(View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.txt);
            img = (ImageView) view.findViewById(R.id.img);
        }

    }

    // =======================================================================
    // METHOD : GroupHolder
    // =======================================================================
    class GroupHolder extends RecyclerView.ViewHolder {

        private LinearLayout linear;
        private TextView groupTxt;

        public GroupHolder(View view) {
            super(view);
            linear = (LinearLayout) view.findViewById(R.id.linear_in_scroll);
            groupTxt = (TextView) view.findViewById(R.id.group_txt);
        }
    }

    // =======================================================================
    // METHOD : ProgressHolder
    // =======================================================================
    class ProgressHolder extends RecyclerView.ViewHolder {
        public ProgressHolder(View view) {
            super(view);
        }
    }

    //=======================================================================
    // METHOD : setListener
    //=======================================================================
    CustomParallaxRecyclerAdapterListener listener;
    public void setListener(CustomParallaxRecyclerAdapterListener listener){
        this.listener = listener;
    }
    public interface CustomParallaxRecyclerAdapterListener {
        void onScrollToBottom();
    }
}
