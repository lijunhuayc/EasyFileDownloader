package com.lijunhuayc.downloader.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/09/20 14:03.
 * Email: lijunhuayc@sina.com
 */
public class DownloadDBHelper extends BaseDBManager {
    private static DownloadDBHelper dBManager = null;

    private DownloadDBHelper(Context mContext, String dbName) {
        super(mContext, dbName);
    }

    public static DownloadDBHelper getInstance(Context mContext, String dbName) {
        if (null == dBManager) {
            synchronized (DownloadDBHelper.class) {
                if (null == dBManager) {
                    dBManager = new DownloadDBHelper(mContext, dbName);
                }
            }
        }
        return dBManager;
    }

    @Override
    protected SQLiteDatabase openDatabase() {
        return getSQLiteOpenHelper().getWritableDatabase();
    }

    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper() {
        return new DBOpenHelper(mContext);
    }

    public void save(String url, Map<Integer, Integer> map) {
        Log.d(TAG, "save: url = " + url + ", map = " + map.toString());
        SQLiteDatabase database = openDatabase();
        database.beginTransaction();
        try {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                database.execSQL("insert into " + DBOpenHelper.TABLE_NAME + " ("
                                + DBOpenHelper.FIELD_URL + ", "
                                + DBOpenHelper.FIELD_THREAD_ID + ", "
                                + DBOpenHelper.FIELD_DOWNLOAD_LENGTH + ") values(?, ?, ?)",
                        new String[]{url, entry.getKey() + "", "" + entry.getValue()});
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    public void update(String url, Map<Integer, Integer> map) {
//        Log.d(TAG, "update: url = " + url + ", map = " + map.toString());
        SQLiteDatabase database = openDatabase();
        database.beginTransaction();
        try {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                database.execSQL("update " + DBOpenHelper.TABLE_NAME + " set "
                                + DBOpenHelper.FIELD_DOWNLOAD_LENGTH + "=? where "
                                + DBOpenHelper.FIELD_THREAD_ID + "=? and "
                                + DBOpenHelper.FIELD_URL + "=?",
                        new String[]{"" + entry.getValue(), entry.getKey() + "", url});
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    public Map<Integer, Integer> query(String url) {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        SQLiteDatabase database = openDatabase();
        Cursor cursor = database.rawQuery("select " +
                        DBOpenHelper.FIELD_THREAD_ID + ", " +
                        DBOpenHelper.FIELD_DOWNLOAD_LENGTH +
                        " from " + DBOpenHelper.TABLE_NAME +
                        " where " + DBOpenHelper.FIELD_URL + "=?",
                new String[]{url});

        while (cursor.moveToNext()) {
            map.put(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close();
        database.close();
//        Log.d(TAG, "query: url = " + url + ", map = " + map.toString());
        return map;
    }

    public void delete(String url) {
//        Log.d(TAG, "delete: url = " + url);
        SQLiteDatabase database = openDatabase();
        try {
            database.execSQL("delete from " + DBOpenHelper.TABLE_NAME + " where " + DBOpenHelper.FIELD_URL + "=?", new String[]{url});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

}
