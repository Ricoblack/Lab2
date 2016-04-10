package it.polito.mad.insane.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HomeRestaurateur extends AppCompatActivity
{
    /* Our Methods */

    // Layout Manager
    private void setUpRecyclerView()
    {
        RecyclerView rV = (RecyclerView) findViewById(R.id.BookingRecyclerView);
        BookingsRecyclerAdapter adapter = new BookingsRecyclerAdapter(this, RestaurateurJsonManager.getInstance().getBookings());
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
        setContentView(R.layout.activity_home_restaurateur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 1)
        });
        graph.addSeries(series);
        graph.setTitle("Data del Giorno");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        setUpRecyclerView();


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
