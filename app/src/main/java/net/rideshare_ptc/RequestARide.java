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

//Calendar imports
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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


import Distance_Matrix.DistanceMatrixResponseShell;




public class RequestARide extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

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
    TextView txtCalendar;
    LoginManager mgr = LoginManager.getInstance();
    User loggedInUser = mgr.getLoggedInUser();


    Boolean errorsFound = false;

    ApplicationInfo appInfo;
    static String apiKey;
    //Calendar Stuff

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;

    String dateTime;



    Button calendar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_aride);
        //get the objects (input fields) from the activity - intialize views
        txtCalendar = (TextView) findViewById(R.id.txtCalendar);
        rpickupLocI = (EditText) findViewById(R.id.inptReqPickUpLoc);
        rdestLocI = (EditText) findViewById(R.id.inptReqDestLoc);
        rsmokingI = (CheckBox) findViewById(R.id.ReqcheckBoxSmoking);
        reatingI = (CheckBox) findViewById(R.id.ReqcheckBoxEating);
        rtalkingI = (CheckBox) findViewById(R.id.ReqcheckBoxTalking);
        rcarseatI = (CheckBox) findViewById(R.id.ReqcheckBoxHasCarseat);
        calendar = (Button) findViewById(R.id.btnCalendar);
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

                                if (rpickupLocI.getText().toString().isEmpty() || rdestLocI.getText().toString().isEmpty() || dateTime.isEmpty()) {

                                    Toast.makeText(RequestARide.this, "Please complete all fields", Toast.LENGTH_SHORT).show();

                                } else {
                                    getRiderRideData();
                                    if (!errorsFound) { //if we have no errors (mainly used to ensure distance matrix properly worked)
                                        try {
                                            postRiderRideDataCreateRideInDB();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(RequestARide.this, RequestARide.this,year, month,day);
                        datePickerDialog.show();
                    }
                });
            }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            myYear = year;
            myday = dayOfMonth;
            myMonth = month + 1;
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR);
            minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(RequestARide.this, RequestARide.this, hour, minute, DateFormat.is24HourFormat(this));
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

            riderRidePost.setRideDate(dateTime.toString());

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

                        riderRidePost.setDuration(totalMins);
                        riderRidePost.setDistance(distance);
                        riderRidePost.setRideDate(dateTime.toString());

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
                //TODO: Add user error validation
                //Toast.makeText(RequestARide.this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            //startActivity(new Intent(DriverPostARide.this,RidePostedSuccess.class).putExtra("Success Ride Posted","Ride Posted: \n"+ rideJSON));
    }

        private void postRiderRideDataCreateRideInDB() throws IOException {

            URL url = new URL("http://10.0.2.2:8080/driverpostaride"); //set URL
            HttpURLConnection conWeb = (HttpURLConnection) url.openConnection(); //open connection
            conWeb.setRequestMethod("POST");//set request method
            conWeb.setRequestProperty("Content-Type", "application/json"); //set the request content-type header parameter
            conWeb.setDoOutput(true); //enable this to write content to the connection OUTPUT STREAM

            //Create the request body
            OutputStream os = conWeb.getOutputStream();
            byte[] input = rrideJSON.getBytes("utf-8");   // send the JSON as bye array input
            os.write(input, 0, input.length);

            //read the response from input stream
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

                    startActivity(new Intent(RequestARide.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Ride Successfully Posted: \n" + riderRidePost.toString()));
                    //get response status code

                } catch (IOException e) {
                    //TODO: Add error message for user
                    e.printStackTrace();
                }finally {
                    conWeb.disconnect();
                }
            }
    }

    /**
     * Takes the duration string given by the distance matrix api
     * and converts its value into a float that represents
     * the expected duration of the ride in seconds.
     *
     * Distance matrix strings are given in this format:
     * x hour(s) y minute(s) :::
     * Where plurals are added depending on the value of the hour/minute.
     * i.e. "1 hour 54 minutes" or "4 hours 1 minute"
     */
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
