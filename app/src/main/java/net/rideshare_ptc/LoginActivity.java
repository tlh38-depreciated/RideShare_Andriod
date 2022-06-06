package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText userEmailInpt ;
    EditText userPWInpt;
    String entEmailAdd;// user's typed in e-mail address
    String entPassword; // TODO: not yet implemented (password validation)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the user input data
        userEmailInpt = (EditText) findViewById(R.id.txtInputLoginEM);
        userPWInpt = (EditText) findViewById(R.id.txtInputLoginPW);
        entEmailAdd = userEmailInpt.getText().toString();
        entPassword = userPWInpt.getText().toString();
        Button btnSubmit = (Button) findViewById(R.id.btnSubmitUser);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        int SDK_INT = Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            try {
                                User loginUser = new User(entEmailAdd);
                                URL url = new URL("http://10.0.2.2:8080/login+?eMail=" + entEmailAdd); //set URL w/param for user's email
                                HttpURLConnection con2 = (HttpURLConnection) url.openConnection(); //open connection

                                con2.setRequestMethod("GET");//set request method
                                con2.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
                                int respCode = con2.getResponseCode();
                                InputStream inputStream = con2.getInputStream();
                                con2.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String userInfo = bufferedReader.readLine(); // this should get the JSON Array with 1 JSON object
                                //TODO: map this JSON object to a User object and persist that user information throughout session.
                                //for now, just checking that can receive the JSON and deserialize it, and display user's First Name as welcome msg
                                //on top of menu

                                if (respCode == 200) {

                                    //If user creds ok, proceed to show the main menu.
                                    startActivity(new Intent(LoginActivity.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "User data retrieved: \n" + userInfo));
                                    //Intent intent = new Intent(view.getContext(), MainMenu.class);
                                    //startActivity(intent);
                                } else {
                                    startActivity(new Intent(LoginActivity.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "User data problem: \n" + userInfo));
                                }
                                con2.disconnect();  //disconnect
                            } catch (MalformedURLException mx) {
                            } catch (IOException ex) {
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                } catch (final Exception tex) {
                                    Log.i("---------------", "Exception in thread" + tex);
                                }
                            }

                        }
                    }

                } ;
            }
        });

    }

}