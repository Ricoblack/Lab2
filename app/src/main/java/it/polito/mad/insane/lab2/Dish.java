package it.polito.mad.insane.lab2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class Dish implements Serializable{
    private String ID;
    private String name;
    private String description;
    private String photo_name;
    private double price;
    private int availability_qty;

    public static ArrayList<Dish> getData()
    {
        ArrayList<Dish> dataList = new ArrayList<Dish>();
        // int[] images = getImages(); // get images stored in the drawable folder

        Dish dish1 = new Dish();
        dish1.setAvailability_qty(100);
        dish1.setDescription("Descrizione del piatto 1");
        dish1.setID("1");
        dish1.setName("Piatto1");
        dish1.setPrice(5.50);

        dataList.add(dish1);

        return dataList;

    }

    public Dish()
    {

    }
    public Dish(String ID, String name, String description, String photo_name, double price, int availability_qty)
    {
        this.ID = ID;
        this.name = name;
        this.description= description;
        this.photo_name= photo_name;
        this.price = price;
        this.availability_qty = availability_qty;

    }


    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
        this.ID = ID;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPhoto_name()
    {
        return photo_name;
    }

    public void setPhoto_name(String photo_name)
    {
        this.photo_name = photo_name;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice (double price)
    {
        this.price = price;
    }

    public int getAvailability_qty()
    {
        return availability_qty;
    }

    public void setAvailability_qty(int availability_qty)
    {
        this.availability_qty = availability_qty;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
