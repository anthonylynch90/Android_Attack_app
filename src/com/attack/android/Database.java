package com.attack.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
	private static final String DATABASE_PATH = "/data/data/com.attack.android/databases/";
	private static final String DATABASE_NAME = "AttackAppDatabase";
	private String createTable = "CREATE TABLE user(ID INTEGER, firstName TEXT, secondName TEXT," +
			" address TEXT, securityPin TEXT, location TEXT, emailAddress TEXT, password TEXT);";
	private String createTableContacts = "CREATE TABLE contacts(ID INTEGER, name TEXT," +
	" number TEXT);";
	private String createTableAudio = "CREATE TABLE audio(ID INTEGER, wordActivation TEXT," +
			"wordActivationOn TEXT, decibelOn TEXT);";
	private String createTableEmail = "CREATE TABLE emailContacts(ID INTEGER, name TEXT, emailAddress TEXT);";
	
	private String myPath = DATABASE_PATH + DATABASE_NAME;

	private Context myContext;
	private SQLiteDatabase myDatabase; 

	public Database(Context context) {
		 super(context, DATABASE_NAME, null, 1);
		  this.myContext = context;
	}

	public void saveInfo(ContentValues values){
		myDatabase = getWritableDatabase();
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	
		myDatabase.insert("user" , "" , values);
		myDatabase.close();
	}

	public void saveGmail(String address, String password){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	
		ContentValues args = new ContentValues();
		args.put("emailAddress", address);
		args.put("password", password);
		myDatabase.update("user", args, "ID ="+1, null);
		args.clear();
		myDatabase.close();
	}
	
	public boolean hasGmail(){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
		boolean found = false;
		Cursor cursor = myDatabase.rawQuery("SELECT emailAddress FROM user;", null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			String val = cursor.getString(cursor.getColumnIndex("emailAddress"));
			if(val != null){
				found = true;
			}
		}
		cursor.close();
		myDatabase.close();
		return found;
	}
	
	public void saveEmailContact(String name, String address){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("emailAddress", address);
		myDatabase.insert("emailContacts","", args);
		args.clear();
		myDatabase.close();
	}
	
	public String[] getGmail(){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		String[] args1 = new String[2];
		Cursor args = myDatabase.rawQuery("SELECT * FROM user;", null);
		
		args.moveToFirst();
		args1[0] = args.getString(args.getColumnIndex("emailAddress"));
		args1[1] = args.getString(args.getColumnIndex("password"));
		myDatabase.close();
		args.close();
		return args1;		
	}
	
	public Cursor getEmailContacts(){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		Cursor args = myDatabase.rawQuery("SELECT * FROM emailContacts;", null);
		args.getCount();
		myDatabase.close();
		return args;		
	}
	
	public void deleteEmailContact(String value){
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		myDatabase.delete("emailContacts", "name=?", new String[]{value});
		myDatabase.close();
	}
	
	public boolean checkDataBase(){

		 SQLiteDatabase checkDB = null;

		 try{
			 String myPath = DATABASE_PATH + DATABASE_NAME;
			 //This causes the collator LOCALIZED not to be created. You must be consistent
			 //when using this flag to use the setting the database was created with.
			 checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

		 }catch(SQLiteException e){
			 //database does't exist yet.

		 }

		 if(checkDB != null){

		  checkDB.close();

		 }
		 else{
			 checkDB = this.getReadableDatabase(); 
			 checkDB.close();
		 }

		 return checkDB != null ? true : false;
		}
	
	public void close() {
		 //this closes the database. You would have to do this every time you finish using it as it 
		 //will speed up the runtime of the application.
		if(myDatabase != null)
		   myDatabase.close();
		   super.close();
	}

		 @Override
		 public void onCreate(SQLiteDatabase db) {
			 db.execSQL(createTable);
			 db.execSQL(createTableContacts);
			 db.execSQL(createTableAudio);
			 db.execSQL(createTableEmail);
			 
		 }


		 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  }
		 
		 public void createTables() {
			 try {
				 myDatabase = getWritableDatabase();
			 }catch(Exception e){
			 }
			 myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
				try{ 
				    myDatabase.execSQL(createTable);
				}catch(SQLException sql){
					
				}
				try{ 
				    myDatabase.execSQL(createTableContacts);
				}catch(SQLException sql){
		
				}
				try{
					myDatabase.execSQL(createTableAudio);
				}catch(SQLException sql){
					
				}
				try{
					myDatabase.execSQL(createTableEmail);
				}catch(SQLException sql){
					
				}
				myDatabase.close();
			}

		public boolean checkdetails(Context c) {
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
			Cursor cursor = myDatabase.rawQuery("SELECT firstName FROM user;", null);
			if(cursor.getCount()>0){
				cursor.moveToFirst();
				myDatabase.close();
				cursor.close();
			   return true;
			}
			else{
				myDatabase.close();
				cursor.close();
				return false;
			}
		}
			
		public Cursor getNumbers() {
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
			Cursor cursor = myDatabase.rawQuery("SELECT * FROM contacts", null);
			cursor.getCount();
			myDatabase.close();
			return cursor;
		}
		
		public boolean checkPin(String pin, Context context) {
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
				boolean match = false;
				Cursor cursor = myDatabase.rawQuery("SELECT securityPin FROM user;", null);
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					if(cursor.getString(cursor.getColumnIndex("securityPin")).equals(pin)){
						match = true;
					}
				}
				cursor.close();
				myDatabase.close();
				return match;
		}

		public void addLocation(String location) {
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
			ContentValues value = new ContentValues();
			value.put("location", location);
			myDatabase.update("user", value, "ID" + "=" + 1, null);
			myDatabase.close();
		}
		public String getLocation() {
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);	 
			Cursor cursor = myDatabase.rawQuery("SELECT * FROM user;", null);
			cursor.moveToFirst();
			String location = cursor.getString(cursor.getColumnIndex("location"));
			cursor.close();
			myDatabase.close();
			return location;
		}
		
		public String[] getPersonalDetails(){
			String[] values = new String[3];
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor cursor = myDatabase.rawQuery("SELECT * FROM user;", null);
			if(cursor!=null){
				cursor.moveToFirst();
				values [0] = cursor.getString(cursor.getColumnIndex("firstName"));
				values [1] = cursor.getString(cursor.getColumnIndex("secondName"));
				values [2] = cursor.getString(cursor.getColumnIndex("address"));
			}
			myDatabase.close();
			return values;
		}
		
		public void updateTableUser(ContentValues args){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			myDatabase.update("user", args, "ID ="+1, null);
			args.clear();
			myDatabase.close();
		}
		
		public void updateTableContacts(ContentValues args){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			myDatabase.insert("contacts" , "" , args);
			args.clear();
			myDatabase.close();
		}
		
		public Cursor getContacts(Context c){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor contacts = myDatabase.rawQuery("SELECT name FROM contacts;", null);
			contacts.getCount();
			myDatabase.close();
			return contacts;
		}
		
		public void deleteContact(String value){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			myDatabase.delete("contacts", "name=?", new String[]{value});
			myDatabase.close();
		}
		
		public void addWordActivation(String value){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			ContentValues args = new ContentValues();
			args.put("wordActivation", value);
			args.put("wordActivationOn", "OFF");
			args.put("decibelOn", "OFF");
			args.put("ID", 1);
			myDatabase.insert("audio","", args);
			args.clear();
			myDatabase.close();
		}
		
		public void updateWordActivation(String value){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			ContentValues args = new ContentValues();
			args.put("wordActivation", value);
			myDatabase.update("audio", args, "ID ="+1, null);
			args.clear();
			myDatabase.close();
		}
		
		public String getActivationWord(){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor activationWord = myDatabase.rawQuery("SELECT * FROM audio;", null);
			activationWord.moveToFirst();
			String value = activationWord.getString(activationWord.getColumnIndex("wordActivation"));
			activationWord.close();
			myDatabase.close();
			return value;
		}
		
		public String getActivationState(){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor activationWord = myDatabase.rawQuery("SELECT * FROM audio;", null);
			activationWord.moveToFirst();
			String value = activationWord.getString(activationWord.getColumnIndex("wordActivationOn"));
			activationWord.close();
			myDatabase.close();
			return value;		
		}
		public void setActivationWordState(String value){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			ContentValues args = new ContentValues();
			args.put("wordActivationOn", value);
			myDatabase.update("audio", args, "ID ="+1, null);
			args.clear();
			myDatabase.close();
		}
		public String getDecibelState(){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor activationWord = myDatabase.rawQuery("SELECT * FROM audio;", null);
			activationWord.moveToFirst();
			String value = activationWord.getString(activationWord.getColumnIndex("decibelOn"));
			activationWord.close();
			myDatabase.close();
			return value;		
		}
		public void setDecibelState(String value){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			ContentValues args = new ContentValues();
			args.put("decibelOn", value);
			myDatabase.update("audio", args, "ID ="+1, null);
			args.clear();
			myDatabase.close();
		}
		public Cursor getContacts(){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			Cursor args = myDatabase.rawQuery("SELECT * FROM contacts;", null);
			myDatabase.close();
			return args;		
		}
		
		protected void onStop(){
			super.close();
		}
		
}
