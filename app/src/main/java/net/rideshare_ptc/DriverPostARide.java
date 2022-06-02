package net.rideshare_ptc;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

public class DriverPostARide extends AppCompatActivity {
    Button drvRideSubmit;
    Ride driverRidePost;
    EditText pickupLocI;
    EditText destLocI ;
    EditText rideDateTimeI ;
    CheckBox smokingI ;
    CheckBox eatingI ;
    CheckBox talkingI ;
    CheckBox carseatI ;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_aride);
        //get the objects (input fields) from the activity - intialize views
        EditText pickupLocI = (EditText) findViewById(R.id.inputTxtDPPickUpLoc);
        EditText destLocI = (EditText) findViewById(R.id.inputTxtDPDestLoc);
        EditText rideDateTimeI = (EditText) findViewById(R.id.inputTxtDPDateTime); //this needs to be changed to a date picker
        CheckBox smokingI = (CheckBox) findViewById(R.id.checkBoxSmokingOK);
        CheckBox eatingI = (CheckBox) findViewById(R.id.checkBoxEatingOK);
        CheckBox talkingI = (CheckBox) findViewById(R.id.checkBoxTalkingOK);
        CheckBox carseatI = (CheckBox) findViewById(R.id.checkBoxHasCarseat);
        drvRideSubmit = (Button) findViewById(R.id.btnPostARide);


        drvRideSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (pickupLocI.getText().toString().isEmpty() || destLocI.getText().toString().isEmpty() || rideDateTimeI.getText().toString().isEmpty()) {
                    Toast.makeText(DriverPostARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                } else {
                    getDriverRideData();
                    try {
                        postDriverRideDataCreateRideInDB();
                        startActivity(new Intent(DriverPostARide.this,RidePostedSuccess.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
    private void getDriverRideData() {
        //get the data from the form and add to Ride object
        //cannot get an object mapper to work, trying construction JSON Object instead
        driverRidePost = new Ride();
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
}


    private void postDriverRideDataCreateRideInDB() throws IOException {
        //send data using Volley
        //url to which to post

        String url = "http://127.0.0.1:8080/driverpostaride";

        //convert the driverRidePost object to JSON and send to the controller.
        //Get a response back from the controller to display in the success message.
        //

    }


}
