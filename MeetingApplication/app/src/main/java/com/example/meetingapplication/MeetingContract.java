package com.example.meetingapplication;
import android.provider.BaseColumns;

public final class MeetingContract {

    private MeetingContract() {
    }

    public static class MeetingEntry implements BaseColumns {
        public static final String TABLE_NAME = "meetings";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AGENDA = "agenda";
    }
}
