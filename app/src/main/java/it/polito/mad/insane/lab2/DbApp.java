package it.polito.mad.insane.lab2;

import java.util.ArrayList;
import java.util.Date;
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

    private  DbApp()
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

    public void fillDbApp(){


        //CARICAMENTO DATI PROFILE
        this.profile=new RestaurateurProfile("Pizza-Pazza","corso duca degli abruzzi, 10","PoliTo","Pizza","Venite a provare la pizza più gustosa di Torino",new Date(),new Date(),"Chiusi la domenica","Bancomat","Wifi-free");

        //CARICAMENTO DATI DISHES
        this.dishes=new ArrayList<Dish>();
        Dish dish1 = new Dish("1","Margherita", "La classica delle classiche", null, 5.50, 100);
        dishes.add(dish1);

        Dish dish2 = new Dish("2","Marinara", "Occhio all'aglio!", null, 2.50, 200);
        dishes.add(dish2);

        Dish dish3 = new Dish("3","Tonno", "Il gusto in una parola", null, 3.50, 300);
        dishes.add(dish3);

        Dish dish4 = new Dish("4","Politecnico", "Solo per veri ingegneri", null, 4.50, 104);
        dishes.add(dish4);

        Dish dish5 = new Dish("5","30L", "Il nome dice tutto: imperdibile", null, 5.55, 150);
        dishes.add(dish5);

        Dish dish6 = new Dish("6","Hilary", "Dedicato a una vecchia amica", null, 5.55, 150);
        dishes.add(dish6);


        //CARICAMENTO DATI BOOKINGS
        this.bookings=new ArrayList<Booking>();

        Booking newBooking = new Booking();
        newBooking.setID("1");
        ArrayList<Dish> elenco1=new ArrayList<Dish>();
        elenco1.add(this.dishes.get(2));
        elenco1.add(this.dishes.get(5));
        newBooking.setDishes(elenco1);
        bookings.add(newBooking);

        Booking newBooking2 = new Booking();
        newBooking2.setID("2");
        ArrayList<Dish> elenco2=new ArrayList<Dish>();
        elenco2.add(this.dishes.get(3));
        newBooking.setDishes(elenco2);
        bookings.add(newBooking2);


    }
}
