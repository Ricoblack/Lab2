package it.polito.mad.insane.lab2;

import java.util.Date;

/**
 * Created by carlocaramia on 08/04/16.
 */
public class RestaurateurProfile {

    private String restaurantName;
    private String address;
    private String nearbyUniversity;
    private String cuisineType;
    private String description;
    private Date openingHour;
    private Date closingHour;
    private String timeInfo;
    private String paymentMethod;
    private String additionalServices;



    public RestaurateurProfile(String restaurantName, String address, String nearbyUniversity, String cuisineType,
                               String description, Date openingHour, Date closingHour, String timeInfo, String paymentMethod,
                               String additionalServices){

        this.restaurantName = restaurantName;
        this.address=address;
        this.openingHour=openingHour;
        this.closingHour=closingHour;
        this.nearbyUniversity = nearbyUniversity;
        this.cuisineType = cuisineType;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.timeInfo = timeInfo;
        this.paymentMethod = paymentMethod;
        this.additionalServices = additionalServices;
        this.description = description;
    }

    public RestaurateurProfile() {}

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

    public String getNearbyUniversity() {
        return nearbyUniversity;
    }

    public void setNearbyUniversity(String nearbyUniversity) {
        this.nearbyUniversity = nearbyUniversity;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    public void setTimeInfo(String timeInfo) {
        this.timeInfo = timeInfo;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
