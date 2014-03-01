package project.finalyear.model;


import java.util.ArrayList;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper {
public static ArrayList<String[]> DATA = new ArrayList<String[]>();
public static String s=null;
private static final String DATABASE_NAME = "Login.db";
private static final int DATABASE_VERSION = 1;
private static final String STUDENT_TABLE = "friend";
private static final String STUDENT_TABLE1 = "retail";
private static final String RETAILER_TABLE = "rlogin";
private static final String USER_TABLE = "ulogin";

private Context context;

private SQLiteDatabase db = null;
OpenHelper openHelper;

public SQLiteHelper(Context context) {
this.context = context;

if (db != null)
if (db.isOpen())
db.close();

openHelper = new OpenHelper(this.context);
this.db = openHelper.getWritableDatabase();
}

public void close() {
if (openHelper != null) {
openHelper.close();
}
}
public int insertRetailerLoginData(String name, String password) {
int entryId = 0;
ContentValues values = new ContentValues();
values.put("un", name);
values.put("pw", password);

entryId = (int) this.db.insert(RETAILER_TABLE, null, values);
return entryId;
}
public int insertUserLoginData(String name, String password) {
int entryId = 0;
ContentValues values = new ContentValues();
values.put("un", name);
values.put("pw", password);

entryId = (int) this.db.insert(USER_TABLE, null, values);
return entryId;
}
public String retailerloginData(String h,String h1) {
	// TODO Auto-generated method stub
	Cursor c1 = null;
	DATA.clear();
	String[] columns = new String[] { "un" ,"pw"};

	c1 = db.query(RETAILER_TABLE, columns,"un=? AND pw=?  ", new String[] {h,h1}, null,null,null);
	if (c1.moveToNext()) {

String n=c1.getString(0);
	return "ok";
	}
	else
	{
	return "no";
	}
	}
public String userloginData(String h,String h1) {
	// TODO Auto-generated method stub
	Cursor c1 = null;
	DATA.clear();
	String[] columns = new String[] { "un" ,"pw"};

	c1 = db.query(USER_TABLE, columns,"un=? AND pw=?  ", new String[] {h,h1}, null,null,null);
	if (c1.moveToNext()) {

String n=c1.getString(0);
	return "ok";
	}
	else
	{
	return "no";
	}
	}
public int insertData(String name, String address, String contact,String d) {
int entryId = 0;
ContentValues values = new ContentValues();
values.put("da", name);
values.put("ta", address);
values.put("lat", contact);
values.put("lon", d);
entryId = (int) this.db.insert(STUDENT_TABLE, null, values);
return entryId;
}
public int insertData1(String name, String address, String contact,String da,String d,String h) {
int entryId = 0;
ContentValues values = new ContentValues();
values.put("rn", name);
values.put("p", address);
values.put("pe", contact);
values.put("da", da);
values.put("lat", d);
values.put("lon", h);
entryId = (int) this.db.insert(STUDENT_TABLE1, null, values);
return entryId;
}
public String selectdatabase(String search) {
Cursor c = null;
//DATA.clear();
String[] columns = new String[] {  "address" };
c = db.query(STUDENT_TABLE, columns,
"name=?", new String[] { search }, null, null, "id DESC");
if (c.moveToFirst()) {
do {
if (c.getColumnCount() == 2) {
/*
	String[] str = new String[3];
str[0] = c.getString(1);
str[1] = c.getString(2);
str[2] = c.getString(3);
DATA.add(str);
*/
	s = c.getString(1);
}
} while (c.moveToNext());
}

if (c != null && !c.isClosed()) {
c.close();
}
return s;
}

public ArrayList<String[]> selectalldatabase2() {
Cursor c = null;
DATA.clear();
String[] columns = new String[] { "id", "da", "ta", "lat" };
c = db.query(STUDENT_TABLE, columns, null, null, null, null, "id DESC");
if (c.moveToFirst()) {
do {
if (c.getColumnCount() == 4) {
String[] str = new String[3];
str[0] = c.getString(1);
str[1] = c.getString(2);
str[2] = c.getString(3);
DATA.add(str);
}
} while (c.moveToNext());
}

if (c != null && !c.isClosed()) {
c.close();
}

return DATA;
}

public ArrayList<String[]> selectalldatabase(String search ,double d,double d1) {
Cursor c = null;
DATA.clear();
int i=0;
String[] columns = new String[] { "id", "ta", "lat","lon" };
c = db.query(STUDENT_TABLE, columns,"da=?", new String[] { search }, null, null, "id DESC");
if (c.moveToFirst()) {
do {
if (c.getColumnCount() == 4) {
String[] str = new String[5];
str[0] = c.getString(1);
str[1] = c.getString(2);
str[2] = c.getString(3);
String h=str[1];
String h1=str[2];
double lat1=d;
double lng1=d1;
double f=Double.parseDouble(h);
double f1=Double.parseDouble(h1);
String ans=distance(lat1,f,lng1,f1);

//String ans1=ans.substring(0, 4);
double dd=Double.parseDouble(ans);
double m=Math.round(dd*100.0)/100.0;
int a=(int)dd;
String di=Integer.toString(a);
str[3]="distance="+m+"Km";
//String s[]=ans.split(".");
//String s1=s[0];
//String s2=s[1];
//int d=Integer.parseInt(s1);

if(a<2)
{
	
	
	str[4]="Task"+""+i++;
	
DATA.add(str);
}
}
} while (c.moveToNext());
}

if (c != null && !c.isClosed()) {
c.close();
}

return DATA;
}

public ArrayList<String[]> selectalldatabase1(String search ,double d,double d1) {
Cursor c = null;
DATA.clear();
int i=0;
String[] columns = new String[] { "id", "rn", "p", "pe", "lat","lon" };
c = db.query(STUDENT_TABLE1, columns,"da=?", new String[] { search }, null, null, "id DESC");
if (c.moveToFirst()) {
do {
if (c.getColumnCount() == 6) {
String[] str = new String[7];
str[0] = c.getString(1);
str[1] = c.getString(2);
str[2] = c.getString(3);
str[3] = c.getString(4);
str[4] = c.getString(5);
String h=str[3];
String h1=str[4];
double lat1=d;
double lng1=d1;
double f=Double.parseDouble(h);
double f1=Double.parseDouble(h1);
String ans=distance(lat1,f,lng1,f1);

//String ans1=ans.substring(0, 4);
double dd=Double.parseDouble(ans);
double m=Math.round(dd*100.0)/100.0;
int a=(int)dd;
String di=Integer.toString(a);
str[5]="distance="+m+"Km";
//String s[]=ans.split(".");
//String s1=s[0];
//String s2=s[1];
//int d=Integer.parseInt(s1);

if(a<2)
{
	
	
	str[6]="Retailer"+""+i++;
	
DATA.add(str);
}
}
} while (c.moveToNext());
}

if (c != null && !c.isClosed()) {
c.close();
}

return DATA;
}



public String distance(double lat1, double lat2, double lng1, double lng2) {
	double temp1 = lng1 - lng2;
	double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
			+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
			* Math.cos(deg2rad(temp1));
	dist = Math.acos(dist);
	dist = rad2deg(dist);
	dist = dist * 60 * 1.1515;

	return String.valueOf(dist);
}

private double deg2rad(double deg) {
	return (deg * Math.PI / 180.0);
}

private double rad2deg(double rad) {
	return (rad * 180.0 / Math.PI);
}



static class OpenHelper extends SQLiteOpenHelper {

OpenHelper(Context context) {
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

@Override
public void onCreate(SQLiteDatabase db) {

db.execSQL("CREATE TABLE " + RETAILER_TABLE + "("
+ "id INTEGER PRIMARY KEY, " + "un TEXT, "
 + "pw TEXT)");
db.execSQL("CREATE TABLE " + USER_TABLE + "("
+ "id INTEGER PRIMARY KEY, " + "un TEXT, "
 + "pw TEXT)");


db.execSQL("CREATE TABLE " + STUDENT_TABLE + "("
+ "id INTEGER PRIMARY KEY, " + "da TEXT, "
+ "ta TEXT,"
+ "lat TEXT, " + "lon TEXT)");

db.execSQL("CREATE TABLE " + STUDENT_TABLE1 + "("
+ "id INTEGER PRIMARY KEY, " + "rn TEXT, "
+ "p TEXT,"+ "pe TEXT, "+ "da TEXT, "
+ "lat TEXT, " + "lon TEXT)");
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
Log.w("Catch DB",
"Upgrading database, this will drop tables and recreate.");
db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE1);
db.execSQL("DROP TABLE IF EXISTS " + RETAILER_TABLE);
db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
onCreate(db);
}
}



public SQLiteDatabase getWritableDatabase() {
	// TODO Auto-generated method stub
	return null;
}

}
