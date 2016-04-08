package it.polito.mad.insane.lab2;

import java.util.ArrayList;

/**
 * Created by miche on 06/04/2016.
 */
public class Booking
{


    // attributi della prenotazione
    private String book_id;


    public static ArrayList<Booking> getData()
    {
        ArrayList<Booking> dataList = new ArrayList<>();

        // populate Booking elements (slide 28)
        Booking newBooking = new Booking();
        newBooking.setBook_id("#24");
        dataList.add(newBooking);
        Booking newBooking2 = new Booking();
        newBooking2.setBook_id("#25");
        dataList.add(newBooking2);

        return dataList;
    }


    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }


}
