package it.polito.mad.insane.lab2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by carlocaramia on 08/04/16.
 */
// Singleton Class
public class RestaurateurJsonManager {

    private static RestaurateurJsonManager instance = null;
    private static DbApp dbApp;
    private Context myContext;

    public static RestaurateurJsonManager getInstance(Context myContext)
    {
        if(RestaurateurJsonManager.instance == null)
            RestaurateurJsonManager.instance = new RestaurateurJsonManager(myContext);

        return RestaurateurJsonManager.instance;
    }
    private RestaurateurJsonManager(Context myContext)
    {
        RestaurateurJsonManager.dbApp = new DbApp();
        this.myContext=myContext;

        //Se l'app è aperta per la prima volta non c'è un json, qui lo creo e lo riempio con dati random
        //Altrimenti recupero il json salvato

        if(getDbApp()==null){
            dbApp.fillDbApp();
            saveDbApp();
        }
        else {
            //recupero json
            this.dbApp=getDbApp();

        }

    }

    public String getJsonString(){
        //ritorna la stringa del Json

        if(dbApp==null) return "";

        Gson gson = new Gson();
        String json = gson.toJson(dbApp);

        return json;
    }

    public int saveDbApp(){

        //scrivo la stringa json su disco
        String jsonString=this.getJsonString();

        ContextWrapper cw = new ContextWrapper(myContext);

        // path to /data/data/yourapp/app_data/jsonDir
        File directory = cw.getDir("jsonDir", Context.MODE_PRIVATE);

        // Create jsonDir
        File mypath=new File(directory,"dbapp.json");

        BufferedWriter bufferedWriter=null;

        try {

            bufferedWriter = new BufferedWriter(new FileWriter(mypath));

            bufferedWriter.write(jsonString);

            bufferedWriter.close();

        } catch (Exception e) {
            return -1;
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                return -2;
            }
        }
        return 0; //tutto ok
    }

    public DbApp getDbApp(){
        Gson gson = new Gson();

            ContextWrapper cw = new ContextWrapper(myContext);
            // path to /data/data/yourapp/app_data/jsonDir
            File directory = cw.getDir("jsonDir", Context.MODE_PRIVATE);
            BufferedReader bufferedReader=null;

            try {
                File f=new File(directory, "dbapp.json");
                bufferedReader = new BufferedReader(new FileReader(f));
            }
            catch (FileNotFoundException e)
            {
                //nothing
                return null;
            }

            //convert the json string back to object
            DbApp obj = gson.fromJson(bufferedReader, DbApp.class);

            this.dbApp=obj;
            return obj;

    }

    public List<Dish> getDishes()
    {
        return this.dbApp.getDishes();
    }

    public List<Booking> getBookings()
    {
        return this.dbApp.getBookings();
    }

    public RestaurateurProfile getRestaurateurProfile()
    {
        return this.dbApp.getProfile();
    }

    public void setRestaurateurProfile(RestaurateurProfile profile){
        dbApp.setProfile(profile);
    }

    /**
     * Created by carlocaramia on 09/04/16.
     */
    private class DbApp {

//        private static DbApp instance = null; // the only instance of this class
        private RestaurateurProfile profile;
        private List<Dish> dishes;
        private List<Booking> bookings;

        public  DbApp()
        {

        }

//        public static DbApp getInstance()
//        {
//            if(DbApp.instance == null)
//                DbApp.instance = new DbApp();
//
//            return DbApp.instance;
//        }
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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            newBooking.setDate_time(calendar);
            bookings.add(newBooking);

            Booking newBooking2 = new Booking();
            newBooking2.setID("2");
            ArrayList<Dish> elenco2=new ArrayList<Dish>();
            elenco2.add(this.dishes.get(3));
            newBooking2.setDishes(elenco2);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            newBooking2.setDate_time(calendar);
            bookings.add(newBooking2);

            Booking newBooking3 = new Booking();
            newBooking3.setID("3");
            ArrayList<Dish> elenco3=new ArrayList<>();
            elenco3.add(this.dishes.get(3));
            newBooking3.setDishes(elenco3);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            newBooking3.setDate_time(calendar);
            bookings.add(newBooking3);

            Booking newBooking4 = new Booking();
            newBooking4.setID("4");
            ArrayList<Dish> elenco4=new ArrayList<>();
            elenco4.add(this.dishes.get(2));
            newBooking4.setDishes(elenco4);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            newBooking4.setDate_time(calendar);
            bookings.add(newBooking4);
        }
    }


}


