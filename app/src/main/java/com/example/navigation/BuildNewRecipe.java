package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class BuildNewRecipe extends AppCompatActivity {
    private BottomNavigationView navigation;
    ImageButton newbutton;
    private GridView gv_edit_msg;
    private TextView mhome;
    //private ImagePicker imagePicker;


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
        initView();


    }

    private void initView() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        gv_edit_msg = (GridView) findViewById(R.id.gv_edit_msg);
        mhome = (TextView)findViewById(R.id.home_buttonPanel);
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

    public void sendRecipeBack(View Button){
        final EditText nameField = (EditText)findViewById(R.id.EditRecipename);
        String name = nameField.getText().toString();

        final EditText materialField = (EditText)findViewById(R.id.Input_material);
        String material = materialField.getText().toString();

        final EditText stepField = (EditText)findViewById(R.id.EditRecipeStep);
        String Step = stepField.getText().toString();

        final Spinner recipeTypeField = (Spinner)findViewById(R.id.recipe_type);
        String recipeType = recipeTypeField.getSelectedItem().toString();
    }

}


