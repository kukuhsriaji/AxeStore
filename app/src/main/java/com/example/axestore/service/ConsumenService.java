package com.example.axestore.service;

import android.content.Context;

import com.example.axestore.model.Consumen;
import com.example.axestore.util.SqlLiteUtil;

public class ConsumenService {
    Context context = null;
    SqlLiteUtil sqlLiteUtil;

    public ConsumenService(Context context){
        this.context = context;
        sqlLiteUtil = new SqlLiteUtil(context);
    }

    public Consumen getConsumenLogin(){
        return sqlLiteUtil.getConsumenlSqlLite();
    }

    public Integer updateConsumen(Consumen consumen){
        return sqlLiteUtil.updateConsumenSqlLite(consumen);
    }

    public Long insertConsumen(Consumen consumen){
        return sqlLiteUtil.insertConsumenSqlLite(consumen);
    }

}
