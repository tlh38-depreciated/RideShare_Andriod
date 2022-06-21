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
import java.util.concurrent.Executor;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


public class LoginActivity extends AppCompatActivity {
    EditText userEmailInpt;
    EditText userPWInpt;
    String entEmailAdd;// user's typed in e-mail address
    String entPassword; // TODO: not yet implemented (password validation)
    String userInfoRes;
    User loggingUser = new User(); //create instance of User to capture deets of user attempting to login
    Integer respCode;
    ImageButton fingerPButton;
    TextView loginMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the user input data
        userEmailInpt = (EditText) findViewById(R.id.txtInputLoginEM);
        userPWInpt = (EditText) findViewById(R.id.txtInputLoginPW);
        loginMsg = (TextView) findViewById(R.id.txt_LoginMethodMsg);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmitUser);

        //Biometric auth
        fingerPButton = (ImageButton) findViewById(R.id.imgBtn_fingerprintLogin);
        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                loginMsg.setText("You may use the fingerprint sensor to login");
                //loginMsg.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                loginMsg.setText("This device does not have a fingerprint sensor");
                fingerPButton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                loginMsg.setText("The biometric sensor is currently unavailable");
                fingerPButton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                loginMsg.setText("Your device doesn't have fingerprint saved, please check your security settings or login with password");
                fingerPButton.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                loginMsg.setText("Biometric authentication successful");
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Fingerprint Login")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        fingerPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ptcCheck = "@students.ptcollege.edu";

                entEmailAdd = userEmailInpt.getText().toString();
                entPassword = userPWInpt.getText().toString();
                int SDK_INT = Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    if (entEmailAdd.contains(ptcCheck) && loginMsg.getText().equals("Biometric authentication successful") || entPassword.equals("password1")) {
                        try {
                            getUserLoginData();
                            loginUserToLoginManager();

                        } catch (IOException e) {
                            e.printStackTrace();
                            startActivity(new Intent(LoginActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "User info: \n" + entEmailAdd));
                        }
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, LoginWelcomeStatusActivity.class).putExtra("Login Status", "This user-name does not appear to be registered. \n"));
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
            respCode = con.getResponseCode();
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
            startActivity(new Intent(LoginActivity.this, LoginWelcomeStatusActivity.class).putExtra("Login Status", "User info: \n ERROR \n"+ e + entEmailAdd));
        }
    con.disconnect();

    }
    private void loginUserToLoginManager(){
        if (respCode ==200) {
            LoginManager mgr = LoginManager.getInstance();
            mgr.setLoggedInUsers(loggingUser);
            startActivity(new Intent(LoginActivity.this, MainMenu.class));
        }
        else
        {
            startActivity(new Intent(LoginActivity.this, LoginWelcomeStatusActivity.class).putExtra("Login Status", "Error with login, try again."));
        }
    }
}