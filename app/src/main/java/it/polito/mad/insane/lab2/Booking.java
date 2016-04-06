package it.polito.mad.insane.lab2;

import java.util.ArrayList;

/**
 * Created by miche on 06/04/2016.
 */
public class Booking
{
    // attributi della prenotazione
    private String TitoloCosaACazzo;


    public static ArrayList<Booking> getData()
    {
        ArrayList<Booking> dataList = new ArrayList<>();

        // populate Booking elements (slide 28)
        Booking newBooking = new Booking();
        newBooking.setTitoloCosaACazzo("Booking 1");
        dataList.add(newBooking);
        Booking newBooking2 = new Booking();
        newBooking2.setTitoloCosaACazzo("Booking 2");
        dataList.add(newBooking2);

        return dataList;
    }


    public String getTitoloCosaACazzo()
    {
        return TitoloCosaACazzo;
    }

    public void setTitoloCosaACazzo(String titoloCosaACazzo)
    {
        TitoloCosaACazzo = titoloCosaACazzo;
    }
}
