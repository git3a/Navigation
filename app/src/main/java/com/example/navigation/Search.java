package com.example.navigation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Search extends AppCompatActivity{

    EditText mEditSearch;
    TextView mTvSearch;
    TextView mTvTip;
    TextView mhome;
    ListViewForScrollView mListView;
    TextView mTvClear;

    SimpleCursorAdapter adapter;

    SearchSqliteHelper searchSqliteHelper;
    RecordsSqliteHelper recordsSqliteHelper;
    SQLiteDatabase db_search;
    SQLiteDatabase db_records;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        mEditSearch =(EditText)findViewById(R.id.edit_search);
        mTvSearch = (TextView)findViewById(R.id.tv_search);
        mTvTip = (TextView)findViewById(R.id.tv_tip);
        mListView = (ListViewForScrollView)findViewById(R.id.listView);
        mTvClear = (TextView)findViewById(R.id.tv_clear);
        mhome = (TextView)findViewById(R.id.home_buttonPanel);

    }

    private void initData(){
        searchSqliteHelper = new SearchSqliteHelper(this);
        recordsSqliteHelper = new RecordsSqliteHelper(this);

        initializeData();
        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor
                , new String[]{"username", "password"}, new int[]{android.R.id.text1, android.R.id.text2}
                , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(adapter);

    }

    private void deleteData(){
        db_search = searchSqliteHelper.getWritableDatabase();
        db_search.execSQL("delete from table_search");
        db_search.close();

    }
    private void initializeData() {
        deleteData();
        db_search = searchSqliteHelper.getWritableDatabase();
        for (int i = 0; i < 20; i++) {
            db_search.execSQL("insert into table_search values(null,?,?)",
                    new String[]{"name" + i + 10, "pass" + i + "word"});
        }
        db_search.close();
    }

    /**
     * 初始化事件监听
     */
    private void initListener() {
        /**
         * 清除历史纪录
         */
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecords();
            }
        });
        /**
         * 搜索按钮保存搜索纪录，隐藏软键盘
         */
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mEditSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //保存搜索记录
                insertRecords(mEditSearch.getText().toString().trim());

            }
        });
        /**
         * EditText对键盘搜索按钮的监听，保存搜索纪录，隐藏软件盘
         */
        // TODO: 2017/8/10 4、搜索及保存搜索纪录
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    //隐藏键盘
                   // ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                   //         .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                     ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                             .hideSoftInputFromWindow(mEditSearch.getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);

                    //保存搜索记录
                    insertRecords(mEditSearch.getText().toString().trim());
                }

                return false;
            }
        });
        /**
         * EditText搜索框对输入值变化的监听，实时搜索
         */
        // TODO: 2017/8/10 3、使用TextWatcher实现对实时搜索
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditSearch.getText().toString().equals("")) {
                    mTvTip.setText("検索履歴");
                    mTvClear.setVisibility(View.VISIBLE);
                    cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
                    refreshListView();
                } else {
                    mTvTip.setText("検索結果");
                    mTvClear.setVisibility(View.GONE);
                    String searchString = mEditSearch.getText().toString();
                    queryData(searchString);
                }
            }
        });

        /**
         * ListView的item点击事件
         */
        // TODO: 2017/8/10 5、listview的点击 做你自己的业务逻辑 保存搜索纪录
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String username = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                String password = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
                Log.e("Skylark ", username + "---" + password);
                // TODO: 2017/8/10 做自己的业务逻辑

            }
        });

    }

    /**
     * 保存搜索纪录
     *
     * @param username
     */
    private void insertRecords(String username) {
        if (!hasDataRecords(username)) {
            db_records = recordsSqliteHelper.getWritableDatabase();
            db_records.execSQL("insert into table_records values(null,?,?)", new String[]{username, ""});
            db_records.close();
        }
    }

    /**
     * 检查是否已经存在此搜索纪录
     *
     * @param records
     * @return
     */
    private boolean hasDataRecords(String records) {

        cursor = recordsSqliteHelper.getReadableDatabase()
                .rawQuery("select _id,username from table_records where username = ?"
                        , new String[]{records});

        return cursor.moveToNext();
    }

    /**
     * 搜索数据库中的数据
     *
     * @param searchData
     */
    private void queryData(String searchData) {
        cursor = searchSqliteHelper.getReadableDatabase()
                .rawQuery("select * from table_search where username like '%" + searchData + "%' or password like '%" + searchData + "%'", null);
        refreshListView();
    }

    /**
     * 删除历史纪录
     */
    private void deleteRecords() {
        db_records = recordsSqliteHelper.getWritableDatabase();
        db_records.execSQL("delete from table_records");

        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        if (mEditSearch.getText().toString().equals("")) {
            refreshListView();
        }
    }

    /**
     * 刷新listview
     */
    private void refreshListView() {
        adapter.notifyDataSetChanged();
        adapter.swapCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db_records != null) {
            db_records.close();
        }
        if (db_search != null) {
            db_search.close();
        }
    }
}



