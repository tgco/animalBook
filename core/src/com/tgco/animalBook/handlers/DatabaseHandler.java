package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

/**
 * Manages database functionality to save information between user plays.  Currently replaced by the Preferences class
 * provided by libGDX.
 * 
 * @author
 *
 */
public class DatabaseHandler {
	Database dbHandler;

	public static final String TABLE_NAME = "datas";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_MONEY = "money";
	public static final String COLUMN_NUM_ANIMALS = "animalNum";

	private static final String DATABASE_NAME = "data\\datas.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_LEVEL + " integer not null," + 
			COLUMN_MONEY + " integer not null, " +
			COLUMN_NUM_ANIMALS + " integer not null" + ");";

	public DatabaseHandler() {
		super();
		dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

		dbHandler.setupDatabase();
		try {
			dbHandler.openOrCreateDatabase();
			dbHandler.execSQL(DATABASE_CREATE);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		
		Gdx.app.log("DatabaseTest", "created successfully");
		DatabaseCursor cursor = null;

		try {
			cursor = dbHandler.rawQuery("SELECT * FROM datas");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		if(!cursor.next()){
			initValue("0", "0", "0", "0");
			initValue("1", "0", "0", "0");
		
		}
		
	}
	
	private void initValue( String spot,String money, String level, String animalNum){
		try {
				dbHandler.execSQL("INSERT INTO " + TABLE_NAME + " (level,money,animalNum) VALUES "+
						"(" + level + ", '"+money+"', '"+animalNum+"')");
			} catch (SQLiteGdxException e) {
				e.printStackTrace();
			}
	}
	
	public void UpdateValue( String spot,String money, String level, String animalNum){
		try {
				dbHandler.execSQL("UPDATE " + TABLE_NAME + " SET level = "+ level + ", money = " + money + ", animalNum = "+ animalNum +
						" WHERE _id = " + spot);
			} catch (SQLiteGdxException e) {
				e.printStackTrace();
			}
	}
	
	public void getValue( String spot){
		//TODO:: run a get statement.
		DatabaseCursor cursor = null;

		try {
			cursor = dbHandler.rawQuery("SELECT * FROM datas");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		if(cursor.next()){
			Gdx.app.log("DataBaseText", "the cursor value is " + String.valueOf(cursor.getInt(1)));
		}
	}
	
	public void close(){
		try {
			dbHandler.closeDatabase();
			} catch (SQLiteGdxException e) {
			e.printStackTrace();
			}
			dbHandler = null;
			Gdx.app.log("DatabaseTest", "dispose");
	}
}
