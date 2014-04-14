package project.finalyear.database;

import java.util.ArrayList;
import java.util.List;

//import com.example.synchronize.ChangeTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PASSWORD };
	
	public UserDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Users selectUser(String name) {
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
				MySQLiteHelper.COLUMN_NAME + " = \"" + name + "\"", null, null,
				null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast())
			return null;
		Users newUser = cursorToUsers(cursor);
		cursor.close();
		close();
		return newUser;
	}

	public Users getUserById(long id) {
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast())
			return null;
		Users newUser = cursorToUsers(cursor);
		cursor.close();
		close();
		return newUser;
	}

	public void updateUsers(Users user) {
		open();
		long id = user.getId();

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, user.getName());
		values.put(MySQLiteHelper.COLUMN_PASSWORD, user.getPwd());
		database.update(MySQLiteHelper.TABLE_USERS, values,
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
		close();
	}

	public Users createUsers(String name, String pwd) {
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PASSWORD, pwd);
		long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Users newUser = cursorToUsers(cursor);
		cursor.close();
		close();
		return newUser;
	}

	public void deleteUser(Users user) {
		open();
		long id = user.getId();
		System.out.println("User deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		close();
	}

	public List<Users> getAllUsers() {
		open();
		List<Users> allusers = new ArrayList<Users>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Users user = cursorToUsers(cursor);
			allusers.add(user);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return allusers;
	}

	private Users cursorToUsers(Cursor cursor) {
		Users user = new Users();
		user.setId(cursor.getLong(0));
		user.setName(cursor.getString(1));
		user.setPwd(cursor.getString(2));
		return user;
	}
}
