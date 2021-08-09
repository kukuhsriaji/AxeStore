package com.example.axestore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axestore.R;
import com.example.axestore.model.Cart;
import com.example.axestore.service.CartService;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class MainCustomAdapter extends BaseAdapter {
    Context context;
    int prodIdList[];
    TypedArray imgProdList;
    String prodNameList[];
    String prodDescList[];
    String prodPriceList[];
    LayoutInflater inflter;
    CartService cartService;

    public MainCustomAdapter(Context applicationContext, int[] prodIdList, TypedArray imgProdList, String[] prodNameList, String[] prodDescList, String[] prodPriceList) {
        this.context = context;
        this.prodIdList = prodIdList;
        this.imgProdList = imgProdList;
        this.prodNameList = prodNameList;
        this.prodDescList = prodDescList;
        this.prodPriceList = prodPriceList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return prodNameList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_main_list, null);
        ImageView imgProd = view.findViewById(R.id.img_prod);
        imgProd.setImageResource(imgProdList.getResourceId(i, -1));

        TextView tvProdName = view.findViewById(R.id.tv_prod_name);
        tvProdName.setText(prodNameList[i]);

        TextView tvProdDesc = view.findViewById(R.id.tv_prod_desc);
        tvProdDesc.setText(prodDescList[i]);

        TextView tvProdPrice = view.findViewById(R.id.tv_prod_price);
        Double amount = Double.valueOf(prodPriceList[i]);
        StringBuilder amountStr = new StringBuilder();
        Formatter formatter = new Formatter(amountStr, Locale.ROOT);
        formatter.format("Rp.%(,.2f,-", amount);
        tvProdPrice.setText(amountStr);

        cartService = new CartService(view.getContext());
        Button btAddCart = view.findViewById(R.id.bt_add_to_cart);
        System.out.println("--> "+prodIdList[i]);
        final String idProduct = String.valueOf(prodIdList[i]);
        final String nameProduct= prodNameList[i];
        final Integer[] isExist = new Integer[1];
        final View viewFinal= view;
        btAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Cart> cartList = cartService.findCarts(idProduct);
                if(cartList.isEmpty()){
                    cartService.insertCart(new Cart(Integer.valueOf(idProduct)));
                    System.out.println("Success add to cart");
                    isExist[0]=0;
                    Toast.makeText(viewFinal.getContext().getApplicationContext(), nameProduct+" success add to cart", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Already in cart");
                    isExist[0]=1;
                    Toast.makeText(viewFinal.getContext().getApplicationContext(), nameProduct+" already in cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        if(isExist[0]==1){
//            Toast.makeText(view.getContext().getApplicationContext(), prodNameList[i]+" already in cart", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(view.getContext().getApplicationContext(), prodNameList[i]+" success add to cart", Toast.LENGTH_SHORT).show();
//        }

        return view;
    }


}
