package com.example.dara.galery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "galery_online";

    // table name
    private static final String TABLE_FOTO = "foto";

    // column tables
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PATH = "path_foto";
    private static final String KEY_DESC = "deskripsi";
    private static final String KEY_LOC = "lokasi";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FOTO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PATH + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_LOC + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTO);
        onCreate(db);
    }

    //tambah data
    public void tambah_foto(Foto fotos){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, fotos.getNama());
        values.put(KEY_PATH, fotos.getPath_foto());
        values.put(KEY_DESC, fotos.getDeskripsi());
        values.put(KEY_LOC, fotos.getLokasi());

        db.insert(TABLE_FOTO, null, values);
        db.close();
    }

    public Foto getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

       Cursor cursor = db.query(TABLE_FOTO, new String[] { KEY_ID,
                        KEY_NAME, KEY_PATH, KEY_DESC, KEY_LOC }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Foto data = new Foto(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

        return data;
    }


    // get All Record
    public List<Foto> getAllDatas() {
        List<Foto> dataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Foto foto = new Foto();
                foto.setId(Integer.parseInt(cursor.getString(0)));
                foto.setNama(cursor.getString(1));
                foto.setPath_foto(cursor.getString(2));
                foto.setDeskripsi(cursor.getString(3));
                foto.setLokasi(cursor.getString(4));

                dataList.add(foto);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }


    //htung jumlah  record
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FOTO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //update data
    public int update(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, foto.getNama());
        values.put(KEY_PATH, foto.getPath_foto());
        values.put(KEY_DESC, foto.getDeskripsi());
        values.put(KEY_LOC, foto.getLokasi());

        // updating row
        return db.update(TABLE_FOTO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(foto.getId()) });
    }

    //Hapus
    public void delete(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOTO, KEY_ID + " = ?",
                new String[] { String.valueOf(foto.getId()) });
        db.close();
    }
}
