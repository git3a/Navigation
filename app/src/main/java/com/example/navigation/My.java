package com.example.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class My extends AppCompatActivity {
    private BottomNavigationView navigation;
    private TextView textname;
    private TextView textemail;
    private ImageView imageView;
    private Button logout;

    private String name;
    private String email;
    private boolean lock = true; // thread lock
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(My.this, Menu.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(My.this, Favorite.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(My.this, List.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my:

                    return true;
            }
            return false;
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        logout = findViewById(R.id.logOutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(My.this, MainActivity.class);
                startActivity(intent);
            }
        });
        textemail = findViewById(R.id.email);
        textname = findViewById(R.id.name);
        getUserData();
        textname.setText(name);
        textemail.setText(email);
    }

    private void getUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String susername = sharedPreferences.getString("username", "");
        System.out.println(susername);
        Request.Builder reqBuild = new Request.Builder().get();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.188.105.219/back_end/getuser")
                .newBuilder();
        urlBuilder.addQueryParameter("user", susername);

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
                Map<String,String> map = (Map) JSONObject.parse(m);
                name = map.get("user");
                email = map.get("email");
                lock = false;
            }

        });
        while (lock) {System.out.println("locked");}
    }
}
