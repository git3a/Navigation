package com.example.navigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
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
    private ArrayList<EditText> editTexts;
    private ArrayList<EditText> editTexts2;
    private int i = -1;
    private int j = -1;
    private LinearLayout my_layout;
    private LinearLayout my_layout_step;
    private EditText editText1;
    private EditText editText2;
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
        }
    }

    private void initView() {
        //navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //dynamics buttons
        btn_add_material = findViewById(R.id.btn_add_material);
        btn_del_material = findViewById(R.id.btn_delete_material);
        btn_add_step = findViewById(R.id.btn_add_step);
        btn_del_step = findViewById(R.id.btn_delete_step);
        mhome = (TextView)findViewById(R.id.home_buttonPanel);
        btn_add_material.setOnClickListener(this);
        btn_del_material.setOnClickListener(this);
        btn_add_step.setOnClickListener(this);
        btn_del_step.setOnClickListener(this);
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
        //upload images

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showChoosePicDialog();
//            }
//        });
    }


    public void addView(){
        my_layout = findViewById(R.id.My_material_layout);
        editText1 = new EditText(this);
        i++;
        Log.d(TAG, "addView:----------"+editText1.toString());
        editText1.setHintTextColor(Color.GRAY);
        editText1.setWidth(300);
        editText1.setHeight(100);
        editText1.setHint("材料"+(i+1));
        editText1.setTop(10);
        editText1.setSingleLine(true);
        editText1.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        editText1.setMovementMethod(LinkMovementMethod.getInstance());
        editTexts.add(i, editText1);
        Log.d(TAG, "addView---------"+i);
        my_layout.addView(editText1);
    }

    public void deleteView(){
        EditText editText = editTexts.get(i);
        my_layout.removeView(editText);
        editTexts.remove(i);
        i--;
        Log.d(TAG, "deleteView-----"+i);
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
    }

    public void deleteStepView(){
        EditText editText2 = editTexts2.get(j);
        my_layout_step.removeView(editText2);
        editTexts2.remove(j);
        j--;
        Log.d(TAG, "deleteView-----"+j);
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
            Request request = new Request.Builder().url("http://localhost:8000//back_end/uploadImage").post(requestBody.build()).build();
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
    public void sendRecipeBack(View Button){
        final EditText nameField = (EditText)findViewById(R.id.EditRecipename);
        String name = nameField.getText().toString();

        //final EditText materialField = (EditText)findViewById(R.id.Input_material);
        //String material = materialField.getText().toString();

        //final EditText stepField = (EditText)findViewById(R.id.EditRecipeStep);
        //String Step = stepField.getText().toString();

        final Spinner recipeTypeField = (Spinner)findViewById(R.id.recipe_type);
        String recipeType = recipeTypeField.getSelectedItem().toString();

    }

}


