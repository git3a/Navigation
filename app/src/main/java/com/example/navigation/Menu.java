package com.example.navigation;

import java.util.*;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    //for recipeXML
    private ArrayList<String> mIndex = new ArrayList<>();
    private ArrayList<String> mInner = new ArrayList<>();

    //for homeXML
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();

    private List<String> name = new ArrayList<String>();
    private  List<String> picurl = new ArrayList<String>();
    private  List<Integer> id = new ArrayList<>();

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private TextView recipeName;
    private ImageView imageView;
    private EditText mEditSearch;

    //for build new receipe
    private ImageButton mnew_receipe;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_favorite:
                    Intent intent = new Intent(Menu.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(Menu.this, com.example.navigation.List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my:
                    intent = new Intent(Menu.this, My.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    private boolean lock = true; // thread lock
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        //mTextMessage = (TextView) findViewById(R.id.message);
        initImageBitmaps();
        mEditSearch = (EditText)findViewById(R.id.edit_search);
        mEditSearch.setFocusable(false);
        mEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        //loadHome();
        // Click the plus and turn to page initialize the recipe
        mnew_receipe = (ImageButton)findViewById(R.id.imageButton3);
        mnew_receipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,BuildNewRecipe.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
    protected void onStart() {
        super.onStart();
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps.");
        //Here to process your JSON, Leader.
        getPhotoName();
        initRecyclerView();
    }

    private void loadmoreBitmaps(){
        Log.d(TAG,"loadmoreBitmaps: preparing bitmaps.");
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Here to process your JSON, Leader.
                getPhotoName();

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
                new StaggeredRecyclerViewAdapter(this,mNames,mImageUrls,mIds);

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
                if(isScrolling && (currentItems + scrollOutItems > totalItems)){
                    //data fetch here, leader
                    isScrolling=false;
                    loadmoreBitmaps();
                }
            }
        });
    }

    private void getPhotoName() {
        String getUrl = "http://35.188.105.219/back_end/getrecipe";
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
                //Message msg = handler.obtainMessage();
                //msg.obj = response.body().string();
               // handler.sendMessage(msg);

                String m = response.body().string();
                Map<String,List> map = (Map)JSONObject.parse(m);
                name = map.get("name");
                picurl = map.get("image");
                id = map.get("id");
                lock = false;
            }
        });
        while (lock) {System.out.println("locked");}
        System.out.println("UnLock");
        Iterator it_name = name.iterator();
        Iterator it_url = picurl.iterator();
        Iterator it_id = id.iterator();
        while(it_name.hasNext() && it_url.hasNext()) {
            String name = (String)it_name.next();
            String url = (String)it_url.next();
            Integer id = (Integer)it_id.next();

            mImageUrls.add(url);
            mNames.add(name);
            mIds.add(id);
            lock = true;
        }
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            lock = false;
            try {
                String m = (String) msg.obj;
                Map<String,List<String>> map = (Map)JSONObject.parse(m);
                name = map.get("name");
                picurl = map.get("image");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

}
