package com.example.meetingapplication;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MeetingScheduleActivity extends Activity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText agendaEditText;
    private Button scheduleButton;
    private MeetingDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_schedule_activity);

        // Initialize views
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        agendaEditText = findViewById(R.id.agendaEditText);
        scheduleButton = findViewById(R.id.scheduleButton);

        // Create the database helper instance
        dbHelper = new MeetingDbHelper(this);

        // Set onClick listener for the schedule button
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleMeeting();
            }
        });
    }

    private void scheduleMeeting() {
        // Get the selected date and time
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        long dateTimeMillis = calendar.getTimeInMillis();

        // Get the meeting agenda
        String agenda = agendaEditText.getText().toString().trim();

        // Check if more than 4 meetings are scheduled for the selected week
        if (dbHelper.isMeetingLimitExceeded(dateTimeMillis)) {
            Toast.makeText(this, "Cannot schedule meeting. Meeting limit exceeded for the week.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the meeting into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MeetingContract.MeetingEntry.COLUMN_DATE, dateTimeMillis);
        values.put(MeetingContract.MeetingEntry.COLUMN_AGENDA, agenda);

        long newRowId = db.insert(MeetingContract.MeetingEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Meeting scheduled successfully.", Toast.LENGTH_SHORT).show();
            clearForm();
        } else {
            Toast.makeText(this, "Failed to schedule meeting.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void clearForm() {
        agendaEditText.setText("");
    }
}
