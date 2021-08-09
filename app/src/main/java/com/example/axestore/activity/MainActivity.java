package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.example.axestore.R;
import com.example.axestore.adapter.MainCustomAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewFlipper v_flipper;
    ListView lvMainTemplate;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
        initAction();
    }

    private void initComponent(){
        int images[] = {R.drawable.koi1, R.drawable.mas_koki1, R.drawable.molly_marble1};
        v_flipper = findViewById(R.id.v_flipper);
        for (int image: images){
            flipperImages(image);
        }

        lvMainTemplate = (ListView) findViewById(R.id.lv_main_template);
        MainCustomAdapter mainCustomAdapter = new MainCustomAdapter(this, getResources().getIntArray(R.array.product_id), getResources().obtainTypedArray(R.array.product_img_arrays), getResources().getStringArray(R.array.product_name_arrays),
                getResources().getStringArray(R.array.product_desc_arrays), getResources().getStringArray(R.array.product_price_arrays));
        lvMainTemplate.setAdapter(mainCustomAdapter);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    private void initAction(){

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                break;
                            case R.id.menu_login:
                                goToActivity(TransactionActivity.class);
                                break;
                            case R.id.menu_cart:
                                goToActivity(CartActivity.class);
                                break;
                            case R.id.menu_profile:
                                goToActivity(ProfileActivity.class);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(3000);
        v_flipper.setAutoStart(true);

        //animation

        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
        }

        private void goToActivity(Class clazz){
            Intent intent = new Intent(MainActivity.this, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        }

}