package com.example.axestore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.adapter.CartCustomAdapter;
import com.example.axestore.adapter.ListAdapterInterface;
import com.example.axestore.model.Cart;
import com.example.axestore.model.Consumen;
import com.example.axestore.service.CartService;
import com.example.axestore.service.ConsumenService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements ListAdapterInterface {
    ListView lvCartTemplate;
    BottomNavigationView bottomNavigationView;
    CartService cartService;
    ConsumenService consumenService;
    Button btClearCart, btCheckout, btRefreshCart;
    TextView tvTotalPrice;
    List<Cart> carts;
    Consumen consumen;
    String prodNameArr[], priceArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartService = new CartService(this);
        carts = cartService.getCarts();
        System.out.println("CARTS SAAT INI");
        for(Cart c : carts){
            System.out.println(c.toString());
        }
        consumenService = new ConsumenService(this);
        consumen = consumenService.getConsumenLogin();
        prodNameArr = getResources().getStringArray(R.array.product_name_arrays);
        priceArray = getResources().getStringArray(R.array.product_price_arrays);

        initComponent();
        initAction();

        setTotalPrice(carts);
    }

    private void initComponent(){
        lvCartTemplate = (ListView) findViewById(R.id.lv_cart_template);
        CartCustomAdapter cartCustomAdapter = new CartCustomAdapter(getApplicationContext(), this, carts, getResources().obtainTypedArray(R.array.product_img_arrays), prodNameArr,
                getResources().getStringArray(R.array.product_desc_arrays), getResources().getStringArray(R.array.product_price_arrays));
        lvCartTemplate.setAdapter(cartCustomAdapter);

        btClearCart = findViewById(R.id.bt_clear_cart);
        btCheckout = findViewById(R.id.bt_checkout);
        btRefreshCart = findViewById(R.id.bt_refresh_cart);

        tvTotalPrice = findViewById(R.id.tv_total_price);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

    }
    private void initAction(){
        btClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartService.truncateCart();
                finish();
                startActivity(getIntent());
            }
        });
        btRefreshCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartService.deleteZeroCart();
                finish();
                startActivity(getIntent());
            }
        });

        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carts = cartService.getCarts();
                if(consumen == null || consumen.getUsername() == null){
                    Toast.makeText(getApplicationContext(), "Please Register to checkout", Toast.LENGTH_SHORT).show();
                } else if (carts.size()<=0){
                    Toast.makeText(getApplicationContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "https://api.whatsapp.com/send?phone=6281288943177&" +
                            "text=CHECKOUT+AXE+STORE+%0A%0A" +
                            "Username:+"+replaceSpace(consumen.getUsername())+"+%0A" +
                            "Nama:+"+replaceSpace(consumen.getName())+"+%0A" +
                            "Alamat:+"+replaceSpace(consumen.getAddress())+"+%0A%0A";

                    for(Cart c : carts){
                        url = url.concat(replaceSpace(prodNameArr[c.getIdProduct()])+"+-+"+c.getCountItem()+"+ekor+%0A");
                    }
                    url = url.concat("+%0ATOTAL+HARGA:+Rp+"+formatCurrency(Double.valueOf(totalPrice(carts)))+",-+%0A");
                    System.out.println("url -> "+url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
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
                                List<Cart> carts = cartService.getCarts();
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
        Intent intent = new Intent(CartActivity.this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CartActivity.this.finish();
    }

    @Override
    public void setTotalPrice(List<Cart> cartsTemp) {
        Double totalPrice = Double.valueOf(totalPrice(cartsTemp));
        tvTotalPrice.setText("Total Prices:   Rp "+formatCurrency(totalPrice)+",-");
    }

    private String formatCurrency(Double nominal){
        DecimalFormat dFormat = new DecimalFormat("####,###,###");
        return dFormat.format(nominal);
    }

    public Integer totalPrice(List<Cart> carts){
        System.out.println("carts.size() -> "+carts.size());
        Integer totalPrice = 0;

        for(Cart cart : carts){
            totalPrice = totalPrice + (cart.getCountItem() * Integer.valueOf(priceArray[cart.getIdProduct()]) );
        }
        return totalPrice;
    }

    private String replaceSpace(String str){
        if(str!=null){
            return str.replace(" ","+");
        } else {
            return "";
        }
    }
}





