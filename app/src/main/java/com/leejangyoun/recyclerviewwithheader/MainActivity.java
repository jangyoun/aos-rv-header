package com.leejangyoun.recyclerviewwithheader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Context mContext;

    Toolbar mToolbar;
    int mHeaderHeight;

    SwipeRefreshLayout mSwipeRefresh;

    List<Fruits> mArrList;
    CustomParallaxRecyclerAdapter mAdapter;

    SlideView mSlideView;

    RequestQueue mQueue;

    int mPage = 1;
    boolean mIsLastItem = false;

    // =======================================================================
    // METHOD : onCreate
    // =======================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.getBackground().setAlpha(0);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // toolbar height
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            mHeaderHeight -= TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());

        // swipeRefresh
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mQueue == null)
                    return;

                mPage = 1;
                mIsLastItem = false;

                String url = "http://leejangyoun.com/android/dummy/recyclerViewWithHeader_1.json";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new CustomSuccessListener(true), new CustomErrorListener());
                mQueue.add(stringRequest);
            }
        });

        //set recyclerview
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);

        // set layoutManager for recyclerview
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) // slide header
                    return 2;

                Fruits.TYPE type = mAdapter.getData().get(position - 1).getType();
                if (type == Fruits.TYPE.ITEM)
                    return 1;
                else
                    return 2;
            }
        });
        recycler.setLayoutManager(manager);

        // set slide header
        mSlideView = new SlideView(mContext);

        // set ArrayList. adapter for recyclerview
        mArrList = new ArrayList<>();
        mAdapter = new CustomParallaxRecyclerAdapter(mContext, mArrList);
        mAdapter.setListener(new CustomParallaxRecyclerAdapter.CustomParallaxRecyclerAdapterListener() {
            @Override
            public void onScrollToBottom() {
                // bottom to update
                if(mQueue == null)
                    return;

                if(mIsLastItem)
                    return;

                String url = "http://leejangyoun.com/android/dummy/recyclerViewWithHeader_" + (++mPage) + ".json";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new CustomSuccessListener(false), new CustomErrorListener());
                mQueue.add(stringRequest);
            }
        });
        mAdapter.setParallaxHeader(mSlideView, recycler);
        mAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                int alpha;
                if(offset > mHeaderHeight) alpha = 255;
                else if(offset < 0)        alpha = 0;
                else                       alpha = (int) ((255.0 / mHeaderHeight) * offset);
                mToolbar.getBackground().setAlpha(alpha);
            }
        });
        recycler.setAdapter(mAdapter);

        //set http queue
        mQueue = Volley.newRequestQueue(mContext);
        String url = "http://leejangyoun.com/android/dummy/recyclerViewWithHeader_" + mPage + ".json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new CustomSuccessListener(false), new CustomErrorListener());
        mQueue.add(stringRequest);
    }

    // =======================================================================
    // METHOD : CustomSuccessListener
    // =======================================================================
    private  class CustomSuccessListener implements Response.Listener<String> {

        boolean clear;

        public CustomSuccessListener(boolean clear) {
            this.clear = clear;
        }

        @Override
        public void onResponse(String response) {

            if(clear)
                mArrList.clear();

            // remove progress footer
            for (Fruits item : mArrList) {
                if (item.getType().equals(Fruits.TYPE.PROGRESS)) {
                    mArrList.remove(item);
                    break;
                }
            }

            Gson gson = new Gson();

            JsonObject root = new JsonParser().parse(response).getAsJsonObject();

            mIsLastItem = root.get("last").getAsBoolean();
            Log.i("TEST", "last : " + mIsLastItem);

            // set slide header data
            if(root.has("slideList")) {
                List<Fruits.Fruit> slidArr = new ArrayList<>();
                JsonArray slideNode = root.get("slideList").getAsJsonArray();
                for (JsonElement jEle : slideNode) {
                    Fruits.Fruit fruit = gson.fromJson(jEle.getAsJsonObject(), Fruits.Fruit.class);
                    slidArr.add(fruit);
                }
                mSlideView.setData(slidArr);
                mSlideView.notifyDataSetChanged();
            }

            // set body data
            JsonArray bodyNode = root.get("arrList").getAsJsonArray();
            for (JsonElement jEle : bodyNode) {
                String type = jEle.getAsJsonObject().get("type").getAsString();

                if(type.equals(Fruits.TYPE.ITEM.toString())) { // item
                    Fruits fruits = new Fruits(Fruits.TYPE.ITEM);
                    Fruits.Fruit fruit = gson.fromJson(jEle.getAsJsonObject().get("items"), Fruits.Fruit.class);
                    fruits.setFruitItem(fruit);
                    mArrList.add(fruits);

                } else if(type.equals(Fruits.TYPE.GROUP.toString())) { // group
                    JsonObject groupObj = jEle.getAsJsonObject().get("groups").getAsJsonObject();
                    String groupTitle = groupObj.get("gTitle").getAsString();
                    int groupNo = groupObj.get("gNo").getAsInt();
                    Fruits group = new Fruits(Fruits.TYPE.GROUP);
                    group.setGroups(groupNo, groupTitle);
                    JsonArray groupNode = groupObj.get("groupList").getAsJsonArray();
                    for (JsonElement gEle : groupNode) {
                        Fruits.Fruit fruit = gson.fromJson(gEle.getAsJsonObject(), Fruits.Fruit.class);
                        group.addGroups(fruit);
                    }
                    mArrList.add(group);
                }
            }

            // add progress footer
            if ( ! mIsLastItem)
                mArrList.add(new Fruits(Fruits.TYPE.PROGRESS));

            // notifyDataSetChanged
            mAdapter.notifyDataSetChanged();

            // stop swipeRefresh
            mSwipeRefresh.setRefreshing(false);
            mSwipeRefresh.setEnabled(true);
        }
    }

    // =======================================================================
    // METHOD : CustomErrorListener
    // =======================================================================
    private  class CustomErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("TEST", "error : " + error.getMessage());
        }
    }
}
