package it.polito.mad.insane.lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

public class DailyMenu extends AppCompatActivity {

    static private RestaurateurJsonManager manager = null;
    /* Standard Methods */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DailyMenu.manager = RestaurateurJsonManager.getInstance(this);

        setContentView(R.layout.activity_daily_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize Recycler View
        setupDishesRecyclerView();

        // set Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_dish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open activity EditDish
                Intent i = new Intent(view.getContext(),EditDish.class);
                view.getContext().startActivity(i);

            }
        });

    }

    /* Our Methods */


    private void setupDishesRecyclerView()
    {
        // set Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);
        DishesRecyclerAdapter adapter = new DishesRecyclerAdapter(this, DailyMenu.manager.getDishes());
        recyclerView.setAdapter(adapter);

        // set Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        // set Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
    }

}
