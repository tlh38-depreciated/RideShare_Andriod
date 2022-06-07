package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText userEmailInpt ;
    EditText userPWInpt;
    String entEmailAdd;// user's typed in e-mail address
    String entPassword; // TODO: not yet implemented (password validation)
    String userInfoRes;
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
                int SDK_INT = Build.VERSION.SDK_INT;
                if (SDK_INT >8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        getUserLoginData();
                    } catch (IOException e) {
                        e.printStackTrace();
                        startActivity(new Intent(LoginActivity.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "User info: \n" + entEmailAdd));
                    }
                }
            }
        });
        }

    private void getUserLoginData() throws IOException {

    URL url = new URL("http://10.0.2.2:8080/login?eMail="+entEmailAdd); //set URL
    HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connection
        con.setRequestMethod("GET");//set request method
        con.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
        con.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM
        con.connect();
        int respCode = con.getResponseCode();
    //Create the request body
    OutputStream os = con.getOutputStream();
    byte[] input = userInfoRes.getBytes("utf-8");   // send the JSON as bye array input
        os.write(input, 0, input.length);

    //read the response from input stream
    //TODO: Add error handling for any response code other than 200
        StringBuilder response = new StringBuilder();
        String strResponse = response.toString();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {

        String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        startActivity(new Intent(LoginActivity.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "User info: \n" + strResponse));
        //get response status code

        }
        catch (IOException e){
            startActivity(new Intent(LoginActivity.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "User info: \n" + respCode));
        }
    con.disconnect();

}
}