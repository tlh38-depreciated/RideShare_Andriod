package net.rideshare_ptc;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Ride {
        int rideID;
        String carID;
        String pickUpLoc;
        String dest;
        float duration;
        float distance;
        float cost;
        String driverID;
        String riderID;
        String driverScore;
        String riderScore;
        String rideDate; //used java.sql date
        Byte smoking;
        Byte eating;
        Byte talking;
        Byte carseat;
        Byte isTaken;
        Byte isCompleted;
        String costPattern = "###,###.##";
        DecimalFormat costFormat = new DecimalFormat(costPattern);
        String timePattern = "##.##";
        DecimalFormat timeFormat = new DecimalFormat(costPattern);


        public int getRideID() {
            return rideID;
        }


        public void setRideID(int rideID) {
            this.rideID = rideID;
        }


        public String getPickUpLoc() {
            return pickUpLoc;
        }


        public void setPickUpLoc(String pickUpLoc) {
            this.pickUpLoc = pickUpLoc;
        }


        public String getDest() {
            return dest;
        }


        public void setDest(String dest) {
            this.dest = dest;
        }


        public float getDuration() {
            return duration;
        }


        public void setDuration(float duration) {
            this.duration = duration;
        }


        public float getDistance() {
            return distance;
        }


        public void setDistance(float distance) {
            this.distance = distance;
        }


        public float getCost() {
            return cost;
        }


        public void setCost(float cost) {
            this.cost = cost;
        }


        public String getDriverID() {
            return driverID;
        }


        public void setDriverID(String driverID) {
            this.driverID = driverID;
        }


        public String getRiderID() {
            return riderID;
        }


        public void setRiderID(String riderID) {
            this.riderID = riderID;
        }


        public String getDriverScore() {
            return driverScore;
        }


        public void setDriverScore(String driverScore) {
            this.driverScore = driverScore;
        }


        public String getRiderScore() {
            return riderScore;
        }


        public void setRiderScore(String riderScore) {
            this.riderScore = riderScore;
        }


        public String getRideDate() {
            return rideDate;
        }


        public void setRideDate(String rideDate) {
            this.rideDate = rideDate;
        }


        public Byte getSmoking() {
            return smoking;
        }


        public void setSmoking(Byte smoking) {
            this.smoking = smoking;
        }


        public Byte getEating() {
            return eating;
        }


        public void setEating(Byte eating) {
            this.eating = eating;
        }


        public Byte getTalking() {
            return talking;
        }


        public void setTalking(Byte talking) {
            this.talking = talking;
        }


        public Byte getCarseat() {
            return carseat;
        }


        public void setCarseat(Byte carseat) {
            this.carseat = carseat;
        }


        public Byte getIsTaken() {
            return isTaken;
        }


        public void setIsTaken(Byte isTaken) {
            this.isTaken = isTaken;
        }


        public Byte getIsCompleted() {
            return isCompleted;
        }


        public void setIsCompleted(Byte isCompleted) {
            this.isCompleted = isCompleted;
        }

        public String getCarID() {
            return carID;
        }

        public void setCarID(String carID) {
            this.carID = carID;
        }


        public Ride(String pickUpLoc, String dest, String driverID,String rideDate, Byte smoking, Byte eating, Byte talking,
                    Byte carseat) {

            this.pickUpLoc = pickUpLoc;
            this.dest = dest;
            this.driverID = driverID;
            this.rideDate = rideDate;
            this.smoking = smoking;
            this.eating = eating;
            this.talking = talking;
            this.carseat = carseat;

        }


        public Ride() {
        }
        public float calculateCost(float duration)
        {

            float mpg = 25.7f; //nation average MPG of light-duty vehicles www.greencarcongress.com/2021/03/20210316-epatrends.html#:~:text=16%20March%202021,light%2Dduty%20vehicle%20fuel%20economy.
            float avgGallon = 4.37f; //source as of May 14, 2022: https: //www.chooseenergy.com/data-center/cost-of-driving-by-state/
                final float staticCost = 5.00f;
            float Cost = (((distance / mpg) * avgGallon) + staticCost) ;
            float finalCost = (float) (Math.round(Cost*2)/2.0);
            return finalCost;
        }


        @Override
        public String toString() {
            return "\n  Pick Up: " + pickUpLoc + "\n  Destination: " + dest + "\n  Date and Time: " + rideDate+ "\n  Duration (hours): " + timeFormat.format((duration/3600)) + "\n  Distance (miles): " + distance + "\n  Cost: $" + String.valueOf(costFormat.format(cost))+"\n";
        }

    }


