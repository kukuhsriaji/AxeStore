package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.ConsumenService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.os.StrictMode;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btRegister, btEdit, btLogout, btLogin, btRegisterLogin;
    EditText tfEmail, tfName, tfAddress, tfPhone, tfUsername, tfPassword, tfUsernameLogin,tfPasswordLogin;
    Spinner cbGender;
    Consumen consumen;
    private ConsumenService consumenService;
    LinearLayout formLogin, formProfile;
    String generatedToken="a";
    int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
        btLogout = findViewById(R.id.bt_logout);
        tfEmail = findViewById(R.id.tf_email);
        tfName = findViewById(R.id.tf_name);
        cbGender = findViewById(R.id.cb_gender);
        tfAddress = findViewById(R.id.tf_address);
        tfPhone = findViewById(R.id.tf_phone);
        tfUsername = findViewById(R.id.tf_username);
        tfPassword = findViewById(R.id.tf_password);
        tfUsernameLogin = findViewById(R.id.tf_username_login);
        tfPasswordLogin = findViewById(R.id.tf_password_login);
        btLogin = findViewById(R.id.bt_login);
        formLogin = findViewById(R.id.formLogin);
        formProfile = findViewById(R.id.formProfile);
        btRegisterLogin = findViewById(R.id.bt_register_login);

        if(consumen != null && consumen.getUsername() != null){
            System.out.println("consumen.getUsername() -> "+consumen.getUsername());
            btRegister.setVisibility(Button.GONE);
            btEdit.setVisibility(Button.VISIBLE);
            btLogout.setVisibility(Button.VISIBLE);
            tfUsername.setEnabled(false);
            formLogin.setVisibility(Button.GONE);
            formProfile.setVisibility(Button.VISIBLE);
        } else {
            btRegister.setVisibility(Button.VISIBLE);
            btEdit.setVisibility(Button.GONE);
            btLogout.setVisibility(Button.GONE);
            tfUsername.setEnabled(true);
            formLogin.setVisibility(Button.VISIBLE);
            formProfile.setVisibility(Button.GONE);
        }
    }

    private void initAction(){
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumen = getFormValue();
                consumenService.updateConsumen(consumen);
                Toast.makeText(getApplicationContext(), "Success edit account", Toast.LENGTH_SHORT).show();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Success logout account", Toast.LENGTH_SHORT).show();
                consumenService.logoutConsumen(consumen);
                finish();
                startActivity(getIntent());
//                goToActivity(LoginActivity.class);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Consumen> consumenList = consumenService.getAllConsumen();
                System.out.println("Show all consumen");
                for(Consumen c : consumenList){
                    System.out.println(c.toString());
                }
                Consumen c = consumenService.doLogin(getStr(tfUsernameLogin.getText()), getStr(tfPasswordLogin.getText()));
                if(c != null){
                    Toast.makeText(getApplicationContext(), "Thank you !\nLogin successfuly "+c.getName()+" !", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
//                    goToActivity(MainActivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Username and Password not match !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLogin.setVisibility(Button.GONE);
                formProfile.setVisibility(Button.VISIBLE);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailTokenPopupActivity popupClass = new EmailTokenPopupActivity();
                if (getStr(tfName.getText()).length() == 0){
                    Toast.makeText(getApplicationContext(), "name cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (getStr(tfEmail.getText()).length() == 0) {
                    Toast.makeText(getApplicationContext(), "email cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (getStr(tfAddress.getText()).length() == 0){
                    Toast.makeText(getApplicationContext(), "address cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (getStr(tfPhone.getText()).length() == 0){
                    Toast.makeText(getApplicationContext(), "phone cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (getStr(tfUsername.getText()).length() == 0){
                    Toast.makeText(getApplicationContext(), "username cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (getStr(tfPassword.getText()).length() == 0){
                    Toast.makeText(getApplicationContext(), "password cannot be empty", Toast.LENGTH_SHORT).show();
                } else{
                    Consumen checkConsumen = consumenService.findConsumenByUsername(getStr(tfUsername.getText()));
                    if(checkConsumen != null && checkConsumen.getUsername() != null && !"".equals(checkConsumen.getUsername())){
                        Toast.makeText(getApplicationContext(), "Username "+getStr(tfUsername.getText())+" already exist", Toast.LENGTH_SHORT).show();
                    } else {
                        showPopupInputToken(v);
                    }
                }
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
                                goToActivity(TransactionActivity.class);
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

    private Consumen getFormValue() {
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

    private boolean checkPermissionInternet() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        return permission1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionInternet() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_CODE);
    }

    public void sendTokenPrep(){
        if (checkPermissionInternet()) {
        } else {
            requestPermissionInternet();
        }
        sendTokenEmail();
    }

    private void sendTokenEmail(){
        generatedToken = new SimpleDateFormat("mmssHH").format(new Date());
        String to = getStr(tfEmail.getText());
        final String from = "axestoreofficial@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "Psak123!!");
            }
        });
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("AXESTORE Registration Token");
            message.setText("This is your token: "+generatedToken);
//            message.setContent(
//                    "<h1>"+token+"</h1>",
//                    "text/html");
            Transport.send(message);
        } catch (MessagingException mex) {
            Toast.makeText(getApplicationContext(), "Email: "+mex.getMessage(), Toast.LENGTH_SHORT).show();
            mex.printStackTrace();
        }
    }

    private void showPopupInputToken(final View view){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_email_token_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final EditText tfToken = popupView.findViewById(R.id.tf_token);
        Button btSubmitToken = popupView.findViewById(R.id.bt_submit_token);
        Button btSendToken = popupView.findViewById(R.id.bt_send_token);

        btSubmitToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generatedToken.equals(getStr(tfToken.getText()))){ //VALIDASI TOKEN
                    Toast.makeText(getApplicationContext(), "Token valid !\nThank you for register Axe Store\nPlease Login..", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                    consumen = getFormValue();
                    consumenService.insertConsumen(consumen);
                    formLogin.setVisibility(Button.VISIBLE);
                    formProfile.setVisibility(Button.GONE);
//                    goToActivity(MainActivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Token not valid !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btSendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTokenPrep();
            }
        });

//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //Close the window when clicked
//                popupWindow.dismiss();
//                return true;
//            }
//        });
    }

}