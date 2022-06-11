package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText userEmailInpt;
    EditText userPWInpt;
    String entEmailAdd;// user's typed in e-mail address
    String entPassword; // TODO: not yet implemented (password validation)
    String userInfoRes;
    User loggingUser = new User(); //create instance of User to capture deets of user attempting to login

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the user input data
        userEmailInpt = (EditText) findViewById(R.id.txtInputLoginEM);
        userPWInpt = (EditText) findViewById(R.id.txtInputLoginPW);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmitUser);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entEmailAdd = userEmailInpt.getText().toString();
                entPassword = userPWInpt.getText().toString();
                int SDK_INT = Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        getUserLoginData();
                        loginUserToLoginManager();
                    } catch (IOException e) {
                        e.printStackTrace();
                        startActivity(new Intent(LoginActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "User info: \n" + entEmailAdd));
                    }
                }
            }
        });
    }

    private void getUserLoginData() throws IOException {

        URL url = new URL("http://10.0.2.2:8080/login?eMail=" + entEmailAdd); //set URL
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connection
        con.setUseCaches(false);
        con.setRequestMethod("GET");//set request method

        //con.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM
        //con.setDoInput(true);
        con.connect();

        //read the response from input stream
        //TODO: Add error handling for any response code other than 200
        try{
            StringBuilder result = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            String strResponse = result.toString();
            int respCode = con.getResponseCode();
            //Map JSON Object to User Object
            ObjectMapper mapper = new ObjectMapper();
                    try {
                        loggingUser = mapper.readValue(strResponse, User.class);
                    }
                    catch (JsonGenerationException ge){
                        System.out.println(ge);
                    }
                    catch (JsonMappingException me) {
                        System.out.println(me);
                    }

        }
        catch (IOException e){
            startActivity(new Intent(LoginActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "User info: \n ERROR \n"+ e + entEmailAdd));
        }
    con.disconnect();

    }
    private void loginUserToLoginManager(){
        LoginManager mgr = LoginManager.getInstance();
        mgr.setLoggedInUsers(loggingUser);
        startActivity(new Intent(LoginActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "User added to LoginMgr List "+ mgr.getLoggedInUser().toString()));
        //get response status code
    }
}