package com.example.navigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class My extends AppCompatActivity {
    private BottomNavigationView navigation;
    private TextView textname;
    private TextView textemail;
    private ImageView imageView;
    private Button logout;

    private String name;
    private String email;
    private boolean lock = true; // thread lock
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    private String filePath;
    protected static Uri photoUri;
    private Bitmap mBitmap;
    private String picSavePath = Environment.getExternalStorageDirectory().getPath() + "/ChooseImage";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(My.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(My.this, Favorite.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(My.this, List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
        navigation.setSelectedItemId(R.id.navigation_my);
        logout = findViewById(R.id.logOutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(My.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        textemail = findViewById(R.id.email);
        textname = findViewById(R.id.name);
        getUserData();
        textname.setText(name);
        textemail.setText(email);
        imageView  = findViewById(R.id.image1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
        if (Build.VERSION.SDK_INT >=23)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
    }
    File temp;
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(My.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
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
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
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
            Request request = new Request.Builder().url("http://192.168.1.10:8000/uploadImage/").post(requestBody.build()).build();
            client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    System.out.println(string);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onStart() {
        super.onStart();
        navigation.setSelectedItemId(R.id.navigation_my);
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
        while (lock) {}
    }
}
