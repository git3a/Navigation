package com.example.navigation;

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

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class List extends AppCompatActivity {

    Boolean isScrolling = false;
    ProgressBar progressBar;
    ListRecyclerViewAdapter staggeredRecyclerViewAdapter;
    int currentItems,totalItems,scrollOutItems;
    private static final String TAG = "List";
    private static final int NUM_COLUMNS = 1;
    private ArrayList<String> materials = new ArrayList<>();
    private ArrayList<String> amounts = new ArrayList<>();
    //private ArrayList<Integer> mIds = new ArrayList<>();

    private java.util.List<String> name = new ArrayList<String>();
    private java.util.List<String> picurl = new ArrayList<String>();


    //private java.util.List<Integer> id = new ArrayList<>();

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private TextView recipeName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(List.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(List.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:

                    return true;
                case R.id.navigation_my:
                    intent = new Intent(List.this, My.class);
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
        recipeName.setText("Recipe Name");
        materials.add("mete");
        materials.add("mete");
        materials.add("mete");
        materials.add("mete");
        amounts.add("2kg");
        amounts.add("2kg");
        amounts.add("2kg");
        amounts.add("2kg");
    }

}

