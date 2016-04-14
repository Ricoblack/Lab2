package it.polito.mad.insane.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ViewBooking extends AppCompatActivity {

    private Booking currentBooking = null;
    private  static RestaurateurJsonManager manager = null;
    TextView note = null;
    TextView data = null;
    TextView ID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = RestaurateurJsonManager.getInstance(ViewBooking.this);
        setContentView(R.layout.activity_view_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Booking b: manager.getBookings()){
                        if(b.getID().equals(currentBooking.getID())){
                            manager.getBookings().remove(b);
                            break;
                        }
                    }
                    manager.saveDbApp();
                    finish();
                }
            });
        }

        this.currentBooking = (Booking) getIntent().getSerializableExtra("Booking");
        //this.position = getIntent().getIntExtra("pos",-1);
        this.ID = (TextView)findViewById(R.id.booking_ID);
        this.note = (TextView)findViewById(R.id.note_dish);
        this.data = (TextView)findViewById(R.id.date_booking);


        setTitle(R.string.booking_title);

        this.ID.setText("#"+this.currentBooking.getID());
        if(this.currentBooking.getNote() == null){
            this.note.setText(R.string.no_notes_in_bookings);
        }else {
            this.note.setText(this.currentBooking.getNote());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        this.data.setText(dateFormat.format(this.currentBooking.getDate_time().getTime()));

        // initialize Recycler View
        setupDishesRecyclerView();

    }


    /* Our Methods */
    private void setupDishesRecyclerView()
    {
        // set Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);
        DishesRecyclerAdapter adapter = new DishesRecyclerAdapter(this, currentBooking.getDishes(),false);
        recyclerView.setAdapter(adapter);

        // set Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        // set Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
    }

}
