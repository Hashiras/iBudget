package com.example.myapplication.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Data.Balance;

import java.util.ArrayList;
import java.util.List;

public class BalanceHelper extends SQLiteOpenHelper {
    public BalanceHelper(Context context) {
        super(context, "DB_Finance", null, 1);
    }

    private final String TABLENAME = "tbl_Balances";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "balance REAL," +
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

    public void insert(Balance data){
        ContentValues contentValues = prepareData(data);
        new insertTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public List<Balance> get(){
        List<Balance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, balance FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return list;
    }

    public Balance get(int ID){
        Balance data = new Balance();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, balance FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);

        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return data;
    }

    public void update(Balance data){
        ContentValues contentValues = prepareData(data);
        new updateTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public void remove(Balance data){
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

    private ContentValues prepareData(Balance data){
        ContentValues content = new ContentValues();
        content.put("balance", data.getBalance());
        content.put("archived", 0);
        return  content;
    }

    private Balance prepareData(Cursor cursor){
        Balance data = new Balance();
        data.setID(cursor.getInt(0));
        data.setBalance(cursor.getDouble(1));
        return data;
    }

    private class PreparedData{
        private SQLiteDatabase db;
        private Balance data;
        private ContentValues values;

        public PreparedData(SQLiteDatabase db, Balance data, ContentValues values) {
            this.db = db;
            this.data = data;
            this.values = values;
        }
    }

    private class insertTask extends AsyncTask<PreparedData, Void, Void> {
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
            Balance data = preparedData[0].data;
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
            Balance data = preparedData[0].data;
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