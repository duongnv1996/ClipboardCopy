package com.duongkk.clipboardcopypro.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.duongkk.clipboardcopypro.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 8/25/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public final static String DB_NAME = "message.db";
    public final static String TABLE_NAME = "t_favourite";
    public final static int VERSION = 1;
    public final static String KEY_ID = "id";
    public final static String KEY_CONTENT = "content";
    public final static String KEY_CODE = "code";
    public final static String KEY_DATE = "date";

    Context context;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public long insertRow(Message msg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, msg.getId());
        values.put(KEY_CODE, msg.getCode());
        values.put(KEY_CONTENT, msg.getContent());
        values.put(KEY_DATE, msg.getDate());
        return db.insert(TABLE_NAME, null, values);
    }
    public long removeRow(String id){
        SQLiteDatabase  db = getReadableDatabase();
        return db.delete(TABLE_NAME,KEY_CODE+" =?",new String[]{id});
    }
    public List<Message> getAllRows(){
        List<Message> listMessage= new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME,null);
        while (cursor.moveToNext()){
            Message msg = new Message();
            String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
            String code = cursor.getString(cursor.getColumnIndex(KEY_CODE));
            String content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
            String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));

            msg.setCode(code);
            msg.setContent(content);
            msg.setId(id);
            msg.setDate(date);
            listMessage.add(msg);
        }
        return listMessage;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create table " + TABLE_NAME + " ( " + KEY_ID + "  text , "
                + KEY_CODE + " text primary key , "
                + KEY_CONTENT + " text , "
                + KEY_DATE + " text )";
        sqLiteDatabase.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP table " + TABLE_NAME + " if  exists");
        onCreate(sqLiteDatabase);
    }
}
