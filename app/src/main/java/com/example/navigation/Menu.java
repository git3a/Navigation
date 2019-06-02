package com.example.navigation;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.*;
import com.alibaba.fastjson.*;

public class Menu extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView imageView;
    private BottomNavigationView navigation;
    private TextView recipeName;

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
        //mTextMessage = (TextView) findViewById(R.id.message);

        loadHome();
    }
    private void loadHome() {

        setContentView(R.layout.activity_home);
        //mTextMessage = (TextView) findViewById(R.id.message);
        recipeName = findViewById(R.id.name);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        imageView = findViewById(R.id.image7);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Recipe.class);
                startActivity(intent);
                System.out.println("click pic");
            }
        });
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
