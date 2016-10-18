package com.leejangyoun.recyclerviewwithheader;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.leejangyoun.recyclerviewwithheader.R.id.linear_indicator;


public class SlideView extends FrameLayout {


    Context mContext;
    View    mView;

    int mMaxPageCnt;
    ImageView mPageIndicator[];

    List<Fruits.Fruit> mData;

    // =======================================================================
    // METHOD : SlideView
    // =======================================================================
    public SlideView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    // =======================================================================
    // METHOD : SlideView
    // =======================================================================
    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    // =======================================================================
    // METHOD : SlideView
    // =======================================================================
    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    // =======================================================================
    // METHOD : init
    // =======================================================================
    public void init() {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.recycler_header, null);

        this.addView(mView);
    }

    // =======================================================================
    // METHOD : setData
    // =======================================================================
    public void setData(List<Fruits.Fruit> data) {
        this.mData = data;

        if(data == null) {
            mMaxPageCnt = 0;
            return;
        }

        mMaxPageCnt = data.size();
    }

    // =======================================================================
    // METHOD : notifyDataSetChanged
    // =======================================================================
    public void notifyDataSetChanged() {
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new CustomOnPageChangeListener());

        LinearLayout linearIndicator = (LinearLayout) mView.findViewById(linear_indicator);
        linearIndicator.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25, 25);
        lp.setMargins(10, 10, 10, 10);

        mPageIndicator = new ImageView[mMaxPageCnt];
        for (int i = 0; i < mMaxPageCnt; i++) {
            mPageIndicator[i] = new ImageView(mContext);
            mPageIndicator[i].setBackgroundColor(Color.BLUE);
            mPageIndicator[i].setLayoutParams(lp);
            mPageIndicator[i].setBackgroundResource(i==0 ? R.drawable.page_w_act : R.drawable.page_w_inact);
            linearIndicator.addView(mPageIndicator[i]);
        }
    }

    // =======================================================================
    // METHOD : CustomOnPageChangeListener
    // =======================================================================
    class CustomOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mMaxPageCnt; i++)
                mPageIndicator[i].setBackgroundResource(i == position ? R.drawable.page_w_act : R.drawable.page_w_inact);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //=======================================================================
    // METHOD : ViewPagerAdapter
    //=======================================================================
    class ViewPagerAdapter extends PagerAdapter {

        public ViewPagerAdapter() {

        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater alertInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = alertInflater.inflate(R.layout.cell_in_header, null);

            ((TextView)view.findViewById(R.id.txt)).setText(mData.get(position).getTitle());
            Glide.with(mContext).load(mData.get(position).getThumb()).into((ImageView)view.findViewById(R.id.img));

            ((ViewPager) container).addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

}