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

        Dish dish2 = new Dish();
        dish1.setAvailability_qty(102);
        dish1.setDescription("Descrizione del piatto 2");
        dish1.setID("2");
        dish1.setName("Piatto2");
        dish1.setPrice(5.52);
        dataList.add(dish2);

        Dish dish3 = new Dish();
        dish1.setAvailability_qty(13);
        dish1.setDescription("Descrizione del piatto 3");
        dish1.setID("3");
        dish1.setName("Piatto3");
        dish1.setPrice(3.50);
        dataList.add(dish3);

        Dish dish4 = new Dish();
        dish1.setAvailability_qty(104);
        dish1.setDescription("Descrizione del piatto 4");
        dish1.setID("4");
        dish1.setName("Piatto4");
        dish1.setPrice(5.40);
        dataList.add(dish4);

        Dish dish5 = new Dish();
        dish1.setAvailability_qty(105);
        dish1.setDescription("Descrizione del piatto 5");
        dish1.setID("5");
        dish1.setName("Piatto5");
        dish1.setPrice(6.50);
        dataList.add(dish5);

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
