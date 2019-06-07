package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Favorite extends AppCompatActivity {
    private BottomNavigationView navigation;
    private TextView recipeName;
    private ImageView imageView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Favorite.this, Menu.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_favorite:
                    intent = new Intent(Favorite.this, Favorite.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    intent = new Intent(Favorite.this, List.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my:
                    intent = new Intent(Favorite.this, My.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadRecipe();
    }
    private void loadRecipe() {
    }
}
