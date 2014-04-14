package project.finalyear.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	  public static final String TABLE_COMMENTS = "comments";
	  public static final String TABLE_USERS = "users";
	  public static final String TABLE_RETAILERLIST = "retailerlist";
	  public static final String TABLE_OFFERCHANGES = "offerchanges";//OC
	  
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_PASSWORD = "pwd";

	  public static final String COLUMN_USERID = "userid";
	  public static final String COLUMN_RETAILERNAME = "retailer";
	  public static final String COLUMN_PRODUCT = "product";
	  public static final String COLUMN_LATITUDE = "latitude";
	  public static final String COLUMN_LONGITUDE = "longitude";
	  public static final String COLUMN_DUETIME = "duetime";
	  public static final String COLUMN_NODUETIME = "noduetime";
	  public static final String COLUMN_CHECKED = "checked";
	  public static final String COLUMN_DISCOUNT = "discount";
	  
	  public static final String COLUMN_OCOFFERID = "offerid";
	  public static final String COLUMN_OCACTION = "action";
	  public static final String COLUMN_OCTIME = "timestamp";

	  
	  public static final String COLUMN_COMMENT = "comment";

	  public static final String DATABASE_NAME = "retailerlist.db";
	  private static final int DATABASE_VERSION = 1;

	  private static final String USER_DATABASE_CREATE = "create table "
			  + TABLE_USERS + "(" + COLUMN_ID
			  + " integer primary key autoincrement, " + COLUMN_NAME
			  + " text not null, " + COLUMN_PASSWORD
			  + " text not null);";

	  private static final String RETAILER_LIST_DATABASE_CREATE = "create table "
			  + TABLE_RETAILERLIST + "(" + COLUMN_ID
			  + " integer primary key autoincrement, " + COLUMN_USERID
			  + " integer default 0, " + COLUMN_RETAILERNAME
			  + " text , " + COLUMN_PRODUCT
			  + " text , " + COLUMN_LATITUDE
			  + " text , " + COLUMN_LONGITUDE
			  + " text , " + COLUMN_DUETIME
			  + " integer default 0, " + COLUMN_NODUETIME
			  + " integer default 0, " + COLUMN_DISCOUNT
			  + " integer default 0, " + COLUMN_CHECKED
			  + " integer default 0);";
	  
	  private static final String OFFER_CHANGE_DATABASE_CREATE = "create table "
			  + TABLE_OFFERCHANGES + "(" + COLUMN_OCOFFERID + " integer primary key, " 
			  + COLUMN_OCACTION  + " text , " 
			  + COLUMN_OCTIME  + " text );";
	  
	  private static final String DATABASE_CREATE = "create table "
		      + TABLE_COMMENTS + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_COMMENT
		      + " text not null);";

	  
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(USER_DATABASE_CREATE);
		database.execSQL(RETAILER_LIST_DATABASE_CREATE);
		database.execSQL(OFFER_CHANGE_DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILERLIST);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERCHANGES);
		onCreate(db);
	}

}
