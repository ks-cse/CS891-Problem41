package com.ksinha.prob41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private String TABLE_NAME = "addbook";
    private String KEY_ID = "id";
    private String KEY_NM = "name";
    private String KEY_PH = "phone";
    private String KEY_EM = "email";

    public DatabaseHandler(Context context){
        super(context,"db_addbook.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME +" ("+ KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_NM +" TEXT,"+ KEY_PH +" TEXT,"+ KEY_EM +" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert
    public boolean addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(KEY_ID,contact.getId());
        cv.put(KEY_NM,contact.getName());
        cv.put(KEY_PH,contact.getPhone());
        cv.put(KEY_EM,contact.getEmail());
        db.insert(TABLE_NAME,null,cv);
        db.close();
        return true;
    }

    //Read
    public Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,KEY_NM,KEY_PH,KEY_EM },
                KEY_ID + "=?",new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        //Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        //return res;
        db.close();
        return contact;
    }

    //Read All Names
    public ArrayList<Contact> getAllNames(){
        ArrayList<Contact> allnames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                // Adding contact to list
                allnames.add(contact);
            } while (cursor.moveToNext());
        }
        return allnames;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NM, contact.getName());
        values.put(KEY_PH, contact.getPhone());
        values.put(KEY_EM, contact.getEmail());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",new String[] { String.valueOf(contact.getId()) });
        db.close();
    }
}
