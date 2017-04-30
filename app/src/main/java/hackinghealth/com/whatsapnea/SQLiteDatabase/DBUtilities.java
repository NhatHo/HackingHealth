package hackinghealth.com.whatsapnea.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by paull on 2017-04-30.
 */

public class DBUtilities {
    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String PATIENTS_TABLE = "patients_profile";
        public static final String VIDEO_TABLE = "video";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_HISTORY = "history";
        public static final String COLUMN_ALLERGIES = "allergies";
        public static final String COLUMN_START = "start";
        public static final String COLUMN_END = "end";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_OCCURANCE = "occurance";
        public static final String COLUMN_VIDEO_LINK = "video_link";
        public static final String COLUMN_VIDEO_RATING = "ratings";
        public static final String COLUMN_VIDEO_NOTES = "notes";
    }

    private int PatientCounter = 0;
    PatientsDBHelper mDbHelper;

    public DBUtilities(Context context) {
        mDbHelper = new PatientsDBHelper(context);
    }

    public PatientsDBHelper getmDbHelper() {
        return mDbHelper;
    }

    public long addNewPatient(String name, int day, int month, int year, String history, String allergies) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_ID, PatientCounter);
        values.put(FeedEntry.COLUMN_NAME, name);
        values.put(FeedEntry.COLUMN_DATE, day);
        values.put(FeedEntry.COLUMN_MONTH, month);
        values.put(FeedEntry.COLUMN_YEAR, year);
        values.put(FeedEntry.COLUMN_HISTORY, history);
        values.put(FeedEntry.COLUMN_ALLERGIES, allergies);

        PatientCounter ++;

        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedEntry.PATIENTS_TABLE, null, values);
    }

    public long addNewVideo(String videoLink, String ratings, String notes) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_VIDEO_LINK, videoLink);
        values.put(FeedEntry.COLUMN_VIDEO_RATING, ratings);
        values.put(FeedEntry.COLUMN_VIDEO_NOTES, notes);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedEntry.VIDEO_TABLE, null, values);
    }

    public Cursor getPatientById(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry.COLUMN_ID, FeedEntry.COLUMN_NAME, FeedEntry.COLUMN_DATE,
                FeedEntry.COLUMN_MONTH, FeedEntry.COLUMN_YEAR, FeedEntry.COLUMN_HISTORY,
                FeedEntry.COLUMN_ALLERGIES, FeedEntry.COLUMN_START, FeedEntry.COLUMN_END,
                FeedEntry.COLUMN_DURATION, FeedEntry.COLUMN_OCCURANCE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.COLUMN_ID + " ASC";

        return db.query(
                FeedEntry.PATIENTS_TABLE,                 // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public int updatePatientById(int id, int start, int end, int duration, int occurance) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_START, start);
        values.put(FeedEntry.COLUMN_END, end);
        values.put(FeedEntry.COLUMN_DURATION, duration);
        values.put(FeedEntry.COLUMN_OCCURANCE, occurance);

// Which row to update, based on the title
        String selection = FeedEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { Integer.toString(id) };

        return db.update(
                FeedEntry.PATIENTS_TABLE,
                values,
                selection,
                selectionArgs);
    }

    public Cursor getAllPatient() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry.COLUMN_ID, FeedEntry.COLUMN_NAME, FeedEntry.COLUMN_DATE,
                FeedEntry.COLUMN_MONTH, FeedEntry.COLUMN_YEAR, FeedEntry.COLUMN_HISTORY,
                FeedEntry.COLUMN_ALLERGIES, FeedEntry.COLUMN_START, FeedEntry.COLUMN_END,
                FeedEntry.COLUMN_DURATION, FeedEntry.COLUMN_OCCURANCE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.COLUMN_ID + " ASC";

        return db.query(
                FeedEntry.PATIENTS_TABLE,                 // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public Cursor getVideoByLink(String link) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry.COLUMN_VIDEO_LINK, FeedEntry.COLUMN_VIDEO_RATING, FeedEntry.COLUMN_VIDEO_NOTES
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_VIDEO_LINK + " = ?";
        String[] selectionArgs = { link };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.COLUMN_ID + " ASC";

        return db.query(
                FeedEntry.VIDEO_TABLE,                 // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public Cursor getAllVideo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry.COLUMN_VIDEO_LINK, FeedEntry.COLUMN_VIDEO_RATING, FeedEntry.COLUMN_VIDEO_NOTES
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.COLUMN_ID + " ASC";

        return db.query(
                FeedEntry.VIDEO_TABLE,                 // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }
}
