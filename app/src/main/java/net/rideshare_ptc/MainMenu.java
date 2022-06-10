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
        Button btnLogin = (Button) findViewById(R.id.btnMenuPost);
        Button btnLogout = (Button) findViewById(R.id.btnMenuLogOut);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DriverPostARide.class);
                startActivity(intent);
                }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                LoginManager mgr = LoginManager.getInstance();
                Integer numUsersLoggedin = mgr.getLoggedInUserList().size();
                btnLogout.setText(numUsersLoggedin.toString());//just checking that my login manager works
                //if this turns to 1, that is good! woooo it worked...will
                //fix and implement the logout tomorrow.
            }
        });
        
    }
}