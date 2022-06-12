package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DriverOnlySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String successMsg = bundle.getString("Success Ride Posted");
        setContentView(R.layout.activity_driver_only_splash);
        TextView msg = (TextView) findViewById(R.id.txtCarDetails);
        msg.setText(successMsg);
        TimerTask task = new TimerTask() {
            @Override
            public void run(){
                finish();
                startActivity(new Intent(DriverOnlySplash.this,MainMenu.class));
            }
        };
        Timer show5secs  = new Timer();
        show5secs.schedule(task,10000);

        Button btnMainRet = (Button) findViewById(R.id.btnCarToMainMenu);
        //TODO: Route the below ALL RIDES Button the Rides Page that doesn't exist yet
        Button btnAllRides = (Button) findViewById(R.id.btnCarToMyRides);
        btnMainRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainMenu.class);
                startActivity(intent);
            }
        });

    }
}