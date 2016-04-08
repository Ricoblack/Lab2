package it.polito.mad.insane.lab2;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class Dish {
    private String dishCode;
    private String description;
    private String photo_name;
    private double price;
    private int availability_qty;

    public Dish(String dishCode,String description,String photo_name,double price,int availability_qty){
        this.dishCode=dishCode;
        this.description=description;
        this.photo_name=photo_name;

    }

    public String getDishCode() {
        return dishCode;
    }

    public void setDishCode(String dishCode) {
        this.dishCode = dishCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailability_qty() {
        return availability_qty;
    }

    public void setAvailability_qty(int availability_qty) {
        this.availability_qty = availability_qty;
    }
}
