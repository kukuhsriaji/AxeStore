package com.example.axestore.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.axestore.model.Cart;
import com.example.axestore.model.Consumen;

import java.util.ArrayList;
import java.util.List;

//import com.project.civillian.model.Civil;

/**
 * Created by Shoozay on 12/23/2019.
 */

public class SqlLiteUtil extends SQLiteOpenHelper {
    //Dekalarasi variabel
    public static final String DATABASE_NAME = "axe_store";
    public static final String TABLE_CONSUMEN = "consumen";
    public static final String field_email = "email";
    public static final String field_name_consumen = "name";
    public static final String field_gender = "gender";
    public static final String field_address = "address";
    public static final String field_phone = "phone";
    public static final String field_username = "username";
    public static final String field_password = "password";
    public static final String field_is_login = "isLogin";


    public static final String TABLE_CART = "cart";
    public static final String field_id_product = "idProduct";
    public static final String field_count_item = "countItem";
    public static final String field_note = "note";


    Context context = null;

    public SqlLiteUtil(Context context) {
        super(context, DATABASE_NAME.concat(".db"), null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Execute Query untuk membuat tabel
        sqLiteDatabase.execSQL("CREATE table IF NOT EXISTS " + TABLE_CONSUMEN +
                " ("+field_email+" TEXT, " +
                " "+ field_name_consumen +" TEXT, " +
                " "+field_gender+" TEXT, " +
                " "+field_address+" TEXT, " +
                " "+field_phone+" TEXT, " +
                " "+field_username+" TEXT PRIMARY KEY, " +
                " "+field_password+" TEXT, " +
                " "+field_is_login+" TEXT)");

        sqLiteDatabase.execSQL("CREATE table IF NOT EXISTS " + TABLE_CART +
                " ("+field_id_product +" TEXT PRIMARY KEY, " +
                " "+ field_count_item +" TEXT, " +
                " "+field_note+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Execute query untuk menghapus tabel
        db.execSQL("Drop table if exists " + TABLE_CONSUMEN);
        db.execSQL("Drop table if exists " + TABLE_CART);
        onCreate(db);
    }

    //START USING LOCAL SQL LITE
    public Long insertConsumenSqlLite(Consumen c){
        //truncateSqlLite();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field_email, c.getEmail());
        contentValues.put(field_name_consumen, c.getName());
        contentValues.put(field_gender, c.getGender());
        contentValues.put(field_address, c.getAddress());
        contentValues.put(field_phone, c.getPhone());
        contentValues.put(field_username, c.getUsername());
        contentValues.put(field_password, c.getPassword());
        contentValues.put(field_is_login, "0");
        long result = db.insert(TABLE_CONSUMEN, null, contentValues);
        db.close();
        return result;
    }

    public Long insertCartSqlLite(Cart c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field_id_product, c.getIdProduct());
        contentValues.put(field_count_item, c.getCountItem());
        contentValues.put(field_note, c.getNote());
        long result = db.insert(TABLE_CART, null, contentValues);
        db.close();
        return result;
    }

    public Integer updateConsumenSqlLite(Consumen c){
        System.out.println("updateSqlLite consumen");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field_email, c.getEmail());
        contentValues.put(field_name_consumen, c.getName());
        contentValues.put(field_gender, c.getGender());
        contentValues.put(field_address, c.getAddress());
        contentValues.put(field_phone, c.getPhone());
        contentValues.put(field_username, c.getUsername());
        contentValues.put(field_password, c.getPassword());
        contentValues.put(field_is_login, "1");
        Integer rowsUpdated = db.update(TABLE_CONSUMEN, contentValues, field_username.concat(" = ?"), new String[] { c.getUsername() });
        db.close();
        return rowsUpdated;
    }

    public Integer logoutConsumenSqlLite(Consumen c){
        System.out.println("updateSqlLite consumen");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(field_email, c.getEmail());
//        contentValues.put(field_name_consumen, c.getName());
//        contentValues.put(field_gender, c.getGender());
//        contentValues.put(field_address, c.getAddress());
//        contentValues.put(field_phone, c.getPhone());
//        contentValues.put(field_username, c.getUsername());
//        contentValues.put(field_password, c.getPassword());
        contentValues.put(field_is_login, "0");
        Integer rowsUpdated = db.update(TABLE_CONSUMEN, contentValues, field_username.concat(" = ?"), new String[] { c.getUsername() });
        db.close();
        return rowsUpdated;
    }

    public Integer updateCartSqlLite(Cart c){
        System.out.println("updateSqlLite cart");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field_id_product, c.getIdProduct());
        contentValues.put(field_count_item, c.getCountItem());
        contentValues.put(field_note, c.getNote());
        Integer rowsUpdated = db.update(TABLE_CART, contentValues, field_id_product.concat(" = ?"), new String[] { (c.getIdProduct()+"") });
        db.close();
        return rowsUpdated;
    }

    public boolean deleteConsumenSqlLite(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONSUMEN, field_username.concat(" = ?"), new String[] { username });
        db.close();
        return true;
    }

    public int deleteCartSqlLite(String idProduct){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowDeleted = db.delete(TABLE_CART, field_id_product.concat(" = ?"), new String[] { idProduct });
        db.close();
        return rowDeleted;
    }

    public void deleteZeroCartSqlLite(){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowDeleted = db.delete(TABLE_CART, field_count_item.concat(" = ?"), new String[] { "0" });
        db.close();
    }

    public void truncateCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
        db.close();
    }

    public void truncateSqlLite(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONSUMEN);
