package it.polito.mad.insane.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;;

/**
 * Created by carlocaramia on 08/04/16.
 */

public class RestaurateurJsonManager {

    private DbApp dbApp;

    public RestaurateurJsonManager(DbApp dbApp){
        this.dbApp=dbApp;
    }

    public String getJsonString(){
        //ritorna la stringa del Json

        if(dbApp==null) return "";

        Gson gson = new Gson();
        String json = gson.toJson(dbApp);

        return json;
    }

    public int writeJson(){

        //scrivo la stringa json su disco
        String jsonString=this.getJsonString();

        //TODO: scrivere stringa su file

        return 0; //tutto ok
    }

    public DbApp getDbApp(){
        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(
                    new FileReader("c:\\file.json"));

            //convert the json string back to object
            DbApp obj = gson.fromJson(br, DbApp.class);


            this.dbApp=obj;
            return obj;

        } catch (IOException e) {

        }

        return null;
    }

}


