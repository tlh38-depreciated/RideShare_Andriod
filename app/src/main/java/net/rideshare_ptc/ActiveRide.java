package net.rideshare_ptc;

import java.util.ArrayList;
import java.util.List;

public class ActiveRide {

    //The purpose of this class is to manage the one and only logged in user.

    private static net.rideshare_ptc.ActiveRide obj = new net.rideshare_ptc.ActiveRide();
    //The variable that the singleton object will maintain (the list)
    Ride aRide = new Ride();

    public Ride getRideInfo(){
        return aRide;
    }

    public void setRideInfo(Ride newRide){
        aRide = newRide;
    }


    public static net.rideshare_ptc.ActiveRide getInstance(){  //this is how you access this singleton object throughout the program
        return obj;
        //call by "LoginManager mgr = LoginManager.getInstance();"
        //and then calling the functions of this instance to update it (like mgr.setLoggedInUsers(newUser));
        //reference: https://techvidvan.com/tutorials/java-singleton-class/
        //refereence: https://www.programiz.com/java-programming/singleton
    }

    private ActiveRide() {
        //this default constructor does nothing except create the Login manager
    }

}
