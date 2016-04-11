package it.polito.mad.insane.lab2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by miche on 06/04/2016.
 */
public class Booking implements Serializable
{

    private String ID;
    private Calendar date_time = Calendar.getInstance(); // TODO: da togliere, è solo stato messo per evitare NullPointerException
    private List<Dish> dishes = new ArrayList<>(); // TODO: da togliere, è solo stato messo per evitare NullPointerException
    private String note;


//    public static ArrayList<Booking> getData()
//    {
//        ArrayList<Booking> dataList = new ArrayList<>();
//
//        // populate Booking elements (slide 28)
//        Booking newBooking = new Booking();
//        newBooking.setID("24");
//        dataList.add(newBooking);
//        Booking newBooking2 = new Booking();
//        newBooking2.setID("25");
//        dataList.add(newBooking2);
//
//        return dataList;
//    }
    //ciao michele lo so che mi odierai


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Calendar getDate_time() {
        return date_time;
    }

    public void setDate_time(Calendar date_time) {
        this.date_time = date_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
