package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RideDetailsActivity extends AppCompatActivity {


    TextView rideDetails;
    Ride ride = new Ride();
    Button returnToRides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        returnToRides = (Button) findViewById(R.id.btnRideRetRides);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String rideInformation = bundle.getString("Ride Details");
        TextView msg = (TextView) findViewById(R.id.txtViewRideDetail);
        msg.setText(rideInformation);

        returnToRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RideDetailsActivity.this, ViewAllRides.class));
            }
        });

    }
}