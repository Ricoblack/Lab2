package it.polito.mad.insane.lab2;

import java.util.List;

/**
 * Created by carlocaramia on 09/04/16.
 */
public class DbApp {

    private RestaurateurProfile profile;
    private List<Dish> dishes;
    private List<Booking> bookings;

    public DbApp(RestaurateurProfile profile, List<Dish> dishes, List<Booking> bookings){
        this.profile=profile;
        this.dishes=dishes;
        this.bookings=bookings;
    }

    public RestaurateurProfile getProfile() {
        return profile;
    }

    public void setProfile(RestaurateurProfile profile) {
        this.profile = profile;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
