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

        if(getDbApp()==null)
        {
            dbApp.fillDbApp();
            saveDbApp();
        }
        else
        {
            //recupero json
            this.dbApp=getDbApp();
        }
    }

    public String getJsonString()
    {
        //ritorna la stringa del Json

        if(dbApp==null) return "";

        Gson gson = new Gson();
        String json = gson.toJson(dbApp);

        return json;
    }

    public int saveDbApp(){

        //scrivo la stringa json su disco
        String jsonString = this.getJsonString();

        ContextWrapper cw = new ContextWrapper(myContext);

        // path to /data/data/yourapp/app_data/jsonDir
        File directory = cw.getDir("jsonDir", Context.MODE_PRIVATE);

        // Create jsonDir
        File mypath = new File(directory,"dbapp.json");

        BufferedWriter bufferedWriter=null;

        try
        {
            bufferedWriter = new BufferedWriter(new FileWriter(mypath));

            bufferedWriter.write(jsonString);

            bufferedWriter.close();

        } catch (Exception e)
        {
            return -1;
        }
        finally
        {
            try
            {
                bufferedWriter.close();
            } catch (IOException e)
            {
                return -2;
            }
        }
        return 0; //tutto ok
    }

    public DbApp getDbApp()
    {
        Gson gson = new Gson();

        ContextWrapper cw = new ContextWrapper(myContext);
        // path to /data/data/yourapp/app_data/jsonDir
        File directory = cw.getDir("jsonDir", Context.MODE_PRIVATE);
        BufferedReader bufferedReader=null;

        try
        {
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
    private class DbApp
    {

        private RestaurateurProfile profile;
        private List<Dish> dishes;
        private List<Booking> bookings;

        public  DbApp()
        {
            this.profile = null;
            this.dishes = null;
            this.bookings = null;
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

        public void fillDbApp()
        {

            //CARICAMENTO DATI PROFILE
            this.profile=new RestaurateurProfile("Pizza-Pazza","corso duca degli abruzzi, 10","PoliTo","Pizza","Venite a provare la pizza più gustosa di Torino",new Date(),new Date(),"Chiusi la domenica","Bancomat","Wifi-free");

            //CARICAMENTO DATI DISHES
            this.dishes=new ArrayList<Dish>();
            Dish dish1 = new Dish("0","Margherita", "La classica delle classiche", null, 5.50, 100);
            dishes.add(dish1);

            Dish dish2 = new Dish("1","Marinara", "Occhio all'aglio!", null, 2.50, 200);
            dishes.add(dish2);

            Dish dish3 = new Dish("2","Tonno", "Il gusto in una parola", null, 3.50, 300);
            dishes.add(dish3);

            Dish dish4 = new Dish("3","Politecnico", "Solo per veri ingegneri", null, 4.50, 104);
            dishes.add(dish4);

            Dish dish5 = new Dish("4","30L", "Il nome dice tutto: imperdibile", null, 5.55, 150);
            dishes.add(dish5);

            Dish dish6 = new Dish("5","Hilary", "Dedicata ad una vecchia amica", null, 5.55, 150);
            dishes.add(dish6);

            Dish dish7 = new Dish("6","Carlo", "Pomodoro e pesto, il mix perfetto", null, 5.55, 150);
            dishes.add(dish7);

            Dish dish8 = new Dish("7","Federico", "La vera pizza napoletana, spessa al punto giusto", null, 5.55, 150);
            dishes.add(dish8);

            Dish dish9 = new Dish("8","Michele", "Pomodoro, patatine, wurstel, senape", null, 5.55, 150);
            dishes.add(dish9);

            Dish dish10 = new Dish("9","Renato", "Pizza e pasta: mai dire mai al gusto!", null, 5.55, 150);
            dishes.add(dish10);




            //CARICAMENTO DATI BOOKINGS
            this.bookings=new ArrayList<Booking>();

            Booking newBooking = new Booking();
            newBooking.setID("1");
            ArrayList<Dish> elenco1=new ArrayList<Dish>();
            elenco1.add(this.dishes.get(2));
            elenco1.add(this.dishes.get(5));
            newBooking.setDishes(elenco1);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            newBooking.setDate_time(calendar);
            newBooking.setNote("Il cibo deve essere ben cotto");
            bookings.add(newBooking);

            Booking newBooking2 = new Booking();
            newBooking2.setID("2");
            ArrayList<Dish> elenco2=new ArrayList<Dish>();
            elenco2.add(this.dishes.get(3));
            newBooking2.setDishes(elenco2);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            newBooking2.setDate_time(calendar);
            newBooking2.setNote("Sono allergico ai latticini. Prego il ristoratore di evitarne l'utilizzo");
            bookings.add(newBooking2);

            Booking newBooking3 = new Booking();
            newBooking3.setID("3");
            ArrayList<Dish> elenco3=new ArrayList<Dish>();
            elenco3.add(this.dishes.get(3));
            newBooking3.setDishes(elenco3);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            newBooking3.setDate_time(calendar);
            bookings.add(newBooking3);

            Booking newBooking4 = new Booking();
            newBooking4.setID("4");
            ArrayList<Dish> elenco4=new ArrayList<Dish>();
            elenco4.add(this.dishes.get(2));
            newBooking4.setDishes(elenco4);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-2);
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            newBooking4.setDate_time(calendar);
            bookings.add(newBooking4);

            Booking newBooking5 = new Booking();
            newBooking5.setID("5");
            ArrayList<Dish> elenco5=new ArrayList<Dish>();
            elenco5.add(this.dishes.get(0));
            elenco5.add(this.dishes.get(1));
            elenco5.add(this.dishes.get(0));
            newBooking5.setDishes(elenco5);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
            calendar.set(Calendar.HOUR_OF_DAY, 19);
            newBooking5.setDate_time(calendar);
            bookings.add(newBooking5);

            Booking newBooking6 = new Booking();
            newBooking6.setID("6");
            ArrayList<Dish> elenco6=new ArrayList<Dish>();
            elenco6.add(this.dishes.get(3));
            newBooking6.setDishes(elenco6);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            newBooking6.setDate_time(calendar);
            newBooking6.setNote("Se arrivo tardi tenete il piatto al caldo, grazie");
            bookings.add(newBooking6);

            Booking newBooking7 = new Booking();
            newBooking7.setID("7");
            ArrayList<Dish> elenco7=new ArrayList<Dish>();
            elenco7.add(this.dishes.get(0));
            elenco7.add(this.dishes.get(1));
            elenco7.add(this.dishes.get(0));
            newBooking7.setDishes(elenco7);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
            calendar.set(Calendar.HOUR_OF_DAY, 13);
            newBooking7.setDate_time(calendar);
            bookings.add(newBooking7);

            Booking newBooking8 = new Booking();
            newBooking8.setID("8");
            ArrayList<Dish> elenco8=new ArrayList<Dish>();
            elenco8.add(this.dishes.get(3));
            elenco8.add(this.dishes.get(1));
            elenco8.add(this.dishes.get(7));
            newBooking8.setDishes(elenco8);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            newBooking8.setDate_time(calendar);
            bookings.add(newBooking8);

            Booking newBooking9 = new Booking();
            newBooking9.setID("9");
            ArrayList<Dish> elenco9=new ArrayList<Dish>();
            elenco9.add(this.dishes.get(9));
            elenco9.add(this.dishes.get(8));
            elenco9.add(this.dishes.get(7));
            newBooking9.setDishes(elenco9);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            newBooking9.setDate_time(calendar);
            bookings.add(newBooking9);

            Booking newBooking10 = new Booking();
            newBooking10.setID("10");
            ArrayList<Dish> elenco10=new ArrayList<Dish>();
            elenco10.add(this.dishes.get(3));
            elenco10.add(this.dishes.get(1));
            elenco10.add(this.dishes.get(7));
            newBooking10.setDishes(elenco10);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            newBooking10.setDate_time(calendar);
            bookings.add(newBooking10);

            Booking newBooking11 = new Booking();
            newBooking11.setID("11");
            ArrayList<Dish> elenco11=new ArrayList<Dish>();
            elenco11.add(this.dishes.get(6));
            elenco11.add(this.dishes.get(3));
            newBooking11.setDishes(elenco11);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            newBooking11.setDate_time(calendar);
            newBooking11.setNote("Il cibo deve essere ben cotto");
            bookings.add(newBooking11);

            Booking newBooking12 = new Booking();
            newBooking12.setID("12");
            ArrayList<Dish> elenco12=new ArrayList<Dish>();
            elenco12.add(this.dishes.get(3));
            newBooking12.setDishes(elenco12);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            newBooking12.setDate_time(calendar);
            newBooking12.setNote("Sono allergico ai latticini. Prego il ristoratore di evitarne l'utilizzo");
            bookings.add(newBooking12);

            Booking newBooking13 = new Booking();
            newBooking13.setID("13");
            ArrayList<Dish> elenco13=new ArrayList<Dish>();
            elenco13.add(this.dishes.get(3));
            newBooking13.setDishes(elenco13);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            newBooking13.setDate_time(calendar);
            bookings.add(newBooking13);

            Booking newBooking14 = new Booking();
            newBooking14.setID("14");
            ArrayList<Dish> elenco14=new ArrayList<Dish>();
            elenco14.add(this.dishes.get(3));
            newBooking14.setDishes(elenco14);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            newBooking14.setDate_time(calendar);
            bookings.add(newBooking14);


            Booking newBooking15 = new Booking();
            newBooking15.setID("15");
            ArrayList<Dish> elenco15=new ArrayList<Dish>();
            elenco15.add(this.dishes.get(3));
            newBooking15.setDishes(elenco15);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            newBooking15.setDate_time(calendar);
            bookings.add(newBooking15);

            Booking newBooking16 = new Booking();
            newBooking16.setID("16");
            ArrayList<Dish> elenco16=new ArrayList<Dish>();
            elenco16.add(this.dishes.get(3));
            newBooking16.setDishes(elenco16);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            newBooking16.setDate_time(calendar);
            bookings.add(newBooking16);
        }
    }
}


