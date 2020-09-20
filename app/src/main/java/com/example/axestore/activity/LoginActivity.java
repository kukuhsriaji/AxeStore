package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.ConsumenService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout formLogin, txLogin;
    Consumen consumen;
    private ConsumenService consumenService;
    Button btLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        formLogin = findViewById(R.id.formLogin);
        txLogin = findViewById(R.id.tx_login);
        if(consumen != null && consumen.getUsername() != null){
            formLogin.setVisibility(Button.GONE);
            txLogin.setVisibility(Button.VISIBLE);
        } else {
            formLogin.setVisibility(Button.VISIBLE);
            txLogin.setVisibility(Button.GONE);
        }
        btLogin = findViewById(R.id.bt_login);
    }
    private void initAction(){
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Username not registered, please Register..", Toast.LENGTH_SHORT).show();
            }
        });

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
        Intent intent = new Intent(LoginActivity.this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        LoginActivity.this.finish();
    }

}