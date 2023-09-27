package com.example.meetingapplication;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Meeting_info extends Activity {

    private DatePicker datePicker;
    private TextView agendaTextView;
    private MeetingDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_info);

        // Initialize views
        datePicker = findViewById(R.id.datePicker);
        agendaTextView = findViewById(R.id.agendaTextView);

        // Create the database helper instance
        dbHelper = new MeetingDbHelper(this);

        // Set onDateChanged listener for the date picker
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                displayMeetingInfo(year, monthOfYear, dayOfMonth);
            }
        });
    }

    private void displayMeetingInfo(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long selectedDateMillis = calendar.getTimeInMillis();

        // Query the database for meetings on the selected date
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                MeetingContract.MeetingEntry.COLUMN_AGENDA
        };

        String selection = MeetingContract.MeetingEntry.COLUMN_DATE + " = ?";
        String[] selectionArgs = {String.valueOf(selectedDateMillis)};

        Cursor cursor = db.query(
                MeetingContract.MeetingEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Meeting found on the selected date
            String agenda = cursor.getString(cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_AGENDA));
            agendaTextView.setText(agenda);
        } else {
            // No meeting found on the selected date
            agendaTextView.setText("");
            Toast.makeText(this, "No Meeting on this Date", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}