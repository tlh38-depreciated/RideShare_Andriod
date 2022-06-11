package net.rideshare_ptc;


import java.util.ArrayList;
import java.util.List;

//This is one big experiment I have never done before.
public class LoginManager {
    //The purpose of this class is to manage the one and only logged in user.
    //Plan is to create a list/ array which will hold the logged in user upon their login
    //and remove it when they log off, and ideally prevent other users from logging in if
    //the list already has the 1 user. (i.e. user in list is logged in until some condition
    //logs them off and removes them from the list).
    //This is creating a 'singleton' which creates this class and keeps the resulting object instance
    //accessible throughout the entire program, supposedly.
    //This class holds the variable list which will monitor and handle user login/logoff activity.

    //Trying this as a 'singleton' and creating the instance of this class at load time
    private static LoginManager obj = new LoginManager();
    //The variable that the singleton object will maintain (the list)
    List<User> loggedInUsers = new ArrayList<User>(); // size of this list should be 1 or 0 always.

    public List<User> getLoggedInUserList() {
        return loggedInUsers;
    }

    public User getLoggedInUser(){
        return this.getLoggedInUserList().get(0);
    }

    public String setLoggedInUsers(User loggedInUser) { //attempt login user taking the User obj as param.
        String response = "";
        if (this.loggedInUsers.size() ==1){
            response = "Current user must logout first"; //do not add to list, return msg
        }
        else if (this.loggedInUsers.size() ==0){
            loggedInUsers.add(loggedInUser);  //add the user to the list
            response = "User added to the list successfully"; //return success message
        }
        return response; //these returns are a way to return  a status for debugging
    }

    public String removeLoggedInUsers() { //attempt login user taking the User obj as param.
        String response = "";
        if (this.loggedInUsers.size() ==1){
            loggedInUsers.clear();
            response = "User successfully logged out"; //do not add to list, return msg
        }
        else if (this.loggedInUsers.size() ==0){
            //do nothing
            response = "No user is logged in"; //return success message
        }
        return response; //these returns are a way to return  a status for debugging
    }


    public static LoginManager getInstance(){  //this is how you access this singleton object throughout the program
        return obj;
        //call by "LoginManager mgr = LoginManager.getInstance();"
        //and then calling the functions of this instance to update it (like mgr.setLoggedInUsers(newUser));
        //reference: https://techvidvan.com/tutorials/java-singleton-class/
        //refereence: https://www.programiz.com/java-programming/singleton
    }

    private LoginManager(){
        //this default constructor does nothing except create the Login manager
    }

}
