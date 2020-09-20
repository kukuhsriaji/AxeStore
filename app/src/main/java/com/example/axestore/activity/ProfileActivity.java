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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.CartService;
import com.example.axestore.service.ConsumenService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btRegister, btEdit;
    EditText tfEmail, tfName, tfAddress, tfPhone, tfUsername, tfPassword;
    Spinner cbGender;
    Consumen consumen;
    private ConsumenService consumenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        consumenService = new ConsumenService(this);
        consumen = consumenService.getConsumenLogin();
        initComponent();
        initAction();
        if(consumen != null && consumen.getUsername() != null) initConsumenLogin();
    }

    private void initComponent(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        btRegister = findViewById(R.id.bt_register);
        btEdit = findViewById(R.id.bt_edit);

        if(consumen != null && consumen.getUsername() != null){
            btRegister.setVisibility(Button.GONE);
            btEdit.setVisibility(Button.VISIBLE);
        } else {
            btRegister.setVisibility(Button.VISIBLE);
            btEdit.setVisibility(Button.GONE);
        }

        tfEmail = findViewById(R.id.tf_email);
        tfName = findViewById(R.id.tf_name);
        cbGender = findViewById(R.id.cb_gender);
        tfAddress = findViewById(R.id.tf_address);
        tfPhone = findViewById(R.id.tf_phone);
        tfUsername = findViewById(R.id.tf_username);
        tfPassword = findViewById(R.id.tf_password);
    }

    private void initAction(){
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumen = getComponentValue();
                consumenService.insertConsumen(consumen);
                Toast.makeText(getApplicationContext(), "Success Register", Toast.LENGTH_SHORT).show();
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumen = getComponentValue();
                consumenService.updateConsumen(consumen);
                Toast.makeText(getApplicationContext(), "Success edit account", Toast.LENGTH_SHORT).show();
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
                                goToActivity(LoginActivity.class);
                                break;
                            case R.id.menu_cart:
                                goToActivity(CartActivity.class);
                                break;
                            case R.id.menu_profile:
                                break;
                        }
                        return true;
                    }
                });

    }

    private Consumen getComponentValue() {
        Consumen c = new Consumen();
        c.setEmail(getStr(tfEmail.getText()));
        c.setName(getStr(tfName.getText()));
        if (cbGender != null && cbGender.getSelectedItemPosition() != -1) {
            if (cbGender.getSelectedItemPosition() == 0) c.setGender("L");
            else if (cbGender.getSelectedItemPosition() == 1) c.setGender("P");
        }
        c.setAddress(getStr(tfAddress.getText()));
        c.setPhone(getStr(tfPhone.getText()));
        c.setUsername(getStr(tfUsername.getText()));
        c.setPassword(getStr(tfPassword.getText()));

        return c;
    }

    private void initConsumenLogin(){
        System.out.println(consumen.toString());
        System.out.println("---> "+getStr(consumen.getEmail()));
        tfEmail.setText(getStr(consumen.getEmail()));
        tfName.setText(getStr(consumen.getName()));
        tfAddress.setText(getStr(consumen.getAddress()));
        tfPhone.setText(getStr(consumen.getPhone()));
        tfUsername.setText(getStr(consumen.getUsername()));
        tfPassword.setText(getStr(consumen.getPassword()));
        if (consumen.getGender() != null) {
            if("L".equals(consumen.getGender())) cbGender.setSelection(0);
            else if("P".equals(consumen.getGender())) cbGender.setSelection(1);
        }
    }

    private void goToActivity(Class clazz){
        Intent intent = new Intent(ProfileActivity.this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ProfileActivity.this.finish();
    }

    private String getStr(Editable text){
        return text == null ? "" : text.toString();
    }

    private String getStr(String str){
        return str == null ? "" : str;
    }

}