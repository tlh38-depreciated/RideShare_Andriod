package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri.Builder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.rideshare_ptc.adapters.RideAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class MyRidesActivity extends AppCompatActivity {

        static ArrayList<Ride> Rides = new ArrayList<Ride>();
        TextView pickUpLoc;
        TextView dest;
        TextView rideDate; //used java.sql date
        Ride ride = new Ride();
        User loggedInUser;
        String lUserId;
        Button retMyRideToMenu;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_all_rides2);
            // Get MY RIDES Specific
            LoginManager mgr = LoginManager.getInstance();
            loggedInUser = mgr.getLoggedInUser();
            lUserId = loggedInUser.getUserID();
            retMyRideToMenu = (Button) findViewById(R.id.btnMyRidesReturnMenu);

            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                    try {
                        getUserRidesFromDB();
                        RideAdapter rideAdapter = new RideAdapter(this, Rides);
                        ListView allRidesView = findViewById(R.id.ridesList);
                        allRidesView.setAdapter(rideAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                        startActivity(new Intent(MyRidesActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "IO Error: " + e.toString()));
                    }

            }

        }


    private void getUserRidesFromDB() throws IOException {
        int resCode = 0;
        String strResponse = "";

        URL url = new URL("http://10.0.2.2:8080/viewRides?User="+lUserId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setUseCaches(false);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.connect();

        try {
            BufferedReader buffread = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = buffread.readLine()) != null) {
                stringBuilder.append(line);
            }
            buffread.close();
            strResponse = stringBuilder.toString();
            resCode = con.getResponseCode();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Rides = mapper.readValue(strResponse, new TypeReference<ArrayList<Ride>>(){});
            } catch (JsonGenerationException ge) {
                System.out.println(ge);
            } catch (JsonMappingException me) {
                System.out.println(me);
            }
            //startActivity(new Intent(MyRidesActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Request Sent:  \n"+"URL"+ url.toString()));
        } catch (IOException e){
            startActivity(new Intent(MyRidesActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Connection Error: \n ERROR \n"+"URL"+ url.toString()+"\n"+e));
        }

        con.disconnect();
    }

}