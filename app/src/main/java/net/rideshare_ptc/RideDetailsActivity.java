package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RideDetailsActivity extends AppCompatActivity {


    TextView rideDetails;
    Ride ride = new Ride();
    Button returnToMenu;
    Button driverDetails;
    Button riderDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        returnToMenu = (Button) findViewById(R.id.btnRideRetMenu);
        driverDetails = (Button) findViewById(R.id.btnViewRideDriver);
        riderDetails = (Button) findViewById(R.id.btnViewRideRider);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String rideInformation = bundle.getString("Ride Details");
        TextView msg = (TextView) findViewById(R.id.txtViewRideDetail);
        msg.setMovementMethod(new ScrollingMovementMethod());
        msg.setText(rideInformation);

        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RideDetailsActivity.this, MainMenu.class));
            }
        });

        driverDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActiveRide active_ride = ActiveRide.getInstance();
                //String rideDriver = active_ride.getRideInfo().getDriverID();
                //TODO: case handling when not assigned
                startActivity(new Intent(RideDetailsActivity.this, rideRiderDriverProfile.class));
            }
        });
        riderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveRide active_ride = ActiveRide.getInstance();
                String rideRider  =  active_ride.getRideInfo().getRiderID();
                //TODO: case handling when not assigned
                startActivity(new Intent(RideDetailsActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", rideRider + "just testing that I can keep this data, will update to driver profile" ));
            }
        });



    }
}