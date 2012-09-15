package com.xinyin.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public final static int VERSION = 1;
	public final static String TABLE_NAME_KUAIDI = "query_kuaidi";
	public final static String TABLE_NAME_WEATHER = "query_weather";
	public final static String TABLE_NAME_PHONE = "query_phone";
	public final static String TABLE_NAME_QQ = "query_qq";
	public final static String TABLE_NAME_IDCARD = "query_idcard";
	public final static String DATABASE_NAME = "query.db";
	

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String kuaidi_sql = "CREATE TABLE "+TABLE_NAME_KUAIDI+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,kuaidiOrder text,com text,comName text );";
		String idcard_sql = "CREATE TABLE "+TABLE_NAME_IDCARD+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,idNum text );";
		String qq_sql = "CREATE TABLE "+TABLE_NAME_QQ+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,qqNum text );";
		String phone_sql = "CREATE TABLE "+TABLE_NAME_PHONE+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,phoneNum text );";
		String weather_sql = "CREATE TABLE "+TABLE_NAME_WEATHER+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,cityName text );";
		db.execSQL(kuaidi_sql);
		db.execSQL(idcard_sql);
		db.execSQL(qq_sql);
		db.execSQL(phone_sql);
		db.execSQL(weather_sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
