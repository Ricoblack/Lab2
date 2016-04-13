package it.polito.mad.insane.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DailyMenu extends AppCompatActivity {
    private DishesRecyclerAdapter adapter = null;
    static private RestaurateurJsonManager manager = null;

    // FIXME: se setti l'immagine della cardview con id piu grande, tutte le immagini delle altre cardview verranno settate con la stessa immagine quando confermi la modifica
    //FIXME:        dell'immagine, sia se gli setti una immagine sia che non gliela setti
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
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open activity EditDish
                    Intent i = new Intent(view.getContext(),EditDish.class);
//                    i.putExtra("Adapter",DailyMenu.this.adapter);
                    view.getContext().startActivity(i);

                }
            });
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(this.adapter!=null)
            this.adapter.notifyDataSetChanged();
        setupDishesRecyclerView();
    }



    /* Our Methods */
    private void setupDishesRecyclerView()
    {
        // set Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);
        DailyMenu.this.adapter = new DishesRecyclerAdapter(this, DailyMenu.manager.getDishes(),true);
        if (recyclerView != null) {
            recyclerView.setAdapter(DailyMenu.this.adapter);


        // set Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        // set Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
        }
    }

}
