package it.polito.mad.insane.lab2;

import java.util.List;

/**
 * Created by carlocaramia on 09/04/16.
 */
// Singleton class
public class DbApp {

    private static DbApp instance = null; // the only instance of this class
    private RestaurateurProfile profile;
    private List<Dish> dishes;
    private List<Booking> bookings;

    protected  DbApp()
    {

    }

    public static DbApp getInstance()
    {
        if(DbApp.instance == null)
            DbApp.instance = new DbApp();

        return DbApp.instance;
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
