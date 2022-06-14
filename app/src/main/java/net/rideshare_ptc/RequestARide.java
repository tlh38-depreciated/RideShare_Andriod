package net.rideshare_ptc;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Distance_Matrix.DistanceMatrixResponseShell;


public class RequestARide extends AppCompatActivity {
    Button riderReq;
    Button retToMenu;
    Ride riderRidePost;
    EditText rpickupLocI;
    EditText rdestLocI;
    EditText rrideDateTimeI;
    CheckBox rsmokingI;
    CheckBox reatingI;
    CheckBox rtalkingI;
    CheckBox rcarseatI;
    String rrideJSON;
    LoginManager mgr = LoginManager.getInstance();
    User loggedInUser = mgr.getLoggedInUser();

    Boolean errorsFound = false;

    ApplicationInfo appInfo;
    static String apiKey;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_aride);
        //get the objects (input fields) from the activity - intialize views
        rpickupLocI = (EditText) findViewById(R.id.inptReqPickUpLoc);
        rdestLocI = (EditText) findViewById(R.id.inptReqDestLoc);
        rrideDateTimeI = (EditText) findViewById(R.id.inptReqDateTime); //this needs to be changed to a date picker
        rsmokingI = (CheckBox) findViewById(R.id.ReqcheckBoxSmoking);
        reatingI = (CheckBox) findViewById(R.id.ReqcheckBoxEating);
        rtalkingI = (CheckBox) findViewById(R.id.ReqcheckBoxTalking);
        rcarseatI = (CheckBox) findViewById(R.id.ReqcheckBoxHasCarseat);
        riderReq = (Button) findViewById(R.id.btnReqARide);
        retToMenu = (Button) findViewById(R.id.btnReqRideRetMenu);

        //Grab Meta Data from Android Manifest
        try{
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if(appInfo != null){
                apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            //Meta data could not be grabbed
            return;
        }


        riderReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int SDK_INT = Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            errorsFound = false;

                            if (rpickupLocI.getText().toString().isEmpty() || rdestLocI.getText().toString().isEmpty() || rrideDateTimeI.getText().toString().isEmpty()) {
                                Toast.makeText(RequestARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                            } else {
                                getRiderRideData();
                                if(!errorsFound){ //if we have no errors (mainly used to ensure distance matrix properly worked)
                                    try {
                                        postRiderRideDataCreateRideInDB();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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

        private void getRiderRideData() {
            //get the data from the form and add to Ride object
            //cannot get an object mapper to work, trying construction JSON Object instead
            //TODO: Add calculations for duration, distance, cost similarly to how handled in webapp
            riderRidePost = new Ride();
            riderRidePost.setRiderID(loggedInUser.getUserID());
            riderRidePost.setRiderScore(loggedInUser.getuRiderScore());
            riderRidePost.setCarseat((byte)0);
            riderRidePost.setTalking((byte)0);
            riderRidePost.setEating((byte)0);
            riderRidePost.setSmoking((byte)0);
            riderRidePost.setPickUpLoc(rpickupLocI.getText().toString());
            riderRidePost.setDest(rdestLocI.getText().toString());
            riderRidePost.setRideDate(rrideDateTimeI.getText().toString());

            //Get Distance Using Google Maps API
            try{
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + rpickupLocI.getText().toString() + "&destinations=" + rdestLocI.getText().toString() + "&units=imperial&key=" + apiKey); //set URL
                HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connection
                con.setUseCaches(false);
                con.setRequestMethod("GET");//set request method
                con.connect();

                //get the result
                StringBuilder result = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                String strResponse = result.toString();
                Integer respCode = con.getResponseCode();
                con.disconnect();
                if(respCode == 200){
                    //Map JSON Object to User Object
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        DistanceMatrixResponseShell response = mapper.readValue(strResponse, DistanceMatrixResponseShell.class);

                        String distanceVal = response.getRows().get(0).getElements().get(0).getDistance().getText();
                        String durationVal = response.getRows().get(0).getElements().get(0).getDuration().getText();

                        //get rid of "mi" from the text
                        distanceVal = distanceVal.replace("mi", "");

                        //reformat the duration into minutes
                        int timeIndex = durationVal.indexOf(" hour");
                        String hour = durationVal.substring(0, timeIndex);
                        Float hourFl = Float.parseFloat(hour);
                        hourFl *= 60; //convert hourFl to mins

                        int lastHrIndex = timeIndex + 6;
                        int minIndex = durationVal.indexOf(" mins");
                        String mins = durationVal.substring(lastHrIndex, minIndex);
                        Float minFl = Float.parseFloat(mins);

                        Float totalMins = hourFl + minFl;
                        Float distance = Float.parseFloat(distanceVal);

                        riderRidePost.setDuration(totalMins);
                        riderRidePost.setDistance(distance);

                    }
                    catch (JsonGenerationException ge){
                        //System.out.println(ge);
                        errorsFound = true;
                    }
                    catch (JsonMappingException me) {
                        //System.out.println(me);
                        errorsFound = true;
                    }
                    catch(NumberFormatException nfe){
                        //System.out.println(nfe);
                        errorsFound = true;
                    }catch(NullPointerException npe){
                        //System.out.println(npe);
                        errorsFound = true;
                    }
                }

            }catch (IOException e) {
                //IO Exception thrown by the response when it's a bad request
                errorsFound = true;
            }

            if (rsmokingI.isChecked()) {
                riderRidePost.setSmoking((byte) 1);
            }
            if (rtalkingI.isChecked()) {
                riderRidePost.setTalking((byte) 1);
            }
            if (rcarseatI.isChecked()) {
                riderRidePost.setCarseat((byte) 1);
            }
            if (reatingI.isChecked()) {
                riderRidePost.setEating((byte) 1);
            }

            riderRidePost.setIsCompleted((byte) 0);
            riderRidePost.setIsTaken((byte) 0);
            //map to JSON
            ObjectMapper mapper = new ObjectMapper();
            try {
                rrideJSON = mapper.writeValueAsString(riderRidePost);
            } catch (JsonProcessingException e) {
                Toast.makeText(RequestARide.this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            //startActivity(new Intent(DriverPostARide.this,RidePostedSuccess.class).putExtra("Success Ride Posted","Ride Posted: \n"+ rideJSON));
    }

        private void postRiderRideDataCreateRideInDB() throws IOException {

            URL url = new URL("http://10.0.2.2:8080/driverpostaride"); //set URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connection
            con.setRequestMethod("POST");//set request method
            con.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
            con.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM

            //Create the request body
            OutputStream os = con.getOutputStream();
            byte[] input = rrideJSON.getBytes("utf-8");   // send the JSON as bye array input
            os.write(input, 0, input.length);

            //read the response from input stream
            if(con.getResponseCode() >= 400)
            {
                errorsFound = true;
                return; //short circuit
            }else{
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    String strResponse = response.toString();

                    startActivity(new Intent(RequestARide.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Ride Successfully Posted: \n" + riderRidePost.toString()));
                    //get response status code

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            con.disconnect();
    }
}
