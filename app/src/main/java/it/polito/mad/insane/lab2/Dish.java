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

//    public static ArrayList<Dish> getDishes()
//    {
//        ArrayList<Dish> dataList = new ArrayList<Dish>();
//
//        RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance();
//        ArrayList<Dish> tempDishes = (ArrayList<Dish>) manager.getDishes();
//        if(tempDishes != null)
//            dataList.addAll(manager.getDishes());
//
//        if(dataList.isEmpty())
//        {
//            //TODO: da togliere quando avremo il DB
//            Dish dish1 = new Dish("1","Piatto 1", "Descrizione piatto 1", null, 5.50, 100);
//            dataList.add(dish1);
//
//            Dish dish2 = new Dish("2","Piatto 2", "Descrizione piatto 2", null, 2.50, 200);
//            dataList.add(dish2);
//
//            Dish dish3 = new Dish("3","Piatto 3", "Descrizione piatto 3", null, 3.50, 300);
//            dataList.add(dish3);
//
//            Dish dish4 = new Dish("4","Piatto 4", "Descrizione piatto 4", null, 4.50, 104);
//            dataList.add(dish4);
//
//            Dish dish5 = new Dish("5","Piatto 5", "Descrizione piatto 5", null, 5.55, 150);
//            dataList.add(dish5);
//
//            Dish dish6 = new Dish("6","Piatto 6", "Descrizione piatto 6", null, 5.55, 150);
//            dataList.add(dish6);
//
//            Dish dish7 = new Dish("7","Piatto 7", "Descrizione piatto 7", null, 5.55, 150);
//            dataList.add(dish7);
//
//            Dish dish8 = new Dish("8","Piatto 8", "Descrizione piatto 8", null, 5.55, 150);
//            dataList.add(dish8);
//
//            Dish dish9 = new Dish("9","Piatto 9", "Descrizione piatto 9", null, 5.55, 150);
//            dataList.add(dish9);
//        }
//
//        return dataList;
//
//    }

    public Dish() {}
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