//
//import android.content.Intent;
//import android.media.browse.MediaBrowser;
//import android.nfc.Tag;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v7.app.AppCompatActivity;
//
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.SearchView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.alibaba.fastjson.JSONObject;
//
//
//import org.w3c.dom.Text;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class Search extends AppCompatActivity {
//
//    private SearchView mSearchView;
//    private ListView lListView;
//    TextView mEmptyView;
//    ArrayAdapter mAdapter;
//
//    private static final String TAG ="Search";
//
//
//
//    private ArrayList<String> mImageUrls = new ArrayList<>();
//    private ArrayList<String> mNames = new ArrayList<>();
//    private ArrayList<Integer> mIds = new ArrayList<>();
//
//    private List<String> name = new ArrayList<String>();
//    private  List<String> picurl = new ArrayList<String>();
//    private  List<Integer> id = new ArrayList<>();
//
//    private BottomNavigationView navigation;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    Intent intent = new Intent(Search.this, Menu.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.navigation_favorite:
//                    intent = new Intent(Search.this,Search.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.navigation_list:
//                    intent = new Intent(Search.this, com.example.navigation.List.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.navigation_my:
//                    intent = new Intent(Search.this, My.class);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_serach_view, menu);
//
//        MenuItem mSearch = menu.findItem(R.id.action_search);
//
//
//       // lListView = findViewById(R.id.list_view);
//       // mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.my_recipe));
//       // lListView.setAdapter(mAdapter);
//       // lListView.setTextFilterEnabled(true);
//
//        android.support.v7.widget.SearchView lSearchView = (android.support.v7.widget.SearchView) mSearch.getActionView();
//        lSearchView.setQueryHint("レシピを入力してください");
//
//
//
//        lSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // 提交按钮的点击事件
//                Toast.makeText(Search.this,query,Toast.LENGTH_SHORT).show();
//                System.out.println("The confirm information is"+ query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // The callback when input contents changed
//                if(!TextUtils.isEmpty(newText)){
//                    lListView.setFilterText(newText);
//                    Log.i(TAG,"input:"+ newText);
//                }else{
//                    lListView.clearTextFilter();
//                }
//                return false;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//
//
//    }
//
//    private boolean lock = true;
//
//
//    private void getPhotoName() {
//        String getUrl = "http://10.40.20.221:8000/getrecipe";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(getUrl)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String err = e.getMessage();
//                System.out.println(err);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // final String data = response.body().string();
//                System.out.println("onResponse");
//                //Message msg = handler.obtainMessage();
//                //msg.obj = response.body().string();
//                // handler.sendMessage(msg);
//
//                String m = response.body().string();
//                Map<String, java.util.List> map = (Map) JSONObject.parse(m);
//                name = map.get("name");
//                picurl = map.get("image");
//                id = map.get("id");
//                lock = false;
//            }
//        });
//        while (lock) {System.out.println("locked");}
//        System.out.println("UnLock");
//        Iterator it_name = name.iterator();
//        Iterator it_url = picurl.iterator();
//        Iterator it_id = id.iterator();
//        while(it_name.hasNext() && it_url.hasNext()) {
//            String name = (String)it_name.next();
//            String url = (String)it_url.next();
//            Integer id = (Integer)it_id.next();
//
//            mImageUrls.add(url);
//            mNames.add(name);
//            mIds.add(id);
//            lock = true;
//        }
//    }
//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            lock = false;
//            try {
//                String m = (String) msg.obj;
//                Map<String, List<String>> map = (Map)JSONObject.parse(m);
//                name = map.get("name");
//                picurl = map.get("image");
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//    });
//}