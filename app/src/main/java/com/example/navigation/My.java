package com.example.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class My extends AppCompatActivity {
    private BottomNavigationView navigation;
    private TextView recipeName;
    private ImageView imageView;
    private Button logout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(My.this, Menu.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(My.this, Favorite.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(My.this, List.class);
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
        logout = findViewById(R.id.logOutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(My.this, MainActivity.class);
                startActivity(intent);
            }
        });
        loadRecipe();
    }

    private void loadRecipe() {
    }
}
