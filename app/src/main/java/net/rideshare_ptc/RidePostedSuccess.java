package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button btnMainRet = (Button) findViewById(R.id.btnRPosSucMain);
        //TODO: Route the below ALL RIDES Button the Rides Page that doesn't exist yet
        Button btnAllRides = (Button) findViewById(R.id.btnRPosSucRides);
        btnMainRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainMenu.class);
                startActivity(intent);
            }
        });

    }
}