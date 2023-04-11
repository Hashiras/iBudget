package com.example.myapplication.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Data.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryHelper extends SQLiteOpenHelper {
    public CategoryHelper(Context context) {
        super(context, "DB_Finance", null, 1);
    }

    private final String TABLENAME = "tbl_Categories";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "accountID INTEGER," +
                    "name TEXT," +
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

    public void insert(Category data){
        ContentValues contentValues = prepareData(data);
        new insertTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public List<Category> get(){
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, accountID, name FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return list;
    }

    public Category get(int ID){
        Category data = new Category();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, accountID, name FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);

        }catch (SQLiteException e){
            Log.e("DBHelper", "Unable to retrieve from the " + TABLENAME, e.getCause());
        }
        return data;
    }

    public void update(Category data){
        ContentValues contentValues = prepareData(data);
        new updateTask().execute(new PreparedData(getWritableDatabase(), data, contentValues));
    }

    public void remove(Category data){
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

    private ContentValues prepareData(Category data){
        ContentValues content = new ContentValues();
        if(data.getAccountID() != 0)
            content.put("accountID", data.getAccountID());
        if(data.getName() != null)
            content.put("name", data.getName());
        content.put("archived", 0);
        return  content;
    }

    private Category prepareData(Cursor cursor){
        Category data = new Category();
        data.setID(cursor.getInt(0));
        data.setAccountID(cursor.getInt(1));
        data.setName(cursor.getString(2));
        return data;
    }

    private class PreparedData{
        private SQLiteDatabase db;
        private Category data;
        private ContentValues values;

        public PreparedData(SQLiteDatabase db, Category data, ContentValues values) {
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
            Category data = preparedData[0].data;
            try{
                db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            }catch (SQLiteException e){
                Log.e("DBHelper", "Unable to insert into the " + TABLENAME, e.getCause());
            }
            return null;
        }
    }

    private class removeTask extends AsyncTask<PreparedData, Void, Void> {
        @Override
        protected Void doInBackground(PreparedData... preparedData) {
            SQLiteDatabase db = preparedData[0].db;
            ContentValues values = new ContentValues();
            Category data = preparedData[0].data;
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