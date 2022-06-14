package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button btnReqRide = (Button) findViewById(R.id.btnMenuReq);
        Button btnLogout = (Button) findViewById(R.id.btnMenuLogOut);
        Button btnPostRide  = (Button) findViewById(R.id.btnMenuPost);
        Button btnUserProf = (Button) findViewById(R.id.btnMenuViewProf);
//Buttons for MyRides, All Rides, and User Profile TBA

        btnPostRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DriverPostARide.class);
                startActivity(intent);
                }
        });

        btnReqRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestARide.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager mgr = LoginManager.getInstance();
                mgr.removeLoggedInUsers();
                //startActivity(new Intent(MainMenu.this,DriverOnlySplash.class).putExtra("Success Ride Posted","User Logged out: "+ mgr.getLoggedInUserList().toString()));
                startActivity(new Intent(MainMenu.this,MainActivity.class));
            }
        });
        //Testing button- Route updated to UserProfile
        btnUserProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainMenu.this, UserProfile.class));
            }
        });
        
    }
}