//        db.execSQL("DELETE FROM " + TABLE_CART);
        db.close();
    }

    public Consumen findConsumenSqlLite(String username, String password){
        Consumen c = new Consumen();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CONSUMEN+" where "+field_username+" = '"+username+"' and "+field_password+" = '"+password+"'", null);
        while (res.moveToNext()) {
            c.setEmail(res.getString(0));
            c.setName(res.getString(1));
            c.setGender(res.getString(2));
            c.setAddress(res.getString(3));
            c.setPhone(res.getString(4));
            c.setUsername(res.getString(5));
            c.setPassword(res.getString(6));
            c.setIsLogin(res.getString(7));
        }
        res.close();
        db.close();
        return c;
    }

    public Consumen findConsumenByUsername(String username){
        System.out.println("masuk findConsumenlSqlLite");
        Consumen c = new Consumen();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CONSUMEN+" where "+field_username+" = '"+username+"'", null);
        while (res.moveToNext()) {
            c.setEmail(res.getString(0));
            c.setName(res.getString(1));
            c.setGender(res.getString(2));
            c.setAddress(res.getString(3));
            c.setPhone(res.getString(4));
            c.setUsername(res.getString(5));
            c.setPassword(res.getString(6));
            c.setIsLogin(res.getString(7));
        }
        res.close();
        db.close();
        System.out.println("end findConsumenlSqlLite");
        return c;
    }

    public Consumen getLoginConsumenlSqlLite(){
        Consumen c = new Consumen();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CONSUMEN+" where "+field_is_login+" = '1'", null);
        while (res.moveToNext()) {
            c.setEmail(res.getString(0));
            c.setName(res.getString(1));
            c.setGender(res.getString(2));
            c.setAddress(res.getString(3));
            c.setPhone(res.getString(4));
            c.setUsername(res.getString(5));
            c.setPassword(res.getString(6));
            c.setIsLogin(res.getString(7));
        }
        res.close();
        db.close();
        return c;
    }

    public List<Cart> getCartsSqlLite(){
        List<Cart> carts = new ArrayList<Cart>();
        Cart c = new Cart();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CART, null);
        while (res.moveToNext()) {
            c = new Cart();
            c.setIdProduct(res.getInt(0));
            c.setCountItem(res.getInt(1));
            c.setNote(res.getString(2));
            carts.add(c);
        }
        res.close();
        db.close();
        return carts;
    }

    public List<Cart> findCarts(String idProduct){
        List<Cart> carts = new ArrayList<Cart>();
        Cart c = new Cart();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_CART+" where "+field_id_product+" = '"+idProduct+"'", null);
        while (res.moveToNext()) {
            c = new Cart();
            c.setIdProduct(res.getInt(0));
            c.setCountItem(res.getInt(1));
            c.setNote(res.getString(2));
            carts.add(c);
        }
        res.close();
        db.close();
        return carts;
    }

    public Integer logoutOtherConsumenSqlLite(Consumen c){
        System.out.println("logoutOtherConsumenSqlLite consumen");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(field_email, c.getEmail());
//        contentValues.put(field_name_consumen, c.getName());
//        contentValues.put(field_gender, c.getGender());
//        contentValues.put(field_address, c.getAddress());
//        contentValues.put(field_phone, c.getPhone());
//        contentValues.put(field_username, c.getUsername());
//        contentValues.put(field_password, c.getPassword());
        contentValues.put(field_is_login, "0");
        Integer rowsUpdated = db.update(TABLE_CONSUMEN, contentValues, field_username.concat(" != ?"), new String[] { c.getUsername() });
        db.close();
        return rowsUpdated;
    }

    public Integer setLoginConsumenSqlLite(Consumen c){
        System.out.println("logoutOtherConsumenSqlLite consumen");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(field_email, c.getEmail());
//        contentValues.put(field_name_consumen, c.getName());
//        contentValues.put(field_gender, c.getGender());
//        contentValues.put(field_address, c.getAddress());
//        contentValues.put(field_phone, c.getPhone());
//        contentValues.put(field_username, c.getUsername());
//        contentValues.put(field_password, c.getPassword());
        contentValues.put(field_is_login, "1");
        Integer rowsUpdated = db.update(TABLE_CONSUMEN, contentValues, field_username.concat(" = ?"), new String[] { c.getUsername() });
        db.close();
        return rowsUpdated;
    }

    public List<Consumen> getAllConsumenSqlLite(){
        List<Consumen> cons = new ArrayList<Consumen>();
        Consumen c = new Consumen();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CONSUMEN, null);
        while (res.moveToNext()) {
            c = new Consumen();
            c.setEmail(res.getString(0));
            c.setName(res.getString(1));
            c.setGender(res.getString(2));
            c.setAddress(res.getString(3));
            c.setPhone(res.getString(4));
            c.setUsername(res.getString(5));
            c.setPassword(res.getString(6));
            c.setIsLogin(res.getString(7));
            cons.add(c);
        }
        res.close();
        db.close();
        return cons;
    }

    //END USING LOCAL SQL LITE



}