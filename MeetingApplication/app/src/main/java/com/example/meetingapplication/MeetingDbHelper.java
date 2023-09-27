package com.example.meetingapplication;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class MeetingDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meeting.db";
    private static final int DATABASE_VERSION = 1;

    public MeetingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the meetings table
        String SQL_CREATE_MEETINGS_TABLE = "CREATE TABLE " + MeetingContract.MeetingEntry.TABLE_NAME + " ("
                + MeetingContract.MeetingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MeetingContract.MeetingEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + MeetingContract.MeetingEntry.COLUMN_AGENDA + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MEETINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + MeetingContract.MeetingEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean isMeetingLimitExceeded(long dateTimeMillis) {
        // Calculate the start and end time of the week
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTimeMillis);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long weekStartTimeMillis = calendar.getTimeInMillis();
        long weekEndTimeMillis = weekStartTimeMillis + (7 * 24 * 60 * 60 * 1000);

        // Query the database to count the number of meetings within the week
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {MeetingContract.MeetingEntry._ID};
        String selection = MeetingContract.MeetingEntry.COLUMN_DATE + " >= ? AND " + MeetingContract.MeetingEntry.COLUMN_DATE + " < ?";
        String[] selectionArgs = {String.valueOf(weekStartTimeMillis), String.valueOf(weekEndTimeMillis)};

        Cursor cursor = db.query(
                MeetingContract.MeetingEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int meetingCount = cursor.getCount();

        cursor.close();
        db.close();

        return meetingCount >= 4;
    }
}
