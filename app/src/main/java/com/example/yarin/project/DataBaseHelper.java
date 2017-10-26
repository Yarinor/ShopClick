package com.example.yarin.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by Yarin on 23/04/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShopClick.db";
    private static final String USERS_TABLE_NAME = "Users";
    private static final String COLUMN_USERNAME = "userName";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_GENDER = "Gender";
    private static final String COLUMN_CITY = "City";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_IMAGE = "image";

    private static final String PRODUCTS_TABLE_NAME="products";
    private static final String COLUMN_PRODUCTNAME="productName";
    private static final String COLUMN_PRODUCTPRICE="productPrice";
    private static final String COLUMN_PRODUCTAMOUNT="productAmount";
    private static final String COLUMN_PRODUCTOWNER="owner";
    private static final String COLUMN_INTIALPRICE="intialPrice";

    private static final String CARTS_TABLE_NAME="carts";
    private static final String COLUMN_CARTOWNER="owner";
    private static final String COLUMN_CARTTOTALPRICE="totalPrice";
    private static final String COLUMN_CARTTOTALAMOUNT="totalAmount";


    SQLiteDatabase db;
    private static final String USERS_TABLE_CREATE ="create table Users(userName text primary key not null," +
            "Password text not null,Email text not null,Gender text not null,City text not null," +
            "address text not null,image blob not null)";

    private static final String PRODUCTS_TABLE_CREATE="create table "+PRODUCTS_TABLE_NAME+"(id int productId(1,1) primary key,"+COLUMN_PRODUCTNAME+" text" +
            "not null,"+COLUMN_PRODUCTPRICE+" text not null,"+COLUMN_PRODUCTAMOUNT+" text not null,"+COLUMN_PRODUCTOWNER+" text not null,"+COLUMN_INTIALPRICE+" text not null)";//("+COLUMN_PRODUCTOWNER+") text references Users("+COLUMN_USERNAME+"))";

    private static final String CARTS_TABLE_CREATE="create table "+CARTS_TABLE_NAME+"("+COLUMN_CARTOWNER+" text primary key not null,"+COLUMN_CARTTOTALAMOUNT+" text not null,"
            +COLUMN_CARTTOTALPRICE+" text not null)";




    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE_CREATE);
        db.execSQL(PRODUCTS_TABLE_CREATE);
        db.execSQL(CARTS_TABLE_CREATE);
        this.db=db;
    }

    public void insertUser(User u){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,u.getUserName());
        values.put(COLUMN_PASSWORD,u.getPassword());
        values.put(COLUMN_EMAIL,u.getEmail());
        values.put(COLUMN_GENDER,u.getGender());
        values.put(COLUMN_CITY,u.getCity());
        values.put(COLUMN_ADDRESS,u.getAddress());
        values.put(COLUMN_IMAGE,u.getProfilePicture());
        db.insert(USERS_TABLE_NAME,null,values);
        db.close();
    }
    public void insertProduct(Product p){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCTNAME, p.getProductName());
            values.put(COLUMN_PRODUCTPRICE, p.getPrice());
            values.put(COLUMN_PRODUCTAMOUNT, p.getAmount());
            values.put(COLUMN_PRODUCTOWNER, p.getOwner());
            values.put(COLUMN_INTIALPRICE,p.getIntialPrice());
            db.insert(PRODUCTS_TABLE_NAME, null, values);
            db.close();
        }
    public void insertCart(Cart cart){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARTOWNER, cart.getOwner());
        values.put(COLUMN_CARTTOTALPRICE, cart.getTotalPrice());
        values.put(COLUMN_CARTTOTALAMOUNT, cart.getTotalAmount());
        db.insert(CARTS_TABLE_NAME, null, values);
        db.close();
    }

    public Bitmap getImage(String username){
        db = this.getReadableDatabase();
        String qu = "select image from " + USERS_TABLE_NAME + " where userName like '%" + username +"%'";
        Cursor cur = db.rawQuery(qu,null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }

    public String getPassword(String userName){
        db = this.getReadableDatabase();
        String sql="Select " +COLUMN_PASSWORD+ " from " +USERS_TABLE_NAME +" where " +COLUMN_USERNAME+ " = '" +userName+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String password = cursor.getString(0);
            cursor.close();
            db.close();
            return password;
        } else {
            return null;
        }
    }
    public void updatePassword(String userName, String newPassword){
        db=this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, newPassword);
        db.update(USERS_TABLE_NAME, cv, COLUMN_USERNAME + " = '" + userName + "'", null);
        db.close();
    }
    public void updateImage(Bitmap profileImage, String userName){
        db=this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        byte[] convertedBitmapProfileImage=getBitmapAsByteArray(profileImage);
        cv.put(COLUMN_IMAGE,convertedBitmapProfileImage );
        db.update(USERS_TABLE_NAME, cv, COLUMN_USERNAME + " = '" + userName + "'", null);
        db.close();
    }

    public String getProductAmount(String productName,String owner) {
        db = this.getReadableDatabase();
        String sql = "Select " + COLUMN_PRODUCTAMOUNT + " from " + PRODUCTS_TABLE_NAME + " where " + COLUMN_PRODUCTNAME + " = '" + productName + "'" + " and " + COLUMN_PRODUCTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String productAmount = cursor.getString(0);
            cursor.close();
            db.close();
            return productAmount;
        } else {
            return null;
        }
    }
    public String getCartTotalAmount(String owner) {
        db = this.getReadableDatabase();
        String sql = "Select " + COLUMN_CARTTOTALAMOUNT + " from " + CARTS_TABLE_NAME + " where " + COLUMN_PRODUCTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String cartTotalAmount = cursor.getString(0);
            cursor.close();
            db.close();
            return cartTotalAmount;
        } else {
            return null;
        }
    }


    public String getProductIntialPrice(String productName){
        db = this.getReadableDatabase();
        String sql="Select " +COLUMN_INTIALPRICE+ " from " +PRODUCTS_TABLE_NAME+" where " + COLUMN_PRODUCTNAME + " = '" + productName + "'";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String intialPrice = cursor.getString(0);
            cursor.close();
            db.close();
            return intialPrice;
        }
        else{
           return  null;
        }

    }

    public String getProductCurrentPrice(String productName,String owner){
        db = this.getReadableDatabase();
        String sql="Select " +COLUMN_PRODUCTPRICE+ " from " +PRODUCTS_TABLE_NAME+" where " + COLUMN_PRODUCTNAME + " = '" + productName + "'"+" and "+COLUMN_PRODUCTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String currentPrice = cursor.getString(0);
            cursor.close();
            db.close();
            return currentPrice;
        }
        else{
            return  null;
        }

    }
    public String getCartTotalPrice(String owner) {
        db = this.getReadableDatabase();
        String sql = "Select " + COLUMN_CARTTOTALPRICE + " from " + CARTS_TABLE_NAME + " where " + COLUMN_PRODUCTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String cartTotalPrice = cursor.getString(0);
            cursor.close();
            db.close();
            return cartTotalPrice;
        } else {
            return null;
        }
    }

    public String searchPass(String username){
        db = this.getReadableDatabase();
        String query = "select userName,Password from "+ USERS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b = "not found";

        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);

                if (a.equals(username)){
                    b=cursor.getString(1);
                    break;
                }


            }
            while(cursor.moveToNext());
            }
                return b;
        }
    public void updateProductAmount(String productName,int currentValue, int addValue,String owner ) {
        db = this.getReadableDatabase();
        int newValue = currentValue + addValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCTAMOUNT, newValue);
        db.update(PRODUCTS_TABLE_NAME, cv, COLUMN_PRODUCTNAME + " = '" + productName + "'" + " and " + COLUMN_PRODUCTOWNER + " = '" + owner + "'", null);
        db.close();
    }
    public void updateTotalProductAmount(int currentValue, int addValue,String owner ){
        db = this.getReadableDatabase();
        int newValue = currentValue + addValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARTTOTALAMOUNT,newValue);
        db.update(CARTS_TABLE_NAME,cv,COLUMN_PRODUCTOWNER + " = '" + owner + "'",null);
        db.close();

    }
    public void restTotalProductAmount(int currentValue,String owner ){
        db = this.getReadableDatabase();
        int newValue = currentValue-currentValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARTTOTALAMOUNT,newValue);
        db.update(CARTS_TABLE_NAME,cv,COLUMN_PRODUCTOWNER + " = '" + owner + "'",null);
        db.close();

    }

    public void updateProductPrice(String productName,int currentValue, int addValue,String owner ){
        db = this.getReadableDatabase();
        int newValue = currentValue + addValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCTPRICE,newValue);
        db.update(PRODUCTS_TABLE_NAME,cv,COLUMN_PRODUCTNAME + " = '" + productName + "'"+" and " + COLUMN_PRODUCTOWNER + " = '" + owner + "'",null);
        db.close();

    }
    public void updateTotalProductPrice(int currentValue, int addValue,String owner ){
        db = this.getReadableDatabase();
        int newValue = currentValue + addValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARTTOTALPRICE,newValue);
        db.update(CARTS_TABLE_NAME,cv,COLUMN_PRODUCTOWNER + " = '" + owner + "'",null);
        db.close();

    }
    public void restTotalProductPrice(int currentValue,String owner ){
        db = this.getReadableDatabase();
        int newValue = currentValue-currentValue;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARTTOTALPRICE,newValue);
        db.update(CARTS_TABLE_NAME,cv,COLUMN_PRODUCTOWNER + " = '" + owner + "'",null);
        db.close();

    }

    public void RemoveProduct(String productName,String owner){
        db = this.getWritableDatabase();
        db.delete(PRODUCTS_TABLE_NAME, COLUMN_PRODUCTNAME + " = '" + productName + "'" + " and " + COLUMN_PRODUCTOWNER + " = '" + owner + "'", null);
    }




    public ArrayList<Product> getProducts(String userName){
        db = this.getReadableDatabase();
        String sql = "Select " + "p."+COLUMN_PRODUCTNAME+", " +"p." +COLUMN_PRODUCTPRICE+
                ", " +"P." + COLUMN_PRODUCTAMOUNT + ", " + "P." +COLUMN_PRODUCTOWNER+ ", " +"P." +COLUMN_INTIALPRICE+ "  FROM "+PRODUCTS_TABLE_NAME+" P"
                +" join " +USERS_TABLE_NAME + " u " + "on p." +COLUMN_PRODUCTOWNER + " = u." +COLUMN_USERNAME +
                " where p." + COLUMN_PRODUCTOWNER +" like '%" + userName +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Product> allProducts = new ArrayList<Product>();
                       if(cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String productName=cursor.getString(0);
                        String productPrice=cursor.getString(1);
                        String productAmount=cursor.getString(2);
                        String productOwner=cursor.getString(3);
                        String intialPrice=cursor.getString(4);

                        Product product = new Product();
                        product.setProductName(productName);
                        product.setPrice(productPrice);
                        product.setAmount(productAmount);
                        product.setOwner(productOwner);
                        product.setIntialPrice(intialPrice);

                        allProducts.add(product);

                cursor.moveToNext();

            }
        }

        cursor.close();
        return  allProducts;

    }
    public boolean isUserExists(String userName) {
        db = this.getReadableDatabase();
        String Query = "Select * from " + USERS_TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public boolean isProductExists(String productName, String owner) {
        db = this.getReadableDatabase();
        String Query = "Select * from " + PRODUCTS_TABLE_NAME + " where " + COLUMN_PRODUCTNAME + " = '" + productName + "' and " + COLUMN_PRODUCTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public boolean isCartExists(String owner) {
        db = this.getReadableDatabase();
        String Query = "Select * from " + CARTS_TABLE_NAME + " where " + COLUMN_CARTOWNER + " = '" + owner + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+ USERS_TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);


    }





}
