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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

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

public class DriverPostARide extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
    TextView txtCalendar;

    Boolean errorsFound = false;

    ApplicationInfo appInfo;
    static String apiKey;

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String dateTime;
    Button calendar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_aride);
        //get the objects (input fields) from the activity - intialize views
        pickupLocI = (EditText) findViewById(R.id.inptReqPickUpLoc);
        destLocI = (EditText) findViewById(R.id.inptReqDestLoc);
        calendar = (Button) findViewById(R.id.btnCalendar);
        txtCalendar = (TextView) findViewById(R.id.txtCalendar);
        smokingI = (CheckBox) findViewById(R.id.ReqcheckBoxSmoking);
        eatingI = (CheckBox) findViewById(R.id.ReqcheckBoxEating);
        talkingI = (CheckBox) findViewById(R.id.ReqcheckBoxTalking);
        carseatI = (CheckBox) findViewById(R.id.ReqcheckBoxHasCarseat);
        drvRideSubmit = (Button) findViewById(R.id.btnReqARide);
        retToMenu = (Button) findViewById(R.id.btnReqRideRetMenu);

        //Grab Meta Data from Android Manifest
        try{
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if(appInfo != null){
                apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            }
        }catch(PackageManager.NameNotFoundException e){
            //Meta data could not be grabbed
            return;
        }

        //Check if logged in user is a driver; if so, show post A ride, if not, re-route
        //with message.
        LoginManager mgr = LoginManager.getInstance();
        User loggedInUser = mgr.getLoggedInUser();
            if (loggedInUser.getIsDriver() ==1 ) {
                drvRideSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int SDK_INT = Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            if (pickupLocI.getText().toString().isEmpty() || destLocI.getText().toString().isEmpty() || dateTime.isEmpty()) {
                                Toast.makeText(DriverPostARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                            } else {
                                getDriverRideData();
                                if(!errorsFound){
                                    try {
                                        postDriverRideDataCreateRideInDB();
                                    } catch (IOException e) {
                                        //TODO: Add Error output for the user
                                        e.printStackTrace();
                                    }
                                }else{
                                    //TODO: Add error output for the user
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

                calendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(DriverPostARide.this, DriverPostARide.this,year, month,day);
                        datePickerDialog.show();
                    }
                });
            }
            else {
                //splash screen message then re-route to main menu. For now, just re-route to main
                //menu.
                startActivity(new Intent(DriverPostARide.this,DriverOnlySplash.class).putExtra("Success Ride Posted","Unavailable: not a driver \n This feature is limited to approved drivers only."));
                //startActivity(new Intent(DriverPostARide.this, MainMenu.class));
            }
        }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(DriverPostARide.this, DriverPostARide.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        String displayTime;
        String meridian = "AM";

        dateTime = (myYear + "-" +
                myMonth + "-" +
                myday + " " +
                myHour + ":" +
                myMinute);

        if (myHour > 12){
            myHour = myHour - 12;
            meridian = "PM";
        }
        displayTime = (myYear + "-" +
                myMonth + "-" +
                myday + " " +
                myHour + ":" +
                myMinute + " " + meridian);

        txtCalendar.setText(displayTime);
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
            driverRidePost.setRideDate(dateTime.toString());

            //Get Distance Using Google Maps API
            try{
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + pickupLocI.getText().toString() + "&destinations=" + destLocI.getText().toString() + "&units=imperial&key=" + apiKey); //set URL
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
                con.disconnect(); //disconnect from API
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
                        float totalMins = parseDistanceMatrixStringToFloat(durationVal);

                        //parse distance float
                        float distance = Float.parseFloat(distanceVal);

                        driverRidePost.setDuration(totalMins);
                        driverRidePost.setDistance(distance);

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
            }
            catch (IOException e) {
                //IO Exception thrown by the response when it's a bad request
                errorsFound = true;
            }

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
            HttpURLConnection conWeb = (HttpURLConnection) url.openConnection(); //open connection
            conWeb.setRequestMethod("POST");//set request method
            conWeb.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
            conWeb.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM

            //Create the request body
            OutputStream os = conWeb.getOutputStream();
            byte[] input = rideJSON.getBytes("utf-8");   // send the JSON as bye array input
            os.write(input, 0, input.length);

            //read the response from input stream
            //TODO: Add error handling for any response code other than 200

            if(conWeb.getResponseCode() >= 400)
            {
                //TODO: Add error output for the user
                conWeb.disconnect();
                return; //short circuit
            }else{
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conWeb.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    String strResponse = response.toString();

                    startActivity(new Intent(DriverPostARide.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Ride Successfully Posted: \n" + driverRidePost.toString()));
                    //get response status code

                } catch (IOException e) {
                    //TODO: Add error message for user
                    e.printStackTrace();
                }finally {
                    conWeb.disconnect();
                }
            }
    }

    public static float parseDistanceMatrixStringToFloat(String distanceMatrixString){
        String hours;
        float hoursParsed = 0.0f;
        String minutes;
        float minutesParsed = 0.0f;

        boolean isHourPlural = distanceMatrixString.contains("hours"); //flag to see if the hour value is plural
        boolean isMinutePlural = distanceMatrixString.contains("mins"); //flag to see if the minute is plural

        if(isHourPlural) //if the hours are plural
        {
            //get the hour value
            int indexOfHours = distanceMatrixString.indexOf("hours");
            hours = distanceMatrixString.substring(0, indexOfHours);
            hoursParsed = Float.parseFloat(hours);
        }else{
            if(distanceMatrixString.contains("hour"))
            {
                hoursParsed = 1.0f; //we have 1 hour
            }
            else{
                hoursParsed = 0.0f; //we don't actually have an hour
            }
        }

        if(isMinutePlural) //if the minutes are plural
        {
            int indexOfMinutes = distanceMatrixString.indexOf("mins");
            int indexOfHours = 0;
            int buffer = 0;
            if(distanceMatrixString.contains("hours")){
                indexOfHours = distanceMatrixString.indexOf("hours");
                buffer = 5;
            }else if(distanceMatrixString.contains("hour")){
                indexOfHours = distanceMatrixString.indexOf("hour");
                buffer = 4;
            }
            minutes = distanceMatrixString.substring(indexOfHours + buffer, indexOfMinutes);
            minutesParsed = Float.parseFloat(minutes);
        }else{
            if(distanceMatrixString.contains("min"))
            {
                minutesParsed = 1.0f; //we have 1 minute
            }
            else{
                minutesParsed = 0.0f; //we don't actually have a minute
            }
        }

        float totalSeconds = (hoursParsed * 3600) + (minutesParsed * 60);

        return totalSeconds;
    }


}
