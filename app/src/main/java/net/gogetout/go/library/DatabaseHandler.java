package net.gogetout.go.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "AXDB";

    private static final String TABLE_USERS = "users";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_UID = "uid";
    private static final String KEY_INTERESTS = "interests";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_INTERESTS + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String email, String uname, String uid, String interests) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, uname); // UserName
        values.put(KEY_UID, uid); // Email
        values.put(KEY_INTERESTS, interests);
        //values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("email", cursor.getString(1));
            user.put("uname", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("interests", cursor.getString(4));
            //user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }


    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public String getUserEmail() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_USERS, new String[]{KEY_EMAIL}, KEY_ID + "=1", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor.getString(0);
    }

    public String getUserInterests() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_USERS, new String[]{KEY_INTERESTS}, KEY_ID + "=1", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor.getString(0);
    }

    public String getUID() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_USERS, new String[]{KEY_UID}, KEY_ID + "=1", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String uid = mCursor.getString(0);

        db.close();
        mCursor.close();
        return uid;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USERS, null, null);
        db.close();
    }

}
