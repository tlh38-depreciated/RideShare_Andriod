package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    User loggedInUser;
    Byte isADriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        Button btnReturnHome = (Button) findViewById(R.id.btnReturnHome);
        Button btnCarInfo = (Button) findViewById(R.id.btnCarInfo);
        Button btnLogout = (Button) findViewById(R.id.btnMainMyRides);

        TextView fName = (TextView) findViewById(R.id.txtFirstName);
        TextView lName = (TextView) findViewById(R.id.txtLastName);
        TextView riderScore = (TextView) findViewById(R.id.txtRiderScore);
        TextView driverScore = (TextView) findViewById(R.id.txtDriverScore);
        TextView role = (TextView) findViewById(R.id.txtRole);


        // Get Logged in user's info
        LoginManager mgr = LoginManager.getInstance();
        loggedInUser = mgr.getLoggedInUser();
        isADriver = loggedInUser.getIsDriver();
        fName.setText(loggedInUser.getUserFName());
        lName.setText(loggedInUser.getUserLName());

        float numRiderScore = loggedInUser.getuRiderScore();
        String riderScoreTxt = Float.toString(numRiderScore);
        riderScore.setText(riderScoreTxt);

        float numDriverScore = loggedInUser.getuDriverScore();
        String DriverScoreTxt = Float.toString(numDriverScore);
        driverScore.setText(DriverScoreTxt);

        if(isADriver == 1){
            role.setText("Driver");
            btnCarInfo.setVisibility(View.VISIBLE);
        }
        else{
            role.setText("Rider");
            btnCarInfo.setVisibility(View.INVISIBLE);
        }

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, MainMenu.class));
            }
        });

        btnCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, DriverCarInfo.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager mgr = LoginManager.getInstance();
                mgr.removeLoggedInUsers();
                //startActivity(new Intent(MainMenu.this,DriverOnlySplash.class).putExtra("Success Ride Posted","User Logged out: "+ mgr.getLoggedInUserList().toString()));
                startActivity(new Intent(UserProfile.this,MainActivity.class));
            }
        });
    }
}