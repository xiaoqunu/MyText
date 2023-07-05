package com.example.mytext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.mytext.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDbOpenHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DB_NAME = "noteSQLite.db";
    //表名称
    private static final String TABLE_NAME_NOTE = "note";
    //建表语句
    private static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME_NOTE + "(id integer primary key autoincrement, title text, content text, create_time text, author text)";

    public NoteDbOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //添加日记
    public long insertData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_time", note.getCreatedTime());
        values.put("author", note.getAuthor());

        return db.insert(TABLE_NAME_NOTE, null, values);
    }

    //删除日记
    public int deleteFromDbById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        //三种写法均可
//        return db.delete(TABLE_NAME_NOTE, "id = ?", new String[]{id});
//        return db.delete(TABLE_NAME_NOTE, "id is ?", new String[]{id});
        return db.delete(TABLE_NAME_NOTE, "id like ?", new String[]{id});
    }

    //编辑日记
    public int updateData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_Time", note.getCreatedTime());
        values.put("author", note.getAuthor());

        return db.update(TABLE_NAME_NOTE, values, "id like ?", new String[]{note.getId()});
    }

    //获取全部日记
    public List<Note> queryAllFromDb() {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreatedTime(createTime);
                note.setAuthor(author);

                noteList.add(note);

            }
            cursor.close();
        }
        return noteList;
    }

    //根据标题查找日记
    public List<Note> queryFromDbByTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return queryAllFromDb();
        }

        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        //模糊查询
        Cursor cursor = db.query(TABLE_NAME_NOTE, null, "title like ?", new String[]{"%" + title + "%"}, null, null, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title2 = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title2);
                note.setContent(content);
                note.setCreatedTime(createTime);
                note.setAuthor(author);

                noteList.add(note);

            }
            cursor.close();
        }
        return noteList;
    }

}
