package net.rideshare_ptc;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class DriverPostARide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_aride);

        String pickupLoc;
        String destLoc;
        String driverID;
        String rideDateTime; // later handle as date  https://developer.android.com/guide/topics/ui/controls/pickers
        byte smoking = 0;
        byte eating = 0;
        byte talking = 0;
        byte carseat = 0;
        int carID;
        byte isTaken;
        byte isComplete;

        //get the objects (input fields) from the activity - intialize views
        EditText pickupLocI = (EditText) findViewById(R.id.inputTxtDPPickUpLoc);
        EditText destLocI = (EditText) findViewById(R.id.inputTxtDPDestLoc);
        EditText rideDateTimeI = (EditText) findViewById(R.id.inputTxtDPDateTime); //this needs to be changed to a date picker
        CheckBox smokingI = (CheckBox) findViewById(R.id.checkBoxSmokingOK);
        CheckBox eatingI = (CheckBox) findViewById(R.id.checkBoxEatingOK);
        CheckBox talkingI = (CheckBox) findViewById(R.id.checkBoxTalkingOK);
        CheckBox carseatI = (CheckBox) findViewById(R.id.checkBoxHasCarseat);
        Button drvRideSubmit = (Button) findViewById(R.id.btnPostARide);
        Ride driverRidePost = new Ride();
        JSONObject rideData = new JSONObject();

    }
}
        /*
        //Intent intent = getIntent();


        //submit button listener
        drvRideSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickupLocI.getText().toString().isEmpty() || destLocI.getText().toString().isEmpty() || rideDateTimeI.getText().toString().isEmpty()) {
                    Toast.makeText(DriverPostARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                } else {
                    getDriverRideData();
                    try {
                        postDriverRideDataCreateRideInDB();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        postDriverRideDataCreateRideInDB();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void postDriverRideDataCreateRideInDB() throws IOException {
        //send data using Volley
        //url to which to post

        String url = "http://127.0.0.1:8080/driverpostaride";
        loadingPB.setVisibility(View.VISIBLE);  // progress bar
        //create a new var for request queue
        RequestQueue queue = Volley.newRequestQueue(DriverPostARide.this);
        JsonObjectRequest sendRideData = new JsonObjectRequest(Request.Method.POST, url, rideData, new Response.Listener<JSONObject>() { //need to make sure the backend sends an object as resp.

            @Override
            public void onResponse(JSONObject response) { //get the response and display in a toast
                Toast.makeText(DriverPostARide.this, "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Toast.makeText(DriverPostARide.this, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        // add the request to the queue
        queue.add(sendRideData);


    }
    private void getDriverRideData() {
        //get the data from the form and add to Ride object
        //cannot get an object mapper to work, trying construction JSON Object instead
        /*
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


//     values as vars
        pickupLoc = pickupLocI.getText().toString();
        destLoc = destLocI.getText().toString();
        rideDateTime = rideDateTimeI.getText().toString();
        if (smokingI.isChecked()){
            smoking = ((byte) 1);
        };
        if (talkingI.isChecked()){
            talking = ((byte) 1);
        };
        if (carseatI.isChecked()){
            carseat = ((byte) 1);
        };
        if (eatingI.isChecked()){
            eating = ((byte) 1);
        };
        String driverID = "4816c6dd-8f03-470e-9aa2-c711eb579e7a";
        int carID;
        byte isTaken =0;
        byte isComplete =0;
        try {
            rideData.put("pickUpLoc", pickupLoc);
            rideData.put("dest", destLoc);
            rideData.put("rideDate", rideDateTime);
            rideData.put("smoking",smoking);
            rideData.put("talking",talking);
            rideData.put("eating",eating);
            rideData.put("carseat",carseat);
        }
        catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(DriverPostARide.this, "JSON Data mapping error:  /n" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

}
*/