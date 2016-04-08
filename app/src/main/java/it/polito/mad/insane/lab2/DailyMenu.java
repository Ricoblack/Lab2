package it.polito.mad.insane.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class DailyMenu extends AppCompatActivity
{

    /* Standard Methods */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow

        setupDishesRecyclerView();

    }

    /* Our Methods */
    private void setupDishesRecyclerView()
    {
        // set Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);
        DishesRecyclerAdapter adapter = new DishesRecyclerAdapter(this,Dish.getData());
        recyclerView.setAdapter(adapter);

        // set Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        // set Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
    }
}
