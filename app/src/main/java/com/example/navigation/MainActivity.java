package com.example.navigation;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.os.Handler;
import java.io.IOException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import java.io.*;
//import java.util.regex.Pattern;
//import java.io.InputStreamReader;
//import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView err;
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            //button1.setText("got data");
            if (user.isEmpty() || pass.isEmpty()) {
                err.setText("ID or password is empty");
                err.setVisibility(View.VISIBLE);
                return;
            }
            String line = "q 123";
            String[] sArr=line.split(" ");
            String mess = "";
            if(! sArr[0].equals(user)) {
                mess += " ユーザが存在しません ";
            }
            if(!mess.isEmpty()) {
                err.setText(mess);
                err.setVisibility(view.VISIBLE);
                return ;
            }

            if(! sArr[1].equals(pass)) {
                mess += " パスワードが間違った ";
            }
            if(!mess.isEmpty()) {
                err.setText(mess);
                err.setVisibility(view.VISIBLE);
                return ;
            }else{
                mess = "ログイン成功!";
                err.setText(mess);
                err.setVisibility(view.VISIBLE);

                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);

                //return ;
            }


            //err.setText("ログイン中");
            //err.setVisibility(View.INVISIBLE);
            //Toast.makeText("ログイン中", Toast.LENGTH_SHORT).show();
//                File file = null;
//                FileReader fileReader=null;
//                BufferedReader bufferedReader=null;



//                    Thread.sleep(500);
//                    file = new File("F:\\user.txt");
//                    fileReader=new FileReader(file);
//                    StringBuilder result = new StringBuilder();
//                    bufferedReader=new BufferedReader(fileReader);
            //FileInputStream fis = getAssets().open("F:\\user.txt");
            //InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            //BufferedReader br = new BufferedReader();
//                    File dirFile = new File("F:\\user.txt");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dirFile)));
//                    String line = br.readLine();

//                    String lineTxt = null;
//                    String out = "";
//                    while ((lineTxt = bufferedReader.readLine()) != null) {
//                        out += lineTxt + "\r\n";
//                    }
//                    if(bufferedReader!=null){
//                        bufferedReader.close();
//                    }
//                    if(fileReader!=null){
//                        fileReader.close();
//                    }



        }

        //getButton.setText("got data");
        //getData();
        //System.out.println("*********************************");
        //String a = "test";
        //System.out.println(a);

        //txv.setText(data);

    }
    class ReClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        }
    }

//    private void getData(String user, String password) {
//        String temp = user + "$" + password;
//        String path = "F:\\user.txt";
//        String mess = "";
//        try {
//            File file = new File(path);
//            if(file.isFile() && file.exists()) {
//                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
//                BufferedReader br = new BufferedReader(isr);
//                String lineTxt = null;
//                while ((lineTxt = br.readLine()) != null) {
//                }
//                br.close();
//                boolean match1 = Pattern.matches(lineTxt, user);
//                if(!match1) {
//                    mess += " ユーザが存在しません ";
//                }
//                if(!mess.isEmpty()) {
//                    err.setText(mess);
//                    err.setVisibility(view.VISIBLE);
//                    return ;
//                }
//            } else {
//                System.out.println("文件不存在!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    //boolean match2 = Pattern.matches(pattern2, uname);

//        String getUrl = "http://192.168.1.45:8000/get";
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
//               // final String data = response.body().string();
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
//                err.setText(m);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//    });
}
