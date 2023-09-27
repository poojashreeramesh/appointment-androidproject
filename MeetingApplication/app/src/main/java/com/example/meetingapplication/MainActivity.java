package com.example.meetingapplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button scheduleButton;
    private Button Meetinginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        scheduleButton = findViewById(R.id.scheduleButton);
        Meetinginfo=findViewById(R.id.meetinginfo);

        // Set onClick listener for the schedule button
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMeetingScheduleActivity();
            }
        });
        Meetinginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openMeetingInfoActivity();}
        });
    }

    private void openMeetingScheduleActivity() {
        Intent intent = new Intent(this, MeetingScheduleActivity.class);
        startActivity(intent);
    }
    private void openMeetingInfoActivity(){
        Intent intent=new Intent(this,Meeting_info.class);
        startActivity(intent);
    }
}
