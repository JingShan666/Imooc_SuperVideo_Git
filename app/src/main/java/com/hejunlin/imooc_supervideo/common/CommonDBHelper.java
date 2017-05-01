package com.hejunlin.imooc_supervideo.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hejunlin on 17/5/1.
 */

public class CommonDBHelper extends SQLiteOpenHelper {

    private static final String TAG = CommonDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "imooc.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_ID = "albumid";
    private static final String KEY_ALBUM_SITE = "albumsite";
    private static final String KEY_ALBUM_JSON = "albumjson";
    private static final String KEY_CREATE_TIME = "createtime";
    private static final String HISTORY_TABLE_NAME = "history";
    private static final String FAVORITE_TABLE_NAME = "favorite";
    private String TABLE_NAME;

    public CommonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void setParams(String tablename) {
        TABLE_NAME = tablename;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // databse 一创建,生成两个空表
        String HISTORY_CREATE_TABLE = "create table " + HISTORY_TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + KEY_ALBUM_ID + " text,"
                + KEY_ALBUM_SITE + " integer,"
                + KEY_ALBUM_JSON + " text,"
                + KEY_CREATE_TIME + " text " + ")";
        String FAVORITE_CREATE_TABLE = "create table " + FAVORITE_TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + KEY_ALBUM_ID + " text,"
                + KEY_ALBUM_SITE + " integer,"
                + KEY_ALBUM_JSON + " text,"
                + KEY_CREATE_TIME + " text " + ")";
        Log.d(TAG , ">>  onCreate " + FAVORITE_CREATE_TABLE);
        Log.d(TAG , ">>  onCreate " + HISTORY_CREATE_TABLE);
        db.execSQL(HISTORY_CREATE_TABLE);
        db.execSQL(FAVORITE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + HISTORY_TABLE_NAME);
        db.execSQL("drop table if exists " + FAVORITE_TABLE_NAME);
        onCreate(db);
    }

    /**
     * 添加
     * @param album
     */
    public void add(Album album) {
        Album oldAlbum = getAlbumById(album.getAlbumId(), album.getSite().getSiteId());
        SQLiteDatabase db = getWritableDatabase();
        if (oldAlbum == null) {
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_ID, album.getAlbumId());
            values.put(KEY_ALBUM_SITE, album.getSite().getSiteId());
            values.put(KEY_ALBUM_JSON, album.toJson());
            values.put(KEY_CREATE_TIME, getCurrentTime());
            db.insert(TABLE_NAME, null ,values);
            db.close();
        }
    }

    /**
     * 删除
     * @param albumId
     * @param siteId
     */
    public void delete(String albumId, int siteId) {
        SQLiteDatabase db = getWritableDatabase();
        //delete(String table, String whereClause, String[] whereArgs)
        db.delete(TABLE_NAME, KEY_ALBUM_ID + " =? and " + KEY_ALBUM_SITE + " =?", new String[] {albumId, String.valueOf(siteId)});
        db.close();
    }

    /**
     * 获取所有的数据
     */
    public AlbumList getAllData() {
        AlbumList albumList = new AlbumList();
        //datetime
        String selectQuery = "select *from " + TABLE_NAME + " order by datetime(" + KEY_CREATE_TIME + ") desc";//按时间倒序排列
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Album album = Album.fromJson(cursor.getString(3));
                    albumList.add(album);
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return albumList;
    }

    private String getCurrentTime() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return formater.format(date);
    }

    public Album getAlbumById(String albumId, int siteId) {
        SQLiteDatabase db = getReadableDatabase();
        //query(String table, String[] columns, String selection,String[] selectionArgs, String groupBy, String having, String orderBy)
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ALBUM_ID, KEY_ALBUM_SITE, KEY_ALBUM_JSON, KEY_CREATE_TIME}
        , KEY_ALBUM_ID + " =? and " + KEY_ALBUM_SITE + " =? ", new String[] {albumId, String.valueOf(siteId)},
                        null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {//查询有数据
                String json = cursor.getString(2);//2表示第2个columns
                Album album = Album.fromJson(json);
                cursor.close();//取到值后,关闭游标,关闭数据库,防止泄漏
                db.close();
                return album;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
        }
        return null;
    }
}
