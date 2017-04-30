package hackinghealth.com.whatsapnea.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by paull on 2017-04-30.
 */

public class PatientsDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PROFILE =
            "CREATE TABLE " + DBUtilities.FeedEntry.PATIENTS_TABLE +
                    " (" + DBUtilities.FeedEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    DBUtilities.FeedEntry.COLUMN_NAME + " TEXT, " + DBUtilities.FeedEntry.COLUMN_DATE + " INTEGER, " +
                    DBUtilities.FeedEntry.COLUMN_MONTH + " INTEGER, " + DBUtilities.FeedEntry.COLUMN_YEAR + " INTEGER, " +
                    DBUtilities.FeedEntry.COLUMN_HISTORY + " TEXT, " + DBUtilities.FeedEntry.COLUMN_ALLERGIES + " TEXT, " +
                    DBUtilities.FeedEntry.COLUMN_START + " INTEGER, " + DBUtilities.FeedEntry.COLUMN_END + " INTEGER, " +
                    DBUtilities.FeedEntry.COLUMN_DURATION + " INTEGER, " + DBUtilities.FeedEntry.COLUMN_OCCURANCE + " INTEGER)";


    private static final String SQL_DELETE_PROFILE =
            "DROP TABLE IF EXISTS " + DBUtilities.FeedEntry.PATIENTS_TABLE;

    private static final String SQL_CREATE_VIDEO =
            "CREATE TABLE " + DBUtilities.FeedEntry.VIDEO_TABLE + " (" + DBUtilities.FeedEntry.COLUMN_VIDEO_LINK + " TEXT PRIMARY KEY, " +
                    DBUtilities.FeedEntry.COLUMN_VIDEO_RATING + " TEXT, " + DBUtilities.FeedEntry.COLUMN_VIDEO_NOTES + " TEXT)";

    private static final String SQL_DELETE_VIDEOS =
            "DROP TABLE IF EXISTS " + DBUtilities.FeedEntry.VIDEO_TABLE;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WhatsApnea.db";

    public PatientsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROFILE);
        db.execSQL(SQL_CREATE_VIDEO);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PROFILE);
        db.execSQL(SQL_DELETE_VIDEOS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
