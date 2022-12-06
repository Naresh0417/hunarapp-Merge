package com.hamstechonline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hamstechonline.datamodel.UserDatamodel;

public class UserDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Hamtech";
    private static final String DATABASE_TABLE = "DATABASE_TABLE";

    private static final String prospectname = "prospectname";
    private static final String phone = "phone";

    public UserDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query = "CREATE TABLE " + DATABASE_TABLE +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, prospectname TEXT,phone TEXT)";
        db.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        onCreate(db);
    }

    public long InsertUser(UserDatamodel details){
        ContentValues values = new ContentValues();

        values.put(prospectname, details.getProspectname());
        values.put(phone, details.getPhone());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(DATABASE_TABLE, null, values);
        sqLiteDatabase.close();

        return id;
    }

    public void updatetUserId(UserDatamodel u) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(prospectname, u.getProspectname());
        values.put(phone, u.getPhone());
        String x="Id=1";
        db.update(DATABASE_TABLE, values,x, null);
        db.close();

    }
    public String getUserMobileNumber(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT phone FROM "+DATABASE_TABLE+" where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        String s=cursor.getString(0);
        cursor.close();db.close();
        return s;
    }
    public String getUserName(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT prospectname FROM "+DATABASE_TABLE+" where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        String s=cursor.getString(0);
        cursor.close();db.close();
        return s;
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+ DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        int x=cursor.getCount();
        cursor.close();
        db.close();
        return x;
    }
}
