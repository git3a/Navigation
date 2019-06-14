package com.example.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.os.Handler;
import java.io.IOException;
import java.util.Map;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import java.io.*;
//import java.util.regex.Pattern;
//import java.io.InputStreamReader;
//import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView err;
    private Button button1;
    private Button button2;

    private String _username = "";
    private String _password = "";

    private SharedPreferences sharedPreferences ;
    private boolean lock = true; // thread lock
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        setContentView(R.layout.activity_login);

        button1 = findViewById(R.id.login);
        button1.setOnClickListener(new MyClick());
        button2 = findViewById(R.id.reg);
        button2.setOnClickListener(new ReClick());
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        err = (TextView) findViewById(R.id.message);
    }

    class MyClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            String user = username.getText().toString();
            String pass = password.getText().toString();
            //System.out.println(user);
            //System.out.println(pass);
            //button1.setText("got data");
            if (user.isEmpty() || pass.isEmpty()) {
                err.setText("ID or password is empty");
                err.setVisibility(View.VISIBLE);
                return;
            }
            getUserData(user);
            if (_username==null) {
                err.setText("ユーザが存在しません");
                err.setVisibility(View.VISIBLE);
                return;
            }
            String mess = "";
            if(!_username.equals(user)) {
                mess += " ユーザが存在しません ";
            }
            if(!mess.isEmpty()) {
                err.setText(mess);
                err.setVisibility(view.VISIBLE);
                return ;
            }

            if(! _password.equals(pass)) {
                mess += " パスワードが間違った ";
            }
            if(!mess.isEmpty()) {
                err.setText(mess);
                err.setVisibility(view.VISIBLE);
                return ;
            }else{
                mess = "ログイン成功!";
                userUpdate();
                err.setText(mess);
                err.setVisibility(view.VISIBLE);

                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);

                //return ;
            }
        }
    }
    class ReClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        }
    }
    private void getUserData(String uname) {
        Request.Builder reqBuild = new Request.Builder().get();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.188.105.219/back_end/getuser")
                .newBuilder();
        urlBuilder.addQueryParameter("user", uname);

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
                _username = map.get("user");
                _password = map.get("pwd");
                lock = false;
            }

        });
        while (lock) {System.out.println("locked");}
    }

    private void userUpdate() {
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String susername = sharedPreferences.getString("username", "");
        String spassword = sharedPreferences.getString("password","");
        if (susername.equals("") && spassword.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", _username);
            editor.putString("password", _password);
            editor.commit();
        }
    }
    private void checkLogin() {
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        String susername = sharedPreferences.getString("username", "");
        String spassword = sharedPreferences.getString("password","");
        if (!susername.equals("") && !spassword.equals("")) {
            Intent intent = new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
        }

    }
}
