package com.example.se328projectalalem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Students.db";
    public static final String TABLE_NAME="Student";
    public static final String COLUMN_FNAME="FNAME";
    public static final String COLUMN_LNAME="LNAME";
    public static final String COLUMN_EMAIL="EMAIL";
    public static final String COLUMN_DATE="DATE";
    public static final String COLUMN_GENDER="GENDER";
    public static final String COLUMN_SID="SID";
    public static final String COLUMN_NSID="NSID";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_FNAME + " TEXT NOT NULL,"
                + COLUMN_LNAME + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_DATE + " TEXT NOT NULL,"
                + COLUMN_GENDER + " TEXT NOT NULL,"
                + COLUMN_NSID + " TEXT NOT NULL,"
                + COLUMN_SID + " INTEGER PRIMARY KEY)");
    }

    public int insert(Student student){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FNAME, student.fName);
        values.put(COLUMN_LNAME, student.lName);
        values.put(COLUMN_EMAIL, student.email);
        values.put(COLUMN_DATE, student.date);
        values.put(COLUMN_GENDER, student.gender);
        values.put(COLUMN_NSID, student.sNatId);
        values.put(COLUMN_SID, student.sId);
        return (int) db.insert(TABLE_NAME, null, values);
    }
    public int delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String [] id_val = {id+""};
        return db.delete(TABLE_NAME, COLUMN_SID+" = ?", id_val);
    }
    public Cursor viewByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] id_val = {id+""};
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME+" WHERE "+COLUMN_SID+"=?",id_val);
        if (cursor != null)
            cursor.moveToFirst();
        if (!cursor.moveToFirst()){
            return null;
        }
        return cursor;
    }
    public Cursor viewAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        if (cursor.moveToFirst()){
            return cursor;
        }
        else{
            return null;
        }
    }
    public int updateSQLStudent(String fName, String lName, String email, String date, String natId, String gender, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FNAME, fName);
        values.put(COLUMN_LNAME, lName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_NSID, natId);
        values.put(COLUMN_SID, id);

        String [] id_val = {id+""};
        return db.update(TABLE_NAME, values, COLUMN_SID+" = ?", id_val);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
