package project.finalyear.database;

import java.util.ArrayList;
import java.util.List;

//import com.example.synchronize.ChangeTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RetailerItemDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_USERID, MySQLiteHelper.COLUMN_RETAILERNAME,
			MySQLiteHelper.COLUMN_PRODUCT,MySQLiteHelper.COLUMN_LATITUDE, 
			MySQLiteHelper.COLUMN_LONGITUDE,MySQLiteHelper.COLUMN_DUETIME,
			MySQLiteHelper.COLUMN_NODUETIME, MySQLiteHelper.COLUMN_DISCOUNT,
			MySQLiteHelper.COLUMN_CHECKED, };


	public RetailerItemDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	
	public RetailerItem getItemByItemId(long id) {
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null, null,
				null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast())
			return null;
		RetailerItem newItem = cursorToRetailerItem(cursor);
		close();
		return newItem;
	}

	public RetailerItem createItem(long userid, String retailername, String product,String latitude,String longitude,
			long duetime, boolean noduetime, boolean checked, long priority) {
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USERID, userid);
		values.put(MySQLiteHelper.COLUMN_RETAILERNAME, retailername);
		values.put(MySQLiteHelper.COLUMN_PRODUCT, product);
		values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
		values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
		values.put(MySQLiteHelper.COLUMN_DUETIME, duetime);
		if (noduetime)
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		else
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		values.put(MySQLiteHelper.COLUMN_DISCOUNT, priority);
		if (checked)
			values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		else
			values.put(MySQLiteHelper.COLUMN_CHECKED, 0);

		long insertId = database.insert(MySQLiteHelper.TABLE_RETAILERLIST, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		RetailerItem newItem = cursorToRetailerItem(cursor);
		cursor.close();
		close();

		return newItem;
	}

	public RetailerItem createItemWithId(long userid, String offerid, String retailername, String product,String latitude,String longitude,
			String duetime, String noduetime, String checked, String priority) {
		
		deleteItemById(Long.parseLong(offerid));
		
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, offerid);
		values.put(MySQLiteHelper.COLUMN_USERID, userid);
		values.put(MySQLiteHelper.COLUMN_RETAILERNAME, retailername);
		values.put(MySQLiteHelper.COLUMN_PRODUCT, product);
		values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
		values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
		values.put(MySQLiteHelper.COLUMN_DUETIME, duetime);
		if (noduetime.equals("true"))
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		else
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		values.put(MySQLiteHelper.COLUMN_DISCOUNT, priority);
		if (checked.equals("true"))
			values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		else
			values.put(MySQLiteHelper.COLUMN_CHECKED, 0);

		long insertId = database.insert(MySQLiteHelper.TABLE_RETAILERLIST, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		RetailerItem newItem = cursorToRetailerItem(cursor);
		cursor.close();
		close();
		return newItem;
	}

	
	public void deleteItem(RetailerItem item) {
		open();
//		
		long id = item.getId();
		database.delete(MySQLiteHelper.TABLE_RETAILERLIST, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		close();
	}
	
	public void deleteItemById(Long itemid) {//Used by sync method so no need to track changes made by this
		open();
		database.delete(MySQLiteHelper.TABLE_RETAILERLIST, MySQLiteHelper.COLUMN_ID
				+ " = " + itemid, null);
		close();
	}

	public int updateItem(RetailerItem item) {
		open();
//		
		long id = item.getId();

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_RETAILERNAME, item.getRetailername());
		values.put(MySQLiteHelper.COLUMN_PRODUCT, item.getProduct());
		values.put(MySQLiteHelper.COLUMN_LATITUDE, item.getLatitude());
		values.put(MySQLiteHelper.COLUMN_LONGITUDE, item.getLongitude());
		values.put(MySQLiteHelper.COLUMN_DUETIME, item.getDueTime());
		if (item.isNoDueTime())
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		else
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		values.put(MySQLiteHelper.COLUMN_DISCOUNT, item.getDiscount());
		if (item.isChecked())
			values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		else
			values.put(MySQLiteHelper.COLUMN_CHECKED, 0);

		int result =  database.update(MySQLiteHelper.TABLE_RETAILERLIST, values,
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
		close();
		return result;
	}

	public List<RetailerItem> getRetailerListByUId(long userId) {
		open();
		List<RetailerItem> list = new ArrayList<RetailerItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RetailerItem item = cursorToRetailerItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	public boolean getCompletedStatus(long offerId) {
		open();
		boolean result = false;
		String query = "SELECT "
				+ MySQLiteHelper.COLUMN_CHECKED + " FROM "
				+ MySQLiteHelper.TABLE_RETAILERLIST + " WHERE "
				+ MySQLiteHelper.COLUMN_ID + " = " + offerId ;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		if (cursor.getInt(0) == 1)
			result = true;
		cursor.close();
		close();
		return result;
	}
	
	public List<RetailerItem> getInCompleteListByUId(long userId) {
		open();
		List<RetailerItem> list = new ArrayList<RetailerItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId
						+ " AND " + MySQLiteHelper.COLUMN_CHECKED + "=0", null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RetailerItem item = cursorToRetailerItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	public List<RetailerItem> getAllRetailer() {
		open();
		List<RetailerItem> list = new ArrayList<RetailerItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_RETAILERLIST,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RetailerItem item = cursorToRetailerItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	private RetailerItem cursorToRetailerItem(Cursor cursor) {
		RetailerItem item = new RetailerItem();
		item.setId(cursor.getLong(0));
		item.setUserId(cursor.getLong(1));
		item.setRetailername(cursor.getString(2));
		item.setProduct(cursor.getString(3));
		item.setLatitude(cursor.getString(4));
		item.setLongitude(cursor.getString(5));
		item.setDueTime(cursor.getLong(6));
		item.setNoDueTime(cursor.getLong(7) == 0 ? false : true);
		item.setDiscount(cursor.getLong(8));
		item.setChecked(cursor.getLong(9) == 0 ? false : true);
		return item;
	}

	
}
