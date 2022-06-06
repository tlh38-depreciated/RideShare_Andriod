package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class RidePostedSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String successMsg = bundle.getString("Success Ride Posted");
        setContentView(R.layout.activity_ride_posted_success);
        TextView msg = (TextView) findViewById(R.id.txtPostSucc);
        msg.setText(successMsg);

    }
}