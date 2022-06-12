package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DriverCarInfo extends AppCompatActivity {
    User loggedInUser;
    String userEmailAdd;
    Byte isADriver;
    Byte hasActiveCar;
    Car usersCar = new Car();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_info);
        Button btnMainMenu = (Button) findViewById(R.id.btnCarToMainMenu);
        //TODO: route this button to myRides page
        Button btnMyRides = (Button) findViewById(R.id.btnCarToMyRides);
        //The below textviews will be populated with user's car data or, if none exists,
        TextView carDetails = (TextView) findViewById(R.id.txtCarDetails);
        TextView userName = (TextView) findViewById(R.id.txtCarUserName);

        //Get Logged in user's info
        LoginManager mgr = LoginManager.getInstance();
        loggedInUser = mgr.getLoggedInUser();
        userEmailAdd = loggedInUser.getUserEmail();
        isADriver = loggedInUser.getIsDriver();
        String fName = loggedInUser.getUserFName();
        userName.setText(fName);
        if (isADriver ==1){
            try {
                getCarData();
                    if (hasActiveCar ==1) {
                        carDetails.setText(usersCar.toString());
                    }
                    else {
                        carDetails.setText("User has no active registered cars. \n Driver user cannot accept this any rides at this time.");
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            carDetails.setText("This user is not a registered driver. \n No car details available");
        }


        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainMenu.class);
                startActivity(intent);
            }
        });

    }

    private void getCarData() throws IOException {

        URL url = new URL("http://10.0.2.2:8080/car?eMail=" + userEmailAdd); //set URL
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
                usersCar = mapper.readValue(strResponse, Car.class);
                hasActiveCar = usersCar.getCarIsActive();
            }
            catch (JsonGenerationException ge){
                System.out.println(ge);
            }
            catch (JsonMappingException me) {
                System.out.println(me);
            }

        }
        catch (IOException e){
            startActivity(new Intent(DriverCarInfo.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "User info: \n ERROR \n"+ e + usersCar.toString()));
        }
        con.disconnect();

    }
}