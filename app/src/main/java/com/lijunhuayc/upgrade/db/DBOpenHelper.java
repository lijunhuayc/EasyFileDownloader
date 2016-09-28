package com.lijunhuayc.upgrade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/09/20 13:55.
 * Email: lijunhuayc@sina.com
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "file_download.db";
    public static final String TABLE_NAME = "file_download_record_table";
    private static final String COMMON_PRIMARY_KEY_ID = " _id integer not null primary key autoincrement,";
    public static final String FIELD_URL = "_url";
    public static final String FIELD_THREAD_ID = "_thread_id";
    public static final String FIELD_DOWNLOAD_LENGTH = "_download_length";

    public DBOpenHelper(Context mContext) {
        super(mContext, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("create table if not exists " + TABLE_NAME
                + "(" + COMMON_PRIMARY_KEY_ID
                + FIELD_URL + " character,"
                + FIELD_THREAD_ID + " integer,"
                + FIELD_DOWNLOAD_LENGTH + " integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
