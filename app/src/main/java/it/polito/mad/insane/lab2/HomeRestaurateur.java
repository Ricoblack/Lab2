package it.polito.mad.insane.lab2;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;


public class HomeRestaurateur extends AppCompatActivity
{
    private static RestaurateurJsonManager manager = null;

    /* Our Methods */

    // Layout Manager
    private void setUpRecyclerView()
    {
        RecyclerView rV = (RecyclerView) findViewById(R.id.BookingRecyclerView);
        BookingsRecyclerAdapter adapter = new BookingsRecyclerAdapter(this, HomeRestaurateur.manager.getBookings());
        rV.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        rV.setLayoutManager(mLinearLayoutManagerVertical);

        // If you don't apply other animations it uses the default one
        rV.setItemAnimator(new DefaultItemAnimator());
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

        editGraph();

        setUpRecyclerView();


    }

    private void editGraph() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = HomeRestaurateur.this.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int colorAccent = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int colorPrimary = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int colorPrimaryDark = typedValue.data;


        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(21, 2),
                new DataPoint(16, 6),
                new DataPoint(18, 1),
                new DataPoint(17,6),
                new DataPoint(20, 2)
        });
        graph.addSeries(series);
        series.setSpacing(20);
        series.setTitle("Bookings");
        series.setDrawValuesOnTop(true);
        series.setColor(colorPrimary);
        series.setValuesOnTopColor(colorPrimaryDark);
        series.setValuesOnTopSize(50);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HomeRestaurateur.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        graph.getViewport().setXAxisBoundsManual(true);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        graph.getViewport().setMinX(hour);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            graph.getGridLabelRenderer().setNumHorizontalLabels(6+1);
            graph.getViewport().setMaxX(hour + 6);
        }
        else {
            graph.getGridLabelRenderer().setNumHorizontalLabels(12+1);
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

    }


}
