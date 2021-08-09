package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.ConsumenService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Consumen consumen;
    private ConsumenService consumenService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        consumenService = new ConsumenService(this);
        consumen = consumenService.getConsumenLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
        initAction();
    }

    private void initComponent(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        if(consumen != null && consumen.getUsername() != null){
            System.out.println("consumen.getUsername() -> "+consumen.getUsername());
        } else {
        }
    }
    private void initAction(){

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                goToActivity(MainActivity.class);
                                break;
                            case R.id.menu_login:
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


    private void goToActivity(Class clazz){
        Intent intent = new Intent(TransactionActivity.this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        TransactionActivity.this.finish();
    }

    private String getStr(Editable text){
        return text == null ? "" : text.toString();
    }

}