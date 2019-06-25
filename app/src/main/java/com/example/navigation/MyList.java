package com.example.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyList extends AppCompatActivity {

    Boolean isScrolling = false;
    ProgressBar progressBar;
    ListRecyclerViewAdapter staggeredRecyclerViewAdapter;
    int currentItems,totalItems,scrollOutItems;
    private static final String TAG = "MyList";
    private static final int NUM_COLUMNS = 1;
    private ArrayList<String> materials = new ArrayList<>();
    private ArrayList<String> amounts = new ArrayList<>();
    //private ArrayList<Integer> mIds = new ArrayList<>();

    private java.util.List<String> name = new ArrayList<String>();
    private java.util.List<String> picurl = new ArrayList<String>();


    //private java.util.MyList<Integer> id = new ArrayList<>();

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private TextView recipeName;
    private String recipename;
    private java.util.List<String> mylists = new ArrayList<String>();
    private ArrayList<String> ingredient_names = new ArrayList<>();
    private ArrayList<String> ingredient_quantities = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(MyList.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(MyList.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:

                    return true;
                case R.id.navigation_my:
                    intent = new Intent(MyList.this, My.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private boolean lock = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_list);
        getList();
        loadRecipe();
    }
    private void loadRecipe() {
        //Here to process your JSON, Leader.

        recipeName = findViewById(R.id.textView);

        loaddata();
        initRecyclerView();
    }

    private void loadmoreBitmaps(){
        Log.d(TAG,"loadmoreBitmaps: preparing bitmaps.");
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Here to process your JSON, Leader.
                loaddata();

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
                new ListRecyclerViewAdapter(this,materials,amounts);

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

    private void loaddata() {

        materials.add("");
        amounts.add("");
        Iterator it_list = mylists.iterator();
        while(it_list.hasNext()) {
            String id = (String)it_list.next();
            getRecipeData(id);
            recipeName.setText(recipename);
            Iterator it_name = ingredient_names.iterator();
            Iterator it_qu = ingredient_quantities.iterator();

            while(it_name.hasNext() && it_qu.hasNext()) {
                materials.add((String)it_name.next());
                amounts.add((String)it_qu.next());
            }
        }
    }
    private void getRecipeData(String id) {
        Request.Builder reqBuild = new Request.Builder().get();
        lock = true;
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.188.105.219/back_end/getrecipebyid")
               .newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.10:8000/getrecipebyid")
        //        .newBuilder();
        urlBuilder.addQueryParameter("id", id);

        OkHttpClient okHttpClient = new OkHttpClient();
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();
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

                try {
                    Map<String, String> map = (Map) JSONObject.parse(response.body().string());
                    recipename = map.get("name");
                    String names[] = map.get("material").split("\n");
                    String quantities[] = map.get("amount").split("\n");


                    for (int i = 0; i < names.length; i++) {
                        ingredient_names.add(names[i]);
                    }
                    for (int i = 0; i < quantities.length; i++) {
                        ingredient_quantities.add(quantities[i]);
                    }
                    lock = false;
                }catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        while(lock) {System.out.println("locked");}
    }
    private void getList() {
        lock = true;
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");

        Request.Builder reqBuild = new Request.Builder().get();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.188.105.219/back_end/getList")
                .newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.10:8000/getList")
        //        .newBuilder();
        urlBuilder.addQueryParameter("id", userid);

        OkHttpClient okHttpClient = new OkHttpClient();
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

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

                String m = response.body().string();
                Map<String,List> map = (Map) JSONObject.parse(m);
                mylists = map.get("list");
                lock = false;
            }

        });
        while (lock) {}

    }
}

