package com.example.axestore.service;

import android.content.Context;

import com.example.axestore.model.Cart;
import com.example.axestore.util.SqlLiteUtil;

import java.util.List;

public class CartService {

    Context context = null;
    SqlLiteUtil sqlLiteUtil;

    public CartService(Context context){
        this.context = context;
        sqlLiteUtil = new SqlLiteUtil(context);
    }

    public List<Cart> getCarts(){
        return sqlLiteUtil.getCartsSqlLite();
    }

    public Integer updateCart(Cart cart){
        return sqlLiteUtil.updateCartSqlLite(cart);
    }

    public Long insertCart(Cart cart){
        return sqlLiteUtil.insertCartSqlLite(cart);
    }

    public int deleteCart(String idProduct){
        return sqlLiteUtil.deleteCartSqlLite(idProduct);
    }

    public void deleteZeroCart(){
        System.out.println("deleteZeroCart called");
        sqlLiteUtil.deleteZeroCartSqlLite();
        System.out.println("deleteZeroCart end called");
    }

    public void truncateCart(){
        sqlLiteUtil.truncateCart();
    }

    public List<Cart> findCarts(String idProduct){
        return sqlLiteUtil.findCarts(idProduct);
    }

}
