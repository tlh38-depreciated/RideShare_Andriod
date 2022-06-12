package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

//Note: Meant to be shown only if there is an issue with the login attempt.
public class LoginWelcomeStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String successMsg = bundle.getString("Login Status");
        setContentView(R.layout.activity_login_welcome_status);
        TextView msg = (TextView) findViewById(R.id.txtLoginMessage);
        msg.setText(successMsg);
        TimerTask task = new TimerTask() {
            @Override
            public void run(){
                finish();
                startActivity(new Intent(LoginWelcomeStatusActivity.this,LoginActivity.class));
            }
        };
        Timer show5secs  = new Timer();
        show5secs.schedule(task,8000);

        Button btnRegister = (Button) findViewById(R.id.btnStatusToRegister);
        //TODO: Route the below ALL RIDES Button the Rides Page that doesn't exist yet
        Button btnLogin= (Button) findViewById(R.id.btnStatusToLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Route this to the same activity that will open the website in new window to register.
            }
        });
    }
}