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
        return this.dbApp.getDishes();
    }

    public List<Booking> getBookings()
    {
        return this.dbApp.getBookings();
    }

    public RestaurateurProfile getRestaurateurProfile()
    {
        return this.getRestaurateurProfile();
    }



}


