package com.example.navigation;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity{

    EditText mEditSearch;
    TextView mTvSearch;
    TextView mTvTip;
    TextView mhome;
    ListViewForScrollView mListView;
    List list;

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
        mhome.setFocusable(false);
        mhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this,Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

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
                    new String[]{"料理" + i + 10, "項目" + i + "名称"});
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


                String searchWord = mEditSearch.getText().toString().trim();
                insertRecords(searchWord);

                insertRecords(mEditSearch.getText().toString().trim());
                Intent intent = new Intent(Search.this, SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("searchWord", searchWord);
                startActivity(intent);

            }
        });



        /**
         * EditText对键盘搜索按钮的监听，保存搜索纪录，隐藏软件盘
         */
        // 搜索及保存搜索纪录
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
                    String searchWord = mEditSearch.getText().toString().trim();
                    insertRecords(searchWord);

                    Intent intent = new Intent(Search.this, SearchResult.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("searchWord", searchWord);
                    startActivity(intent);
                }

                return false;
            }
        });
        /**
         * EditText搜索框对输入值变化的监听，实时搜索
         */
        // 使用TextWatcher实现对实时搜索
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

        mhome.setClickable(true);
        //mhome.setOnClickListener(new View.OnClickListener());

        /**
         * ListView的item点击事件
         */
        // listview的点击 做你自己的业务逻辑 保存搜索纪录
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String username = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                String password = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
                Log.e("Skylark ", username + "---" + password);
                //String searchWord = adapterView.getItemAtPosition(position).toString();
                String searchWord = username;
                insertRecords(searchWord);
                Intent intent = new Intent(Search.this, SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("searchWord", searchWord);
                startActivity(intent);

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
