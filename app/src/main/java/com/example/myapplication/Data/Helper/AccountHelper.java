package com.example.myapplication.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Data.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountHelper extends SQLiteOpenHelper {
    public AccountHelper(Context context) {
        super(context, "DB_Finance", null, 1);
    }

    private final String TABLENAME = "tbl_Accounts";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fullName TEXT," +
                    "email TEXT," +
                    "username TEXT," +
                    "password TEXT," +
                    "pin TEXT," +
                    "balanceID INTEGER," +
                    "archived INTEGER)");
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to create the " + TABLENAME, e.getCause());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME);
            onCreate(db);
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to upgrade the " + TABLENAME, e.getCause());
        }
    }

    public void insert(Account data){
        ContentValues contentValues = prepareData(data);
        new insertTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public List<Account> get(){
        List<Account> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, fullname, email, username, password, pin, balanceID FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return list;
    }

    public Account get(int ID){
        Account data = new Account();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, fullname, email, username, password, pin, balanceID FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);

        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return data;
    }

    public void update(Account data){
        ContentValues contentValues = prepareData(data);
        new updateTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public void remove(Account data){
        ContentValues contentValues = prepareData(data);
        new removeTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public int getNextID(){
        int data = 0;
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT MAX(ID) FROM " + TABLENAME, null);
            while (cursor.moveToNext())
                data = cursor.getInt(0);
            data++;
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return data;
    }

    private ContentValues prepareData(Account data){
        ContentValues content = new ContentValues();
        if(data.getFullName() != null)
            content.put("fullName", data.getFullName());
        if(data.getEmail() != null)
            content.put("email", data.getEmail());
        if(data.getUsername() != null)
            content.put("username", data.getUsername());
        if(data.getPassword() != null)
            content.put("password", data.getPassword());
        if(data.getPin() != null)
            content.put("pin", data.getPin());
        if(data.getBalanceID() != 0)
            content.put("balanceID", data.getBalanceID());
        content.put("archived", 0);
        return  content;
    }

    private Account prepareData(Cursor cursor){
        Account data = new Account();
        data.setID(cursor.getInt(0));
        data.setFullName(cursor.getString(1));
        data.setEmail(cursor.getString(2));
        data.setUsername(cursor.getString(3));
        data.setPassword(cursor.getString(4));
        data.setPin(cursor.getString(5));
        data.setBalanceID(cursor.getInt(6));
        return data;
    }

    private class PreparedData{
        private SQLiteDatabase db;
        private Account data;
        private ContentValues values;

        public PreparedData(SQLiteDatabase db, Account data, ContentValues values) {
            this.db = db;
            this.data = data;
            this.values = values;
        }
    }

    private class insertTask extends AsyncTask<PreparedData, Void, Void>{
        @Override
        protected Void doInBackground(PreparedData... preparedData) {
            SQLiteDatabase db = preparedData[0].db;
            ContentValues values = preparedData[0].values;
            try{
                db.insert(TABLENAME, null, values);
            }catch (SQLiteException e){
                Log.e("DBHelper", "Unable to insert into the " + TABLENAME, e.getCause());
            }
            return null;
        }
    }

    private class updateTask extends AsyncTask<PreparedData, Void, Void>{
        @Override
        protected Void doInBackground(PreparedData... preparedData) {
            SQLiteDatabase db = preparedData[0].db;
            ContentValues values = preparedData[0].values;
            Account data = preparedData[0].data;
            try{
                db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            }catch (SQLiteException e){
                Log.e("DBHelper", "Unable to insert into the " + TABLENAME, e.getCause());
            }
            return null;
        }
    }

    private class removeTask extends AsyncTask<PreparedData, Void, Void>{
        @Override
        protected Void doInBackground(PreparedData... preparedData) {
            SQLiteDatabase db = preparedData[0].db;
            ContentValues values = new ContentValues();
            Account data = preparedData[0].data;
            values.put("archived", 1);
            try{
                db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            }catch (SQLiteException e){
                Log.e("DBHelper", "Unable to insert into the " + TABLENAME, e.getCause());
            }
            return null;
        }
    }
}
