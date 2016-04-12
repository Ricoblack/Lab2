package it.polito.mad.insane.lab2;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeRestaurateur extends AppCompatActivity
{
    private static RestaurateurJsonManager manager = null;
    int position;
    private BookingsRecyclerAdapter adapter;

    /* Our Methods */

    // Layout Manager
    private void setUpRecyclerDay(int year, int month, int day)
    {

        RecyclerView rV = (RecyclerView) findViewById(R.id.BookingRecyclerView);

        //BookingsRecyclerAdapter adapter = new BookingsRecyclerAdapter(this, HomeRestaurateur.manager.getBookings());
        Calendar c=Calendar.getInstance();
        adapter = new BookingsRecyclerAdapter(this, getBookingsOfDay(year,month,day));
        rV.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        rV.setLayoutManager(mLinearLayoutManagerVertical);

        // If you don't apply other animations it uses the default one
        rV.setItemAnimator(new DefaultItemAnimator());

        fillGraphWithBookings(adapter);

    }


    private void fillGraphWithBookings(BookingsRecyclerAdapter adapter) {

        //creo un vettore in cui gli indici corrispondono alle ore del giorno e i valori al numero di prenotazioni in quell'ora
        int hours[] = new int[24];

        //inizializzo il vettore con tutti zeri
        for(int i=0; i<24; i++)
            hours[i] = 0;

        //prendo la lista delle prenotazioni
        List<Booking> bookings = adapter.getMData();

        // aggiungo all'i-esimo posto (che corrisponde all'i-esima ora) il numero di piatti prenotati
        for(Booking b : bookings)
            hours[b.getDate_time().get(Calendar.HOUR_OF_DAY)] += b.getDishes().size();

        GraphView graph = (GraphView) findViewById(R.id.graph);

        //creo un vettore di DataPoint per riempire il grafico. quest'oggetto contiene un item per ogni ora del giorno
        DataPoint[] graphBookings= new DataPoint[24];
        for(int i=0; i<hours.length; i++){
            //creo un DataPoint che ha per ascissa l'ora e per ordinata il numero di prenotazioni per ogni ora
            graphBookings[i] = new DataPoint(i, hours[i]);
        }
        //creo l'oggetto BarGraphSeries per avere un istogramma
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(graphBookings);
        //aggiungo la serie al grafico in modo da visualizzarla
        graph.addSeries(series);

        editGraph(series); //modifica l'aspetto visivo del grafico
    }

    /* Standard methods */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        HomeRestaurateur.manager = RestaurateurJsonManager.getInstance(this);
        setContentView(R.layout.activity_home_restaurateur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //caso in cui debba essere cancellata una eccezione faccio il refresh della home
//        position = getIntent().getIntExtra("pos",-1);
//        if( position != -1 ){
//            manager.getBookings().remove(position);
//            manager.saveDbApp();
//
//        }

        Calendar c=Calendar.getInstance();
        setUpRecyclerDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

    }

    private void editGraph(BarGraphSeries<DataPoint> series) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = HomeRestaurateur.this.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        final int colorAccent = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int colorPrimary = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int colorPrimaryDark = typedValue.data;


        GraphView graph = (GraphView) findViewById(R.id.graph);

        if (graph != null) {
            series.setSpacing(20);
            series.setTitle("Bookings");
            series.setDrawValuesOnTop(true);
            series.setColor(colorPrimary);
            series.setValuesOnTopColor(colorPrimaryDark);
            series.setValuesOnTopSize(50);

            series.setOnDataPointTapListener(new OnDataPointTapListener() {

                @Override
                public void onTap(Series series, final DataPointInterface dataPoint) {
                    setUpRecyclerHour((int) dataPoint.getX());
                }
            });

            graph.getViewport().setXAxisBoundsManual(true);
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            graph.getViewport().setMinX(hour);

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                graph.getGridLabelRenderer().setNumHorizontalLabels(6+1);
                if(hour > 24 -6)
                    graph.getViewport().setMaxX(24);
                else
                    graph.getViewport().setMaxX(hour + 6);
            }
            else {
                graph.getGridLabelRenderer().setNumHorizontalLabels(12+1);
                if(hour > 24 -6)
                    graph.getViewport().setMaxX(24);
                else
                    graph.getViewport().setMaxX(hour + 12);
            }

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getLegendRenderer().setTextColor(Color.WHITE);
//        graph.getLegendRenderer().setBackgroundColor();
//        graph.getLegendRenderer().setTextSize();
//        graph.getLegendRenderer().setWidth();
//        graph.getLegendRenderer().setBackgroundColor(color);

            graph.getGridLabelRenderer().setPadding(20);

            graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(0);
            nf.setMaximumIntegerDigits(2);
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));
//        graph.getViewport().setScrollable(true);
//        graph.getGridLabelRenderer().setHorizontalLabelsColor(colorPrimary);
//        graph.getGridLabelRenderer().setVerticalLabelsColor(colorPrimary);
//        graph.getGridLabelRenderer().setVerticalAxisTitle("Bookings");
//        graph.getGridLabelRenderer().setHorizontalAxisTitle("Hours");

            graph.getGridLabelRenderer().setGridColor(0xFFFAFAFA); //background theme color
            graph.getGridLabelRenderer().reloadStyles();
        }

    }

    public void showDatePickerDialog(View v)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_restaurateur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
        switch(id)
        {
            case R.id.action_daily_menu:
                // Start DailyMenu activity
                Intent invokeDailyMenu = new Intent(HomeRestaurateur.this, DailyMenu.class);
                startActivity(invokeDailyMenu);
                break;
            case R.id.action_edit_profile:
                //Start EditProfile activity
                Intent invokeEditProfile = new Intent(HomeRestaurateur.this, EditProfile.class);
                startActivity(invokeEditProfile);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        // Activity being restarted from stopped state
        //Calendar c=Calendar.getInstance();
        //setUpRecyclerDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }



    public void setDate(int year, int month, int day) {
        //set graph time interval
        //TODO:set graph time interval

        //set up again recycle view
        setUpRecyclerDay(year,month,day);
    }

    private List<Booking> getBookingsOfDay(int year,int month,int day){

        ArrayList<Booking> bookingList= new ArrayList<Booking>();
        ArrayList<Booking> totalList= (ArrayList<Booking>) HomeRestaurateur.manager.getBookings();
        for(int i=0;i<totalList.size();i++){
            Booking booking=totalList.get(i);
            Calendar c=booking.getDate_time();
            if(c.get(Calendar.YEAR)==year && c.get(Calendar.MONTH)==month && c.get(Calendar.DAY_OF_MONTH)==day){
                bookingList.add(booking);
            }
        }
        return bookingList;
    }

    private void setUpRecyclerHour (final int hour){
        RecyclerView rV = (RecyclerView) findViewById(R.id.BookingRecyclerView);

        Calendar c = Calendar.getInstance();
        BookingsRecyclerAdapter adapter = new BookingsRecyclerAdapter(this, getBookingsOfHour(hour));
        rV.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        rV.setLayoutManager(mLinearLayoutManagerVertical);

        // If you don't apply other animations it uses the default one
        rV.setItemAnimator(new DefaultItemAnimator());
        TextView tv = (TextView) findViewById(R.id.hourBanner);
        if (tv != null) {
            tv.setText(hour + ":00");
        }
        tv.setVisibility(View.VISIBLE);
    }

    private List<Booking> getBookingsOfHour(int hour){

        ArrayList<Booking> bookingList= new ArrayList<Booking>();
        ArrayList<Booking> totalList= (ArrayList<Booking>) HomeRestaurateur.manager.getBookings();
        for(Booking b : totalList){
            if (b.getDate_time().get(Calendar.HOUR_OF_DAY) == hour)
                bookingList.add(b);
        }
        return bookingList;
    }
}
