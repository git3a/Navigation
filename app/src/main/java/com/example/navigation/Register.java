package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.*;

public class Register extends AppCompatActivity {
    private Button b1;
    private Button b2;
    private EditText u1;
    private EditText n1;
    private EditText em;
    private EditText p1;
    private EditText p2;
    private TextView er;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new MyClick());
        u1 = (EditText) findViewById(R.id.username);
        em = (EditText) findViewById(R.id.name);
        p1 = (EditText) findViewById(R.id.password1);
        p2 = (EditText) findViewById(R.id.password2);
        er = (TextView) findViewById(R.id.mess);

    }

    class MyClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //button1.setText("got data");
            String err = er.getText().toString();
            if (err.length() > 0) er.setVisibility(view.INVISIBLE);

            String uname = u1.getText().toString();
            String email = em.getText().toString();
            String pass1 = p1.getText().toString();
            String pass2 = p2.getText().toString();

            String mess = "";
            if (uname.length() < 4 || uname.length() > 10)
                mess += " User ID should have legth in 4-10 ";
            String pattern1 = "^[a-zA-Z][a-zA-Z\\d_]*";
            String pattern2 = ".*[A-Z].*";
            boolean match1 = Pattern.matches(pattern1, uname);
            //boolean match2 = Pattern.matches(pattern2, uname);
            if (!match1) {
                mess += " Username needs to start with an English letter, consisting of English alphanumeric characters and _ ";
            }


            if (!mess.isEmpty()) {
                er.setText(mess);
                er.setVisibility(view.VISIBLE);
                return;
            }

            //检查Email
            if (email.isEmpty()) {
                er.setText(" Email is empty ");
                er.setVisibility(view.VISIBLE);
                return;
            }

            //检查密码
            mess = "";
            if (pass1.length() < 6 || pass2.length() > 12 || pass1.length() > 12 || pass2.length() < 6)
                mess += " Password length needs 6-12 ";
            if (!pass1.equals(pass2)) {
                mess += " Inconsistent password input ";
            }
            String pattern6 = "[a-zA-Z\\d_]*";
            boolean match6 = Pattern.matches(pattern6, pass1);
            if (!match6) {
                mess += " Password should consist of English alphanumeric characters and _  ";
            }
            if (!mess.isEmpty()) {
                er.setText(mess);
                er.setVisibility(view.VISIBLE);
                return;
            } else {
                er.setText(" 新規作成 成功! ");
                er.setVisibility(view.INVISIBLE);

                regist(uname, email, pass1);

                Intent intent = new Intent();
                intent.setClass(Register.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

        }

        private void regist(String uname, String email, String pass) {
            Request.Builder reqBuild = new Request.Builder().get();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.222.222.232/insert")
                   .newBuilder();
            //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.10:8000/insert")
            //        .newBuilder();
            urlBuilder.addQueryParameter("user", uname);
            urlBuilder.addQueryParameter("email", email);
            urlBuilder.addQueryParameter("password", pass);
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
                }
            });
        }
    }
}

