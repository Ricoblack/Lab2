package it.polito.mad.insane.lab2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditDish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Dish currentDish = (Dish)getIntent().getSerializableExtra("Dish");
        if(currentDish != null)
        {
            // Edit existing dish
            EditText dishID = (EditText) EditDish.this.findViewById(R.id.edit_dish_ID);
            dishID.setText(currentDish.getID());

            EditText dishName = (EditText) EditDish.this.findViewById(R.id.edit_dish_name);
            dishName.setText(currentDish.getName());
            setTitle(currentDish.getName());

            EditText dishDesc = (EditText) EditDish.this.findViewById(R.id.edit_dish_description);
            dishDesc.setText(currentDish.getDescription());

            EditText dishQty = (EditText) EditDish.this.findViewById(R.id.edit_dish_availab_qty);
            dishQty.setText(Integer.toString(currentDish.getAvailability_qty()));

            EditText dishPrice = (EditText) EditDish.this.findViewById(R.id.edit_dish_price);
            dishPrice.setText(Double.toString(currentDish.getPrice()));

            // TODO: ImageView dishPhoto = (ImageView) EditDish.this.findViewById(R.id.dishPhoto);
            // TODO: dishPhoto.set .....




        }else
        {
            // Add new dish
            setTitle(R.string.new_dish);
        }

    }


}
