package it.polito.mad.insane.lab2;

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

    public String getJson(){
        //ritorna la stringa del Json

        if(dbApp==null) return "";

        Gson gson = new Gson();
        String json = gson.toJson(dbApp);

        return json;
    }

}


