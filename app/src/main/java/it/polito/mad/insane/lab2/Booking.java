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
    private Calendar date_time = Calendar.getInstance();
    private List<Dish> dishes = new ArrayList<>();
    private String note;

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
