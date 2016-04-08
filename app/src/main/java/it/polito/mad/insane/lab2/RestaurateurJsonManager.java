package it.polito.mad.insane.lab2;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by carlocaramia on 08/04/16.
 */

public class RestaurateurJsonManager {

    private RestaurateurProfile profile;
    private DailyOffer dailyOffer;
    private ArrayList<Booking> bookings;

    public RestaurateurJsonManager(){

    }

    public void setRestaurateurProfile(RestaurateurProfile profile){
        this.profile=profile;
    }

    public RestaurateurProfile getRestaurateurProfile(){
        return this.profile;
    }


    public DailyOffer getDailyOffer() {
        return dailyOffer;
    }

    public void setDailyOffer(DailyOffer dailyOffer) {
        this.dailyOffer = dailyOffer;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }
}


