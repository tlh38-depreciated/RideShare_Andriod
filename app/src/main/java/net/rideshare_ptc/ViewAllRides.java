package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.rideshare_ptc.adapters.RideAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewAllRides extends AppCompatActivity {

    TextView pickUpLoc;
    TextView dest;
    TextView rideDate; //used java.sql date
    Ride ride = new Ride();
    Button retToMenu;
    ListView allRidesView;
    ArrayList<Ride> Rides = new ArrayList<Ride>();

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_rides2);
        retToMenu = (Button) findViewById(R.id.btnAllRidesReturnMenu);


        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Rides = getRidesFromDB();
                RideAdapter rideAdapter = new RideAdapter(this, Rides);
                allRidesView = findViewById(R.id.ridesList);
                allRidesView.setAdapter(rideAdapter);

            } catch (IOException e) {
                e.printStackTrace();
                startActivity(new Intent(ViewAllRides.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "IO Error: " + e.toString()));
            }
            retToMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ViewAllRides.this, MainMenu.class));
                }
            });

            // ListView on item selected listener.
            allRidesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                    ActiveRide active_ride = ActiveRide.getInstance();
                    Ride selItem = (Ride) Rides.get(position); //
                    active_ride.setRideInfo(selItem);
                    String rideInfo = selItem.toString(); //getter method
                    String attributes = "";

                    Byte completed = selItem.getIsCompleted();
                    if (completed ==1){
                        attributes += "Ride Completed |";
                    }

                    Byte eating = selItem.getEating();
                    if (eating == 1){
                        attributes+= "Eating OK |";
                    }
                    Byte talking = selItem.getTalking();
                    if (talking == 1){
                        attributes +="Talking OK |";
                    }
                    Byte carseat = selItem.getCarseat();
                    if (carseat == 1){
                        attributes+="Has Carseat |";
                    }
                    Byte smoking = selItem.getSmoking();
                    if (smoking == 1){
                        attributes+="Smoking OK |";
                    }
                    selItem.setCost(selItem.calculateCost(selItem.duration));


                    //Toast.makeText(ViewAllRides.this, rideInfo, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ViewAllRides.this, RideDetailsActivity.class).putExtra("Ride Details", "Ride Details:"+selItem.toString()+"Attributes: "+attributes));
                }
            });
        }
    }

    private ArrayList<Ride> getRidesFromDB() throws IOException {
        ArrayList<Ride> retrievedRides = new ArrayList<Ride>();
        int resCode = 0;
        String strResponse = "";
        URL url = new URL("http://10.0.2.2:8080/viewRides");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestMethod("GET");

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
                retrievedRides = mapper.readValue(strResponse, new TypeReference<ArrayList<Ride>>(){});
            } catch (JsonGenerationException ge) {
                System.out.println(ge);
            } catch (JsonMappingException me) {
                System.out.println(me);
            }
        } catch (IOException e){
            startActivity(new Intent(ViewAllRides.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Connection Error: \n ERROR \n"+ e + resCode + strResponse));
        }

    con.disconnect();
        return retrievedRides;
    }
}