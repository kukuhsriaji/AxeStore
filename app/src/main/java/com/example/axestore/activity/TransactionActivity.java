package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Cart;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.CartService;
import com.example.axestore.service.ConsumenService;
import com.example.axestore.util.EncryptDecryptUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TransactionActivity extends AppCompatActivity {
    EditText tfCheckoutCode;
    Button btVerifyCode;
    BottomNavigationView bottomNavigationView;
    TextView tfDownload, tfDownloadPath;
    Consumen consumen;
    private ConsumenService consumenService;
    int PERMISSION_REQUEST_CODE = 200;
    
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
        tfCheckoutCode = findViewById(R.id.tf_checkout_code);
        btVerifyCode = findViewById(R.id.bt_verify_code);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        tfDownload = findViewById(R.id.tf_download);
        tfDownloadPath = findViewById(R.id.tf_download_path);
    }
    private void initAction(){
        btVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String invoiceStr = new EncryptDecryptUtil().decrypt(getStr(tfCheckoutCode.getText()));

                    if(invoiceStr == null || "".equals(invoiceStr)){
                        Toast.makeText(getApplicationContext(), "Code not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] invoiceArr = invoiceStr.split(Pattern.quote("|"));
                        String idInvoice="", username="";
                        for(int i=0; i<invoiceArr.length; i++){
                            System.out.println("checkoutArr["+i+"] = "+invoiceArr[i]);
                            if(i==0) idInvoice = invoiceArr[i];
                            else if(i==1) username = invoiceArr[i];
                        }

                        if(consumen == null || consumen.getUsername() == null || "".equals(consumen.getUsername())){
                            Toast.makeText(getApplicationContext(), "Please login to submit invoice", Toast.LENGTH_SHORT).show();
                        } else if(!consumen.getUsername().equals(username)){
                            Toast.makeText(getApplicationContext(), "Invoice not valid, this is invoice for another consumen ("+username+")", Toast.LENGTH_SHORT).show();
                        } else {
                            //MASUK VALID TO GENERATE INVOICE
                            String pathDownloaded = prepGeneratePdfInvoice(idInvoice);
                            tfDownload.setVisibility(View.VISIBLE);
                            tfDownloadPath.setVisibility(View.VISIBLE);
                            tfDownloadPath.setText(pathDownloaded);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private String prepGeneratePdfInvoice(String idInvoice){
        if (checkPermission()) {
        } else {
            requestPermission();
        }
        CartService cartService;
        cartService = new CartService(this);
        cartService.deleteZeroCart();
        List<Cart> carts = cartService.getCarts();
        if(carts.size() <= 0){
            Toast.makeText(this, "Your Carts is empty.", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return doGeneratePdfInvoice(idInvoice, carts);
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private String doGeneratePdfInvoice(String idInvoice, List<Cart> carts){
        String pathDownloaded = null;
        String prodNameArr[], priceArray[];
        prodNameArr = getResources().getStringArray(R.array.product_name_arrays);
        priceArray = getResources().getStringArray(R.array.product_price_arrays);

        int pageHeight = 1120; int pagewidth = 592;
        Bitmap bmp, scaledbmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        //paint image
        Paint paint = new Paint();
        canvas.drawBitmap(scaledbmp, 225, 40, paint);
        //text normal
        Paint title = new Paint();
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //text bold
        Paint titleBold = new Paint();
        titleBold.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titleBold.setTextSize(15);
        titleBold.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //text bold ACCENT
        Paint titleAccent = new Paint();
        titleAccent.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titleAccent.setTextSize(15);
        titleAccent.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        //text bold RED
        Paint titleRed = new Paint();
        titleRed.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titleRed.setTextSize(15);
        titleRed.setColor(ContextCompat.getColor(this, R.color.red));

        canvas.drawText("Invoice No - "+idInvoice, 40, 240, titleBold);
        canvas.drawText("Recipient", 40, 260, titleBold);
        canvas.drawText(": "+consumen.getName(), 170, 260, title);
        canvas.drawText("Email", 40, 280, titleBold);
        canvas.drawText(": "+consumen.getEmail(), 170, 280, title);
        canvas.drawText("Phone", 40, 300, titleBold);
        canvas.drawText(": "+consumen.getPhone(), 170, 300, title);
        canvas.drawText("Shipping Address", 40, 320, titleBold);
        canvas.drawText(": "+consumen.getAddress(), 170, 320, title);
        canvas.drawText("__________________________________________________________________________", 40, 360, title);
        canvas.drawText("Product", 40, 375, titleBold);
        canvas.drawText("Count", 335, 375, titleBold);
        canvas.drawText("Price", 400, 375, titleBold);
        canvas.drawText("Subtotal", 480, 375, titleBold);
        canvas.drawText("__________________________________________________________________________", 40, 380, title);
        int currentLine = 400;
        Integer grandTotal = 0, subTotal = 0;
        for(Cart c :carts){
            subTotal = (Integer.valueOf(priceArray[c.getIdProduct()]) * c.getCountItem());
            grandTotal += subTotal;

            canvas.drawText(prodNameArr[c.getIdProduct()]+"", 40, currentLine, title);
            canvas.drawText(c.getCountItem()+"", 350, currentLine, title);
            canvas.drawText(formatCurrency(priceArray[c.getIdProduct()]), 400, currentLine, title);
            canvas.drawText(formatCurrency(String.valueOf(subTotal)), 480, currentLine, title);
            currentLine+=20;
            canvas.drawText("note: "+c.getNote()+"", 40, currentLine, title);
            currentLine+=5;
            canvas.drawText("__________________________________________________________________________", 40, currentLine, title);
            currentLine+=20;
        }
        canvas.drawText("Total : ", 430, currentLine, titleBold);
        canvas.drawText(formatCurrency(String.valueOf(grandTotal)), 480, currentLine, titleBold);
        currentLine+=20;
        canvas.drawText("Status : ", 40, currentLine, title);
        canvas.drawText("LUNAS", 100, currentLine, titleRed);
        canvas.drawText("Thank you for shopping at our store, AXE Store", 40, 1080, titleAccent);

        pdfDocument.finishPage(myPage);

        File file = new File(getExternalCacheDir().getAbsolutePath(), "Invoice-"+consumen.getName().replace(" ","-")+".pdf");
        System.out.println("Saving PDF to "+getExternalCacheDir().getAbsolutePath()+"Invoice-"+consumen.getName().replace(" ","-")+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(TransactionActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            pathDownloaded = getExternalCacheDir().getAbsolutePath()+"Invoice-"+consumen.getName().replace(" ","-")+".pdf";
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
        return pathDownloaded;
    }

    private String formatCurrency(String nominal){
        DecimalFormat dFormat = new DecimalFormat("####,###,###");
        return dFormat.format(Integer.valueOf(nominal));
    }


}