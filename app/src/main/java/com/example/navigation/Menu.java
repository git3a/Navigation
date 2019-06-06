package com.example.navigation;

import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.*;
import com.alibaba.fastjson.*;

public class Menu extends AppCompatActivity {
    Boolean isScrolling = false;
    ProgressBar progressBar;
    StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    int currentItems,totalItems,scrollOutItems;
    private static final String TAG = "Menu";
    private static final int NUM_COLUMNS = 2;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private TextView recipeName;
    private ImageView imageView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadHome();
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //mTextMessage = (TextView) findViewById(R.id.message);
        initImageBitmaps();
        //loadHome();
    }
    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps.");
        //Here to process your JSON, Leader.
        mImageUrls.add("https://assets3.thrillist.com/v1/image/2797371/size/tmg-article_default_mobile.jpg");
        mNames.add("Hamberger");
        mImageUrls.add("https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/one_pot_chorizo_and_15611_16x9.jpg");
        mNames.add("noodles");
        mImageUrls.add("https://www.bbcgoodfood.com/sites/default/files/guide/guide-image/2018/06/chicken-wings-main.jpg");
        mNames.add("Chicken Wings");
        mImageUrls.add("https://assets3.thrillist.com/v1/image/2815308/size/tmg-article_default_mobile.jpg");
        mNames.add("Fried Chiken");
        mImageUrls.add("https://wi-images.condecdn.net/image/V1RQ0lZ8pgQ/crop/3240/f/istock-459025743.jpg");
        mNames.add("Lunch Meat");
        mImageUrls.add("https://www.godairyfree.org/wp-content/uploads/2017/11/fast-food-feature-2.jpg");
        mNames.add("French fries");
        mImageUrls.add("https://2q0p8d1c7z0l2s9xl3323ez3-wpengine.netdna-ssl.com/wp-content/uploads/2018/04/Ultimate-Tokyo-Japan-Food-Guide-Featured.png");
        mNames.add("海鮮丼");
        mImageUrls.add("https://assets3.thrillist.com/v1/image/2797371/size/tmg-article_default_mobile.jpg");
        mNames.add("Hamberger");
        mImageUrls.add("https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/one_pot_chorizo_and_15611_16x9.jpg");
        mNames.add("noodles");
        mImageUrls.add("https://www.bbcgoodfood.com/sites/default/files/guide/guide-image/2018/06/chicken-wings-main.jpg");
        mNames.add("Chicken Wings");
        mImageUrls.add("https://assets3.thrillist.com/v1/image/2815308/size/tmg-article_default_mobile.jpg");
        mNames.add("Fried Chiken");
        mImageUrls.add("https://wi-images.condecdn.net/image/V1RQ0lZ8pgQ/crop/3240/f/istock-459025743.jpg");
        mNames.add("Lunch Meat");
        mImageUrls.add("https://www.godairyfree.org/wp-content/uploads/2017/11/fast-food-feature-2.jpg");
        mNames.add("French fries");
        mImageUrls.add("https://2q0p8d1c7z0l2s9xl3323ez3-wpengine.netdna-ssl.com/wp-content/uploads/2018/04/Ultimate-Tokyo-Japan-Food-Guide-Featured.png");
        mNames.add("海鮮丼");
        initRecyclerView();
    }

    private void loadmoreBitmaps(){
        Log.d(TAG,"loadmoreBitmaps: preparing bitmaps.");
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Here to process your JSON, Leader.
                mImageUrls.add("https://assets3.thrillist.com/v1/image/2797371/size/tmg-article_default_mobile.jpg");
                mNames.add("Hamberger");
                mImageUrls.add("https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/one_pot_chorizo_and_15611_16x9.jpg");
                mNames.add("noodles");
                mImageUrls.add("https://www.bbcgoodfood.com/sites/default/files/guide/guide-image/2018/06/chicken-wings-main.jpg");
                mNames.add("Chicken Wings");
                mImageUrls.add("https://assets3.thrillist.com/v1/image/2815308/size/tmg-article_default_mobile.jpg");
                mNames.add("Fried Chiken");
                mImageUrls.add("https://wi-images.condecdn.net/image/V1RQ0lZ8pgQ/crop/3240/f/istock-459025743.jpg");
                mNames.add("Lunch Meat");
                mImageUrls.add("https://www.godairyfree.org/wp-content/uploads/2017/11/fast-food-feature-2.jpg");
                mNames.add("French fries");
                mImageUrls.add("https://2q0p8d1c7z0l2s9xl3323ez3-wpengine.netdna-ssl.com/wp-content/uploads/2018/04/Ultimate-Tokyo-Japan-Food-Guide-Featured.png");
                mNames.add("海鮮丼");
                staggeredRecyclerViewAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        },2000);
    }
    private void initRecyclerView(){
        Log.d(TAG,"initRecyclerView: initializing staggered recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        staggeredRecyclerViewAdapter =
                new StaggeredRecyclerViewAdapter(this,mNames,mImageUrls);

        //perfectly solve the blink problem
        staggeredRecyclerViewAdapter.setHasStableIds(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        //forbid swap side of two columns
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);

        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = staggeredGridLayoutManager.getChildCount();
                totalItems = staggeredGridLayoutManager.getItemCount();
                int[] firstVisibleItem=null;
                firstVisibleItem = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItem);
                if(firstVisibleItem != null && firstVisibleItem.length > 0) {
                    scrollOutItems = firstVisibleItem[0];
                }
                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    //data fetch here, leader
                    isScrolling=false;
                    loadmoreBitmaps();
                }
            }
        });
    }
    private void loadHome() {
        //setContentView(R.layout.activity_home);
        //mTextMessage = (TextView) findViewById(R.id.message);
        //recipeName = findViewById(R.id.textView1);
        //navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        imageView = findViewById(R.id.imageView1);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Menu.this, Recipe.class);
//                startActivity(intent);
//                System.out.println("click pic");
//            }
//        });
        getPhotoName();
        System.out.println("load home");
    }

    private void getPhotoName() {
        String getUrl = "http://192.168.2.102:8000/get";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage();
                System.out.println(err);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // final String data = response.body().string();
                System.out.println("onResponse");
                Message msg = handler.obtainMessage();
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String m = (String) msg.obj;
            Map<String,String> map = (Map)JSONObject.parse(m);
            String name = map.get("recipe_name");
            String picurl = map.get("img_html");
            try {
                Picasso.get().load(picurl).into(imageView);
                recipeName.setText(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

}
