package it.polito.mad.insane.lab2;

import java.util.Date;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class RestaurateurProfile {

    private String name;
    private String surname;
    private Date openingHour;
    private Date closingHour;
    private String address;
    private String restaurantName;

    public RestaurateurProfile(String name,String surname,Date openingHour, Date closingHour,String address,String restaurantName){
        this.name=name;
        this.surname=surname;
        this.openingHour=openingHour;
        this.closingHour=closingHour;
        this.address=address;
        this.restaurantName=restaurantName;


    }

    public RestaurateurProfile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(Date openingHour) {
        this.openingHour = openingHour;
    }

    public Date getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(Date closingHour) {
        this.closingHour = closingHour;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

}
