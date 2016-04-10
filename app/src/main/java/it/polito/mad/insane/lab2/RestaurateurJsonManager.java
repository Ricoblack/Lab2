package it.polito.mad.insane.lab2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/**
 * Created by carlocaramia on 08/04/16.
 */
// Singleton Class
public class RestaurateurJsonManager extends Activity {

    private static RestaurateurJsonManager instance = null;
    private DbApp dbApp; // Singleton

    public static RestaurateurJsonManager getInstance()
    {
        if(RestaurateurJsonManager.instance == null)
            RestaurateurJsonManager.instance = new RestaurateurJsonManager();

        return RestaurateurJsonManager.instance;
    }
    private RestaurateurJsonManager()
    {
        this.dbApp = DbApp.getInstance();
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

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

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


            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/jsonDir
            File directory = cw.getDir("jsonDir", Context.MODE_PRIVATE);
            BufferedReader br=null;

            try {
                File f=new File(directory, "dbapp.json");
                BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            }
            catch (FileNotFoundException e)
            {
                //nothing
                return null;
            }

            //convert the json string back to object
            DbApp obj = gson.fromJson(br, DbApp.class);

            this.dbApp=obj;
            return obj;

    }

    public List<Dish> getDishes()
    {
        ArrayList<Dish> result;

        result = (ArrayList<Dish>) this.dbApp.getDishes();
        if(result != null)
            return result;
        else
        {   //TODO: da togliere quando avremo il DB

            result = new ArrayList<Dish>();

            Dish dish1 = new Dish("1","Piatto 1", "Descrizione piatto 1", null, 5.50, 100);
            result.add(dish1);

            Dish dish2 = new Dish("2","Piatto 2", "Descrizione piatto 2", null, 2.50, 200);
            result.add(dish2);

            Dish dish3 = new Dish("3","Piatto 3", "Descrizione piatto 3", null, 3.50, 300);
            result.add(dish3);

            Dish dish4 = new Dish("4","Piatto 4", "Descrizione piatto 4", null, 4.50, 104);
            result.add(dish4);

            Dish dish5 = new Dish("5","Piatto 5", "Descrizione piatto 5", null, 5.55, 150);
            result.add(dish5);

            Dish dish6 = new Dish("6","Piatto 6", "Descrizione piatto 6", null, 5.55, 150);
            result.add(dish6);

            Dish dish7 = new Dish("7","Piatto 7", "Descrizione piatto 7", null, 5.55, 150);
            result.add(dish7);

            Dish dish8 = new Dish("8","Piatto 8", "Descrizione piatto 8", null, 5.55, 150);
            result.add(dish8);

            Dish dish9 = new Dish("9","Piatto 9", "Descrizione piatto 9", null, 5.55, 150);
            result.add(dish9);

            return result;
        }
    }

    public List<Booking> getBookings()
    {
        ArrayList<Booking> result;
        result = (ArrayList<Booking>) this.dbApp.getBookings();
        if(result!= null)
            return result;
        else
        {   //TODO: da togliere quando avremo il DB
            result = new ArrayList<Booking>();

            Booking newBooking = new Booking();
            newBooking.setID("24");
            result.add(newBooking);

            Booking newBooking2 = new Booking();
            newBooking2.setID("25");
            result.add(newBooking2);
            return result;
        }
    }

    public RestaurateurProfile getRestaurateurProfile()
    {
        return this.dbApp.getProfile();
    }

}


