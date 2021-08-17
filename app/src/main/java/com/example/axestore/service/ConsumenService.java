package com.example.axestore.service;

import android.content.Context;

import com.example.axestore.model.Consumen;
import com.example.axestore.util.SqlLiteUtil;

import java.util.List;

public class ConsumenService {
    Context context = null;
    SqlLiteUtil sqlLiteUtil;

    public ConsumenService(Context context){
        this.context = context;
        sqlLiteUtil = new SqlLiteUtil(context);
    }

    public Consumen doLogin(String username, String password){
        Consumen consumen = sqlLiteUtil.findConsumenSqlLite(username, password);
        if(consumen != null && consumen.getUsername() != null && !"".equals(consumen.getUsername())){
            sqlLiteUtil.setLoginConsumenSqlLite(consumen);
            sqlLiteUtil.logoutOtherConsumenSqlLite(consumen);
            System.out.println("return not null -> "+consumen.getUsername());
            return consumen;
        } else {
            System.out.println("return null");
            return null;
        }
    }

    public Consumen getConsumenLogin(){
        return sqlLiteUtil.getLoginConsumenlSqlLite();
    }

    public List<Consumen> getAllConsumen(){
        return sqlLiteUtil.getAllConsumenSqlLite();
    }

    public Integer updateConsumen(Consumen consumen){
        return sqlLiteUtil.updateConsumenSqlLite(consumen);
    }

    public Long insertConsumen(Consumen consumen){
        return sqlLiteUtil.insertConsumenSqlLite(consumen);
    }

    public Boolean deleteConsumen(String username){
        return sqlLiteUtil.deleteConsumenSqlLite(username);
    }

    public Integer logoutConsumen(Consumen consumen){
        return sqlLiteUtil.logoutConsumenSqlLite(consumen);
    }

    public Consumen findConsumenByUsername(String username){
        return sqlLiteUtil.findConsumenByUsername(username);
    }

}
