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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.alibaba.fastjson.*;

public class  Recipe extends AppCompatActivity {

    private ImageView imageView;
    private BottomNavigationView navigation;
    private TextView recipeName;
    private TextView ingredient_name1;
    private TextView ingredient_quantity1;
    private TextView ingredient_name2;
    private TextView ingredient_quantity2;
    private TextView ingredient_name3;
    private TextView ingredient_quantity3;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Recipe.this, Menu.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        //mTextMessage = (TextView) findViewById(R.id.message);
        loadRecipe();
    }
    private void loadRecipe() {

        recipeName = findViewById(R.id.name);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        imageView = findViewById(R.id.image1);

        ingredient_name1 = findViewById(R.id.mt_name1);
        ingredient_quantity1 = findViewById(R.id.mt_quan1);
        ingredient_name2 = findViewById(R.id.mt_name2);
        ingredient_quantity2 = findViewById(R.id.mt_quan2);
        ingredient_name3 = findViewById(R.id.mt_name3);
        ingredient_quantity3 = findViewById(R.id.mt_quan3);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);

        getRecipeData();
        System.out.println("getRecipeData");
    }

    private void getRecipeData() {
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
            String ingredient_names[] = map.get("ingredient_name").split("\n");
            String ingredient_quantitys[] = map.get("ingredient_quantity").split("\n");
            String steps[] = map.get("setp_text").split("\n");

            try {
                Picasso.get().load(picurl).into(imageView);
                recipeName.setText(name);
                ingredient_name1.setText(ingredient_names[0]);
                ingredient_quantity1.setText(ingredient_quantitys[0]);
                ingredient_name2.setText(ingredient_names[1]);
                ingredient_quantity2.setText(ingredient_quantitys[1]);
                ingredient_name3.setText(ingredient_names[2]);
                ingredient_quantity3.setText(ingredient_quantitys[2]);
                step1.setText(steps[0]);
                step2.setText(steps[1]);
                step3.setText(steps[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

}