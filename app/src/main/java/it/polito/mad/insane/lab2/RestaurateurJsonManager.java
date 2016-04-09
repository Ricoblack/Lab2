package it.polito.mad.insane.lab2;

import java.util.List;

/**
 * Created by carlocaramia on 08/04/16.
 */

public class RestaurateurJsonManager {

    private RestaurateurProfile profile;
    private List<Dish> dishes;
    private List<Booking> bookings;

    public RestaurateurJsonManager(){

    }

    public void setRestaurateurProfile(RestaurateurProfile profile){
        this.profile=profile;
    }

    public RestaurateurProfile getRestaurateurProfile(){
        return this.profile;
    }


    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}


