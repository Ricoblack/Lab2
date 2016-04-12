package it.polito.mad.insane.lab2;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EditDish extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 280;
    private static final int REQUEST_IMAGE_GALLERY = 157;

    private static RestaurateurJsonManager manager = null;

    private DishesRecyclerAdapter adapter = null;
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
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // check if all the required info are filled
                    if(!isAllDataFilled()) {
                        Toast.makeText(EditDish.this, R.string.error_some_empty_fill, Toast.LENGTH_SHORT).show();
                        return;
                    }

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

                                    EditDish.manager.saveDbApp();
                                    Toast.makeText(EditDish.this, R.string.confirm_save_dish, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Toast.makeText(EditDish.this, R.string.error_input_number, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else
                    {

                        try {
                            // Add new dish
                            Dish d = new Dish();
                            // find the highest ID actually used
                            try
                            {
                                d.setID(getNextDishID(EditDish.manager.getDishes()));
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                Toast.makeText(EditDish.this, R.string.error_save_dish, Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                            //d.setID();
                            d.setName(EditDish.this.dishName.getText().toString());
                            d.setDescription(EditDish.this.dishDesc.getText().toString());
                            d.setAvailability_qty(Integer.parseInt(EditDish.this.dishQty.getText().toString()));
                            d.setPrice(Double.parseDouble(EditDish.this.dishPrice.getText().toString()));
                            //d.setPhoto_name();
                            EditDish.manager.getDishes().add(d);
                            EditDish.manager.saveDbApp();
                            Toast.makeText(EditDish.this, R.string.confirm_add_dish, Toast.LENGTH_SHORT).show();
                            finish();
                        }catch( NumberFormatException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(EditDish.this, R.string.error_input_number, Toast.LENGTH_SHORT).show();
                        }



                    }
                }
            });
        }


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
                    displayChooseDialog();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath;

        switch(requestCode)
        {
            case REQUEST_IMAGE_GALLERY:
                if(resultCode == RESULT_OK) {
                    if (data == null)
                        break;
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    if (selectedImage == null)
                        break;

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
                    if (cursor == null) {
                        imgPath = selectedImage.getPath();
                    } else {
                        // Move to first row
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imgPath = cursor.getString(columnIndex);
                        cursor.close();
                    }

                    try
                    {
                        this.dishPhoto.setImageURI(processImg (imgPath));
                        // il nome dell'immagine va salvato nel DB
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(EditDish.this, R.string.error_processing_img, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            default:
                Toast.makeText(this, "Switch-case non trovato", Toast.LENGTH_LONG).show();
                break;
        }
    }


    /* Our Methods */

    /**
     * Method that copy the original img in the app internal directory and compress it
     * @param imgPath
     * @return the URI of the new Img
     * @throws Exception
     */
    private Uri processImg(String imgPath) throws Exception
    {
        // open image given from gallery
        File f = new File(imgPath);

        // obtain bitmap from original file
        Bitmap bitmapImg = BitmapFactory.decodeStream(new FileInputStream(f));

        /* save bitmap into App Internal directory creating a compressed copy of it */
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            // path: /data/data/<my_app>/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // name: dishPhoto_<dishID>
            String imgName = "dishPhoto_"+this.currentDish.getID();

            // Compress and create img in: /data/data/<my_app>/app_data/imageDir/<imgName>
            File myImg = new File(directory, imgName);
            FileOutputStream fos = new FileOutputStream(myImg);
        // TODO : PORCO DEMONIO QUESTA CAZZO DI IMMAGINE VA FATTA SCALARE

        /*
        *  1- Vanno prese le massime dimensioni che openGL può supportare sul device corrente
        *                   int[] maxSize = new int[1];
        *                   GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        *
        *                   a questo punto in maxSize[0] c'è il valore massimo di risoluzione che l'assex o l'assey possono supportare
        *   2- sapendo ciò, bisogna verificare se la risoluzione corrente dell'immagine supera questi limiti in una delle due direzioni
        *   3- se i limiti sono superati in una delle due dimensioni va scalata l'immagine (usando bitmap.createScaledBitmap() mantenendo le proporzioni originali ma riducendo la risoluzione
        *   4- l'immagine va compressa (usando .compress())
        */
            //Bitmap bitmapImgScaled = Bitmap.createScaledBitmap(bitmapImg,4096,4096,false);
            //bitmapImgScaled.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();

        /*        *****************        */

        return Uri.parse(myImg.getPath());
    }

    public void displayChooseDialog() {

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
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); // EXTERNAL_CONTENT_URI or INTERNAL_CONTENT_URI
        // start the image gallery intent
        startActivityForResult(imageGalleryIntent, REQUEST_IMAGE_GALLERY);
    }

    /**
     * Method that get the max ID used in the input list and return the next ID to use (maxID +1)
     * @param dishes
     * @return next ID to use
     * @throws NumberFormatException
     */
    private String getNextDishID(List<Dish> dishes) throws Exception
    {
        int maxID = 0;

        if(!dishes.isEmpty())
            for(Dish d: dishes)
            {
                int tempID = Integer.parseInt(d.getID());
                if(tempID > maxID)
                    maxID = tempID;
            }

        maxID ++;
        return Integer.toString(maxID);

    }

    /**
     * Method that check if all
     * @return
     */
    private boolean isAllDataFilled()
    {
        //this.dishID.getText().toString().trim().length() > 0
        if(this.dishName.getText().toString().trim().length() > 0 &&
                this.dishDesc.getText().toString().trim().length() > 0 &&
                this.dishPrice.getText().toString().trim().length() > 0 &&
                this.dishQty.getText().toString().trim().length() > 0)
            return  true;
        else
            return false;

    }
}
