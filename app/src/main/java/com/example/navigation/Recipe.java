package com.example.navigation;

import android.content.SharedPreferences;
import java.util.*;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;
import java.util.List;

import android.view.View;
import android.widget.*;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.alibaba.fastjson.*;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Vibrator;
import android.app.Service;
import android.media.MediaPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class  Recipe extends AppCompatActivity {

    private Integer recipeid;
    private static  final  String TAG = "Recipe Activity";
    //for recipeXML
    private ArrayList<String> mIndex = new ArrayList<>();
    private ArrayList<String> mInner = new ArrayList<>();

    List<RecipetModel> arrayList;

    //    private ImageView imageView;
    private BottomNavigationView navigation;
    private String recipeName;
    private String imageUrl;
    private ArrayList<String> ingredient_names = new ArrayList<>();
    private ArrayList<String> ingredient_quantities = new ArrayList<>();
    private ArrayList<String> steps = new ArrayList<>();
    private ArrayList<Integer> times = new ArrayList<>();

    boolean locked = false;
    //    private TextView time;
//    private Integer i = 10;
//    private Timer timer = null;
//    private TimerTask task = null;
//    private Vibrator vibrator;
//    private MediaPlayer player;
//    ProgressBar progressBar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Recipe.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(Recipe.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(Recipe.this, MyList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my:
                    intent = new Intent(Recipe.this, My.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.d(TAG,"onCreate: started");
        dataset();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_recipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReceiptAdapter adapter = new ReceiptAdapter(arrayList, this, recipeid.toString());

        recyclerView.setAdapter(adapter);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {

        }
    }
    private void dataset(){
        getRecipeData();
        Iterator it_name = ingredient_names.iterator();
        Iterator it_qu = ingredient_quantities.iterator();
        Iterator it_time = times.iterator();
        Iterator it_step = steps.iterator();
        arrayList = new ArrayList<>();
        //recipe imageurl & recipe name holder here
        arrayList.add(new RecipetModel(RecipetModel.IMGNAME_TYPE,imageUrl,recipeName,"材料リスト"));
        //meterial list holder here mind the boolean varible 't' at last
        while(it_name.hasNext() && it_qu.hasNext()) {
            arrayList.add(new RecipetModel(RecipetModel.METAR_TYPE, (String)it_name.next(), (String)it_qu.next(), true));
        }
        //step holder here
        int i = 1;
        while(it_step.hasNext()) {

            String stepnum = String.format("ステップ %d", i);
            i++;
            arrayList.add(new RecipetModel(RecipetModel.STEP_TYPE, stepnum, (String)it_step.next(), (Integer)it_time.next()));
        }
    }

    private void getRecipeData() {
        Request.Builder reqBuild = new Request.Builder().get();
        locked = true;
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.222.222.232/getrecipebyid")
                .newBuilder();
        final Intent intent = getIntent();
        recipeid = intent.getIntExtra("id", 0);
        urlBuilder.addQueryParameter("id", recipeid.toString());

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
                    recipeName = map.get("name");
                    imageUrl = map.get("image");
                    String names[] = map.get("material").split("\n");
                    String quantities[] = map.get("amount").split("\n");
                    String step[] = map.get("step").split("\n");
                    String time[] = map.get("time").split("\n");

                    for (int i = 0; i < names.length; i++) {
                        ingredient_names.add(names[i]);
                    }
                    for (int i = 0; i < quantities.length; i++) {
                        ingredient_quantities.add(quantities[i]);
                    }
                    for (int i = 0; i < step.length; i++) {
                        steps.add(step[i]);
                    }
                    for (int i = 0; i < time.length; i++) {
                        times.add(Integer.parseInt(time[i]));
                    }

                }catch (Exception e) {
                    locked = false;
                    System.out.println(e);
                }
            }
        });
        while(locked) {System.out.println("locked");}
    }



}
