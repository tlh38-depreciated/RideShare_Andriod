package net.rideshare_ptc;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DriverPostARide extends AppCompatActivity {
    Button drvRideSubmit;
    Button retToMenu;
    Ride driverRidePost;
    EditText pickupLocI;
    EditText destLocI;
    EditText rideDateTimeI;
    CheckBox smokingI;
    CheckBox eatingI;
    CheckBox talkingI;
    CheckBox carseatI;
    String rideJSON;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_aride);
        //get the objects (input fields) from the activity - intialize views
        pickupLocI = (EditText) findViewById(R.id.inputTxtDPPickUpLoc);
        destLocI = (EditText) findViewById(R.id.inputTxtDPDestLoc);
        rideDateTimeI = (EditText) findViewById(R.id.inputTxtDPDateTime); //this needs to be changed to a date picker
        smokingI = (CheckBox) findViewById(R.id.checkBoxSmokingOK);
        eatingI = (CheckBox) findViewById(R.id.checkBoxEatingOK);
        talkingI = (CheckBox) findViewById(R.id.checkBoxTalkingOK);
        carseatI = (CheckBox) findViewById(R.id.checkBoxHasCarseat);
        drvRideSubmit = (Button) findViewById(R.id.btnPostARide);
        retToMenu = (Button) findViewById(R.id.btnPostARideRetMenu);

        drvRideSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int SDK_INT = Build.VERSION.SDK_INT;
                if (SDK_INT >8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    if (pickupLocI.getText().toString().isEmpty() || destLocI.getText().toString().isEmpty() || rideDateTimeI.getText().toString().isEmpty()) {
                        Toast.makeText(DriverPostARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                    } else {
                        getDriverRideData();
                        try {
                            postDriverRideDataCreateRideInDB();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        });

        retToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainMenu.class);
                startActivity(intent);
            }

        });


    }
    private void getDriverRideData() {
        //get the data from the form and add to Ride object
        //cannot get an object mapper to work, trying construction JSON Object instead
        //TODO: Enhance input validation- calendar selector for date/time, implement Google API for locations
        //TODO: Add calculations for duration, distance, cost similarly to how handled in webapp
        driverRidePost = new Ride();
        driverRidePost.setCarseat((byte)0);
        driverRidePost.setTalking((byte)0);
        driverRidePost.setEating((byte)0);
        driverRidePost.setSmoking((byte)0);
        driverRidePost.setPickUpLoc(pickupLocI.getText().toString());
        driverRidePost.setDest(destLocI.getText().toString());
        driverRidePost.setRideDate(rideDateTimeI.getText().toString());
        if (smokingI.isChecked()) {
            driverRidePost.setSmoking((byte) 1);
        }
        ;
        if (talkingI.isChecked()) {
            driverRidePost.setTalking((byte) 1);
        }
        ;
        if (carseatI.isChecked()) {
            driverRidePost.setCarseat((byte) 1);
        }
        ;
        if (eatingI.isChecked()) {
            driverRidePost.setEating((byte) 1);
        }
        ;
        driverRidePost.setDriverID("4816c6dd-8f03-470e-9aa2-c711eb579e7a");
        driverRidePost.setIsCompleted((byte) 0);
        driverRidePost.setIsTaken((byte) 0);
        //map to JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            rideJSON = mapper.writeValueAsString(driverRidePost);
        } catch (JsonProcessingException e) {
            Toast.makeText(DriverPostARide.this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        //startActivity(new Intent(DriverPostARide.this,RidePostedSuccess.class).putExtra("Success Ride Posted","Ride Posted: \n"+ rideJSON));
}

    private void postDriverRideDataCreateRideInDB() throws IOException {


        URL url = new URL("http://10.0.2.2:8080/driverpostaride"); //set URL
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connection
        con.setRequestMethod("POST");//set request method
        con.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
        con.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM

        //Create the request body
        OutputStream os = con.getOutputStream();
        byte[] input = rideJSON.getBytes("utf-8");   // send the JSON as bye array input
        os.write(input, 0, input.length);

        //read the response from input stream
                //TODO: Add error handling for any response code other than 200

                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        String strResponse = response.toString();

                    startActivity(new Intent(DriverPostARide.this, RidePostedSuccess.class).putExtra("Success Ride Posted", "Ride Successfully Posted: \n" + driverRidePost.toString()));
                    //get response status code

                }

        con.disconnect();

    }
}
