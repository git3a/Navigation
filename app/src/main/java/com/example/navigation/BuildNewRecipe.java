package com.example.navigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.navigation.My.CHOOSE_PICTURE;
import static com.example.navigation.My.photoUri;


public class BuildNewRecipe extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuildNewRecipe";
    private BottomNavigationView navigation;
    private TextView mhome;
    private Button btn_add_material;
    private Button btn_del_material;
    private Button btn_add_step;
    private Button btn_del_step;
    private Button btn_save_to_db;
    private ArrayList<EditText> editTexts; // material list
    private ArrayList<EditText> editTexts2; // step list
    private ArrayList<EditText> editTexts3; // amount list
    private ArrayList<EditText> editTexts4; // time list
    private ArrayList<EditText> timeTexts; // minute list
    private int i = -1;
    private int j = -1;
    private int l = -1;
    private int k = -1;
    private int m = -1;
    private LinearLayout my_layout;
    private LinearLayout my_layout_step;
    private LinearLayout my_layout_amount;
    private LinearLayout my_layout_time;
    private LinearLayout my_layout_min;
    private EditText editname;
    private String myurl;
    private EditText editText1; // material
    private EditText editText2; // step
    private EditText editText3; // amount
    private EditText editText4; // time
    private EditText time_text; // minute
    private ImageView imageView;




    private String filePath;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri photoUri;
    private Bitmap mBitmap;
    private String picSavePath = Environment.getExternalStorageDirectory().getPath() + "/ChooseImage";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(BuildNewRecipe.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(BuildNewRecipe.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(BuildNewRecipe.this, MyList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my:
                    intent = new Intent(BuildNewRecipe.this, My.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        editTexts = new ArrayList<>();
        editTexts2 = new ArrayList<>();
        editTexts3 = new ArrayList<>();
        editTexts4 = new ArrayList<>();
        timeTexts = new ArrayList<>();
        initView();
        imageView = findViewById(R.id.cuisine_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
        if (Build.VERSION.SDK_INT >=23)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_material:
                //addAmountView();
                addView();
                break;
            case R.id.btn_delete_material:
                if(i>-1){
                    deleteView();
                }else{
                    Toast.makeText(this, "入力済み材料がありません",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_step:
                addStepView();
                break;
            case R.id.btn_delete_step:
                if(j>-1){
                    deleteStepView();
                }else{
                    Toast.makeText(this,"入力済みステップがありません",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ButtonSendRecipe:
                String rname = editname.getText().toString();
                String url = myurl;
                System.out.println(url);
                ArrayList<String> my_materials = new ArrayList<>();
                ArrayList<String> my_steps = new ArrayList<>();
                ArrayList<String> my_amount = new ArrayList<>();
                ArrayList<String> my_time = new ArrayList<>();

                for(int i = 0;i < editTexts.size();i++){
                   // System.out.println(editTexts.get(i).getText().toString());
                    my_materials.add(editTexts.get(i).getText().toString());
                }


                for(int j = 0;j < editTexts2.size(); j++){
                    //System.out.println(editTexts2.get(j).getText().toString());

                    my_steps.add(editTexts2.get(j).getText().toString());
                }

                for (int k=0;k< editTexts3.size();k++){
                    //System.out.print(editTexts3.get(k).getText().toString());
                    my_amount.add(editTexts3.get(k).getText().toString());
                }

                for (int l=0;l< editTexts4.size();l++){
                    //System.out.println(editTexts4.get(l).getText().toString());
                    my_time.add(editTexts4.get(l).getText().toString());
                }

                sendRecipeBack(rname ,url,my_materials, my_amount,my_steps,my_time);
                break;
        }


    }

    private void initView() {
        // navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // dynamics buttons
        btn_add_material = findViewById(R.id.btn_add_material);
        btn_del_material = findViewById(R.id.btn_delete_material);
        btn_add_step = findViewById(R.id.btn_add_step);
        btn_del_step = findViewById(R.id.btn_delete_step);
        btn_save_to_db = findViewById(R.id.ButtonSendRecipe);
        // recipe name input
        editname = findViewById(R.id.EditRecipename);
        // back to homepage
        mhome = (TextView)findViewById(R.id.home_buttonPanel);
        // buttons' listeners
        btn_add_material.setOnClickListener(this);
        btn_del_material.setOnClickListener(this);
        btn_add_step.setOnClickListener(this);
        btn_del_step.setOnClickListener(this);
        btn_save_to_db.setOnClickListener(this);

        //back to homepage
        mhome.setFocusable(false);
        mhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildNewRecipe.this,Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }


    public void addView(){
        my_layout = findViewById(R.id.My_material_layout);
        editText1 = new EditText(this);
        i++;
        Log.d(TAG, "addView:----------"+editText1.toString());
        editText1.setHintTextColor(Color.GRAY);
        editText1.setWidth(300);
        editText1.setHeight(100);
        editText1.setGravity(Gravity.START);
        editText1.setHint("材料"+(i+1));
        editText1.setTop(10);
        editText1.setSingleLine(false);
        editText1.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        editText1.setMovementMethod(LinkMovementMethod.getInstance());
        editTexts.add(i, editText1);
        Log.d(TAG, "addView---------"+i);
        my_layout.addView(editText1);

        my_layout_amount = findViewById(R.id.My_amount_layout);
        editText3 = new EditText(this);
        l++;
        Log.d(TAG,"addAmountView:--------"+editText3.toString());
        editText3.setHintTextColor(Color.GRAY);
        editText3.setWidth(80);
        editText3.setHeight(100);
        editText3.setHint("量"+(l+1));
        editText3.setGravity(Gravity.RIGHT);
        editText3.setTop(10);
        editText3.setSingleLine(true);
        editText3.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        editText3.setMovementMethod(LinkMovementMethod.getInstance());
        editTexts3.add(l, editText3);
        Log.d(TAG, "addAmout-------"+l);
        my_layout_amount.addView(editText3);


    }

    public void deleteView(){
        //delete material view
        EditText editText = editTexts.get(i);
        my_layout.removeView(editText);
        editTexts.remove(i);
        i--;
        Log.d(TAG, "deleteView-----"+i);

        //delete amount view
        EditText editText_amount = editTexts3.get(l);
        my_layout_amount.removeView(editText_amount);
        editTexts3.remove(l);
        l--;
        Log.d(TAG, "deleteAmount----" + l);
    }

    public void addStepView(){
        my_layout_step = findViewById(R.id.My_step_layout);
        editText2 = new EditText(this);
        j++;
        Log.d(TAG, "addMaterialView:----------"+editText2.toString());
        editText2.setHintTextColor(Color.GRAY);
        editText2.setWidth(300);
        editText2.setHeight(100);
        editText2.setHint("ステップ"+(j+1));
        editText2.setTop(10);
        editText2.setSingleLine(true);
        editText2.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        editText2.setMovementMethod(LinkMovementMethod.getInstance());
        editTexts2.add(j, editText2);
        Log.d(TAG, "addView---------"+j);
        my_layout_step.addView(editText2);

        my_layout_time = findViewById(R.id.My_time_layout);
        editText4 = new EditText(this);
        k++;
        Log.d(TAG, "addTimeView:----" + editText4.toString());
        editText4.setHintTextColor(Color.GRAY);
        editText4.setWidth(80);
        editText4.setHeight(100);
        editText4.setHint("時間"+(k+1));
        editText4.setTop(10);
        editText4.setSingleLine(true);
        editText4.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        editText4.setMovementMethod(LinkMovementMethod.getInstance());
        editTexts4.add(k, editText4);
        Log.d(TAG, "addTime-----"+k);
        my_layout_time.addView(editText4);

        my_layout_min = findViewById(R.id.My_minute_layout);
        time_text = new EditText(this);
        m++;
        Log.d(TAG, "addMinView------" + time_text.toString());
        time_text.setHintTextColor(Color.GRAY);
        time_text.setWidth(80);
        time_text.setHeight(100);
        time_text.setText(getString(R.string.txtText));
        //time_text.setTextSize(20);
        time_text.setTop(10);
        time_text.setSingleLine(true);
        time_text.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        time_text.setMovementMethod(LinkMovementMethod.getInstance());
        time_text.setEnabled(false);
        timeTexts.add(m,time_text);
        Log.d(TAG, "addmin-----"+m);
        my_layout_min.addView(time_text);

    }


    public void deleteStepView(){
        EditText editText2 = editTexts2.get(j);
        my_layout_step.removeView(editText2);
        editTexts2.remove(j);
        j--;
        Log.d(TAG, "deleteView-----"+j);

        EditText editText_time = editTexts4.get(k);
        my_layout_time.removeView(editText_time);
        editTexts4.remove(k);
        k--;
        Log.d(TAG, "deleteTime-----"+k);

        TextView min_text = timeTexts.get(m);
        my_layout_min.removeView(min_text);
        timeTexts.remove(m);
        m--;
        Log.d(TAG, "deletemin----"+m);
    }


    File temp;
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuildNewRecipe.this);
        builder.setTitle("select photo");
        String[] items = { "写真選択", "撮影" };
        builder.setNegativeButton("キャンセル", null);
        temp = createPhotoFile(true);
        tempUri = Uri.fromFile(temp);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //openAlbumIntent.addCategory(Intent.CATEGORY_OPENABLE);

                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);

                        File cameraFile = createPhotoFile(false);
                        photoUri = Uri.fromFile(cameraFile);

                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);

                }
            }
        });
        builder.show();
    }
    private File  createPhotoFile(boolean isCrop) {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;


        try {
            if (isCrop)
                image = File.createTempFile(name + "crop", ".jpg", storageDir);
            else
                image = File.createTempFile(name, ".jpg", storageDir);
            filePath = image.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(photoUri); // 对图片进行裁剪处理
                    //imageView.setImageURI(photoUri);
                    //uploadImage();
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    //imageView.setImageURI(data.getData());
                    //uploadImage();
                    break;
                case CROP_SMALL_PICTURE:
                    if (tempUri != null) {
                        setImageToView(); // 让刚才选择裁剪得到的图片显示在界面上
                        System.out.println("uploading image");
                        uploadImage();
                    }

                    break;
            }
        }
    }
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高　
        intent.putExtra("outputX", 1000);
        intent.putExtra("outputY", 1000);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_SMALL_PICTURE);

    }

    protected void setImageToView() {
        imageView.setImageURI(tempUri);
    }
    protected void uploadImage() {
        try {
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

            RequestBody body = RequestBody.create(MediaType.parse("image/*"), temp);
            requestBody.addFormDataPart("file", temp.getName(), body);
            Request request = new Request.Builder().url("http://35.222.222.232/uploadImage/").post(requestBody.build()).build();
            // the url was localhose before
            client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    System.out.println(string);
                    Map<String, String> map = (Map) JSONObject.parse(string);
                    myurl = map.get("url");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean lock = true; // thread lock
    private void sendRecipeBack(String rname, String img_url,ArrayList<String> rmaterial,
                                ArrayList<String> ramount, ArrayList<String> rsteps, ArrayList<String> rtime){
        Request.Builder reqBuild = new Request.Builder().get();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://35.222.222.232/insertRecipe").
                newBuilder();

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        String user_id = sharedPreferences.getString("userid", "");
        // process string for material
        StringBuffer sb_material = new StringBuffer();
        for(String s:rmaterial){
            sb_material.append(s+"\n");
        }
        // process string for amount
        StringBuffer sb_amount = new StringBuffer();
        for(String s:ramount){
            sb_amount.append(s+"\n");
        }
        //process string for rsteps
        StringBuffer sb_step = new StringBuffer();
        for(String s:rsteps){
            sb_step.append(s+"\n");
        }
        //process string for rtime
        StringBuffer sb_time = new StringBuffer();
        for(String s:rtime){
            sb_time.append(s+"\n");
        }

        urlBuilder.addQueryParameter("user_id",user_id);
        urlBuilder.addQueryParameter("name", rname);
        urlBuilder.addQueryParameter("img_url",img_url);
        urlBuilder.addQueryParameter("material", sb_material.toString());
        urlBuilder.addQueryParameter("amount",sb_amount.toString());
        urlBuilder.addQueryParameter("steps", sb_step.toString());
        urlBuilder.addQueryParameter("time", sb_time.toString());



        System.out.println("The saved information --------------");
        System.out.println(user_id);
        System.out.println(rname);
        System.out.println(img_url);
        System.out.println(sb_material.toString());
        System.out.println(sb_amount.toString());
        System.out.println(sb_step.toString());
        System.out.println(sb_time.toString());

        OkHttpClient okHttpClient = new OkHttpClient();
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage();
                System.out.print(err);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Onresponse");

               Intent intent = new Intent();
               intent.setClass(BuildNewRecipe.this, BuildNewRecipe.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               startActivity(intent);
            }
        });
        Toast.makeText(BuildNewRecipe.this,"アプロードした",Toast.LENGTH_LONG).show();
    }

}


