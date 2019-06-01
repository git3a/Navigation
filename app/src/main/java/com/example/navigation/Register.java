package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        em = (EditText) findViewById(R.id.email);
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
                //setData(uname, pass1);
                String temp = uname + "$" + pass1;
                //account(temp);
                //String path = "F://user.txt";

                //File writer = new File("F:\\user.txt");
                    try {
                        FileOutputStream fos = new FileOutputStream(new File("F:\\user.txt"));
                        PrintStream ps = new PrintStream(fos);
                        ps.print(temp);
                        ps.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                Intent intent = new Intent();
                intent.setClass(Register.this, MainActivity.class);
                startActivity(intent);
            }

        }
        //getButton.setText("got data");
        //getData();
        //System.out.println("*********************************");
        //String a = "test";
        //System.out.println(a);

        //txv.setText(data);

    }


//        String sql1 = "INSERT INTO users VALUES('"+user+"', '"+password+"')";
//        String getUrl = "http://192.168.1.45:8000/insert";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(getUrl)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String err = e.getMessage().toString();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // final String data = response.body().string();
//                Message msg = handler.obtainMessage();
//                msg.obj = response.body().string();
//                handler.sendMessage(msg);
//            }
//        });

//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            String m = (String) msg.obj;
//            try {
//                er.setText(m);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//    });

}

