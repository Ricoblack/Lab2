package it.polito.mad.insane.lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditDish extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 280;

    private static RestaurateurJsonManager manager = null;
    Dish currentDish = null;
    EditText dishID;
    EditText dishName;
    EditText dishDesc;
    EditText dishQty;
    EditText dishPrice;
    ImageView dishPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // initialize manager
        EditDish.manager = RestaurateurJsonManager.getInstance(this);

        // load layout form XML
        setContentView(R.layout.activity_edit_dish);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_edit_dish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // save data in manager
                if(EditDish.this.currentDish != null)
                {
                    // Edit existing dish
                    for (Dish d : EditDish.manager.getDishes())
                    {
                        try
                        {
                            if (d.getID().equals(EditDish.this.currentDish.getID()))
                            {
                                d.setName(EditDish.this.dishName.getText().toString());
                                d.setDescription(EditDish.this.dishDesc.getText().toString());
                                d.setAvailability_qty(Integer.parseInt(EditDish.this.dishQty.getText().toString()));
                                d.setPrice(Double.parseDouble(EditDish.this.dishPrice.getText().toString()));
                                //d.setPhoto_name();
                                Toast.makeText(EditDish.this, R.string.confirm_save_dish, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(EditDish.this, R.string.error_input_number, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                {

                    //TODO: inserisci controlli per veriificare che tutti i campi siano stati riempiti
                    try {
                        // Add new dish
                        Dish d = new Dish();
                        d.setName(EditDish.this.dishName.getText().toString());
                        d.setDescription(EditDish.this.dishDesc.getText().toString());
                        d.setAvailability_qty(Integer.parseInt(EditDish.this.dishQty.getText().toString()));
                        d.setPrice(Double.parseDouble(EditDish.this.dishPrice.getText().toString()));
                        //d.setPhoto_name();
                        EditDish.manager.getDishes().add(d);
                        Toast.makeText(EditDish.this, R.string.confirm_add_dish, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }catch( NumberFormatException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(EditDish.this, R.string.error_input_number, Toast.LENGTH_SHORT).show();
                    }



                }
            }
        });


        this.dishID = (EditText) EditDish.this.findViewById(R.id.edit_dish_ID);
        this.dishName = (EditText) EditDish.this.findViewById(R.id.edit_dish_name);
        this.dishDesc = (EditText) EditDish.this.findViewById(R.id.edit_dish_description);
        this.dishQty = (EditText) EditDish.this.findViewById(R.id.edit_dish_availab_qty);
        this.dishPrice = (EditText) EditDish.this.findViewById(R.id.edit_dish_price);
        this.dishPhoto = (ImageView) EditDish.this.findViewById(R.id.dishPhoto);
        if(dishPhoto != null) {
            dishPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchChooseDialog();
                }
            });
        }
        this.currentDish = (Dish)getIntent().getSerializableExtra("Dish");
        if(this.currentDish != null)
        {
            // Edit existing dish
            dishID.setText(this.currentDish.getID());
            dishName.setText(this.currentDish.getName());
            setTitle(this.currentDish.getName()); // set Activity Title
            dishDesc.setText(this.currentDish.getDescription());
            dishQty.setText(Integer.toString(this.currentDish.getAvailability_qty()));
            dishPrice.setText(Double.toString(this.currentDish.getPrice()));

            // TODO: ImageView dishPhoto = (ImageView) EditDish.this.findViewById(R.id.dishPhoto);
            // TODO: dishPhoto.set .....




        }else
        {
            // Add new dish
            setTitle(R.string.new_dish);
        }

    }

    public void dispatchChooseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditDish.this);

        //Create dialog entries
        final String [] items = new String[] {EditDish.this.getResources().getString(R.string.take_photo),
                EditDish.this.getResources().getString(R.string.gallery_image)};
        final Integer[] icons = new Integer[] {R.drawable.ic_camera_alt_black_24dp, R.drawable.ic_collections_black_24dp,};
        ListAdapter adapter = new DialogArrayAdapter(EditDish.this, items, icons);

        builder.setTitle(EditDish.this.getResources().getString(R.string.alert_title))
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case (0):
                                takePhotoFromCamera();
                                break;
                            case (1):
                                takePhotoFromGallery();
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void takePhotoFromCamera() {
        Toast.makeText(EditDish.this, "Camera", Toast.LENGTH_SHORT).show();
    }

    private void takePhotoFromGallery() {
        Toast.makeText(EditDish.this, "Gallery", Toast.LENGTH_SHORT).show();
    }

}
