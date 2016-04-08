package it.polito.mad.insane.lab2;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class DailyOffer {
    private ArrayList<Dish> dishes;


    public DailyOffer(){

    }

    public void setDishes(ArrayList<Dish> dishes){
        this.dishes=dishes;
    }
    public ArrayList<Dish> getDishes(){
        return this.dishes;

    }

}
