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
import com.example.axestore.R;
import com.example.axestore.model.Cart;
import com.example.axestore.service.CartService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartCustomAdapter extends BaseAdapter {
    Context mContext;
    List<Cart> carts = new ArrayList<>();
    TypedArray imgProdList;
    String prodNameList[];
    String prodDescList[];
    String prodPriceList[];
    LayoutInflater inflter;
    CartService cartService;
    private ListAdapterInterface listAdapterInterface;

    public CartCustomAdapter(Context applicationContext, ListAdapterInterface listAdapterInterface, List<Cart> carts, TypedArray imgProdList, String[] prodNameList, String[] prodDescList, String[] prodPriceList) {
        this.mContext = mContext;
        this.listAdapterInterface = listAdapterInterface;
        this.carts = carts;
        this.imgProdList = imgProdList;
        this.prodNameList = prodNameList;
        this.prodDescList = prodDescList;
        this.prodPriceList = prodPriceList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return carts.size();
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
        view = inflter.inflate(R.layout.activity_cart_list, null);
        ImageView imgProd = view.findViewById(R.id.img_prod);
        Cart cart = carts.get(i);
        final Integer idProduct = cart.getIdProduct();

        imgProd.setImageResource(imgProdList.getResourceId(cart.getIdProduct(), -1));

        TextView tvProdName = view.findViewById(R.id.tv_prod_name);
        tvProdName.setText(prodNameList[cart.getIdProduct()]);

        TextView tvProdDesc = view.findViewById(R.id.tv_prod_desc);
        tvProdDesc.setText(prodDescList[cart.getIdProduct()]);

        TextView tvProdPrice = view.findViewById(R.id.tv_prod_price);
        Double amount = Double.valueOf(prodPriceList[cart.getIdProduct()]);
        tvProdPrice.setText("Rp "+formatCurrency(amount)+",-");

        Button btMinProd = view.findViewById(R.id.bt_min_prod);
        Button btPlusProd = view.findViewById(R.id.bt_plus_prod);
        final TextView tvCountProdFinal = view.findViewById(R.id.tf_count_prod);
        tvCountProdFinal.setText(String.valueOf(cart.getCountItem()));

        btMinProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(String.valueOf(tvCountProdFinal.getText()));
                System.out.println("count min = "+count);
                if(count > 0) tvCountProdFinal.setText(String.valueOf((count-1)));
            }
        });

        btPlusProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(String.valueOf(tvCountProdFinal.getText()));
                System.out.println("count plus = "+count);
                tvCountProdFinal.setText(String.valueOf((count+1)));
            }
        });

        final EditText tfNoteProd = view.findViewById(R.id.tf_note_prod);
        tfNoteProd.setText(cart.getNote());


        final View viewFinal= view;
        cartService = new CartService(view.getContext());
        Button btSaveChanges = view.findViewById(R.id.bt_save_changes);
        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cartTemp = new Cart();
                cartTemp.setIdProduct(idProduct);
                cartTemp.setCountItem(Integer.valueOf(String.valueOf(tvCountProdFinal.getText())));
                cartTemp.setNote(String.valueOf(tfNoteProd.getText()));
                cartService.updateCart(cartTemp);
                listAdapterInterface.setTotalPrice(cartService.getCarts());
            }
        });

        return view;
    }

    private String formatCurrency(Double nominal){
        DecimalFormat dFormat = new DecimalFormat("####,###,###");
        return dFormat.format(nominal);
    }

}
