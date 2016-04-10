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
import java.util.List;

/**
 * Created by carlocaramia on 08/04/16.
 */
// Singleton Class
public class RestaurateurJsonManager {

    private static RestaurateurJsonManager instance = null;
    private DbApp dbApp; // Singleton
    private Context myContext;

    public static RestaurateurJsonManager getInstance(Context myContext)
    {
        if(RestaurateurJsonManager.instance == null)
            RestaurateurJsonManager.instance = new RestaurateurJsonManager(myContext);

        return RestaurateurJsonManager.instance;
    }
    private RestaurateurJsonManager(Context myContext)
    {
        this.dbApp = DbApp.getInstance();
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

}


