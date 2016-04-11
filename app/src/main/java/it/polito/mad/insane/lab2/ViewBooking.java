package it.polito.mad.insane.lab2;

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
    TextView note = null;
    TextView data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.currentBooking = (Booking) getIntent().getSerializableExtra("Booking");
        this.note = (TextView)findViewById(R.id.note_dish);
        this.data = (TextView)findViewById(R.id.date_booking);


        setTitle("Prenotazione");
        this.note.setText(this.currentBooking.getNote());
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
        DishesRecyclerAdapter adapter = new DishesRecyclerAdapter(this, currentBooking.getDishes());
        recyclerView.setAdapter(adapter);

        // set Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        // set Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
    }

}
