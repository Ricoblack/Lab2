package it.polito.mad.insane.lab2;

import java.io.Serializable;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class Dish implements Serializable{
    private String ID;
    private String name;
    private String description;
    private String photoPath;
    private double price;
    private int availability_qty;


    public Dish()
    {
        this.ID = null;
        this.name = null;
        this.description= null;
        this.photoPath= null;
        this.price = 0;
        this.availability_qty = 0;
    }
    public Dish(String ID, String name, String description, String photoPath, double price, int availability_qty)
    {
        this.ID = ID;
        this.name = name;
        this.description= description;
        this.photoPath= photoPath;
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

    public String getPhotoPath()
    {
        return this.photoPath;
    }

    public void setPhotoPath(String photo_name)
    {
        this.photoPath = photo_name;
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
