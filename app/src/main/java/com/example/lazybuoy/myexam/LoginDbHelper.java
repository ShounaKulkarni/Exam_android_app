package com.example.lazybuoy.myexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDbHelper extends SQLiteOpenHelper {
    public LoginDbHelper( Context context) {
        super(context, "login.db",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table user(email text primary key,password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists user");


    }

    public boolean insert(String email,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cvs =  new ContentValues();
        cvs.put("email",email);
        cvs.put("password",password);
        long ins = db.insert("user",null,cvs);
        if (ins==1) return false;
        else return true;
    }
    public Boolean chkemail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery("Select * from user where email=?",new  String[]{email});
        if (c2.getCount() > 0) return false;
        else return true;
    }

    public Boolean emailpassword(String email,String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery("Select * from user where email=? and password=?",new String[]{email,password});
        if (c2.getCount() > 0) return true;
        else  return false;
    }
}
