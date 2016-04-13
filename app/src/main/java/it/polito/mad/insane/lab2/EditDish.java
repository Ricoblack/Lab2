package it.polito.mad.insane.lab2;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import java.io.FileOutputStream;
import java.util.List;

public class EditDish extends AppCompatActivity {

    private static final int MY_GL_MAX_TEXTURE_SIZE = 1024; // compatible with almost all devices. To obtain the right value for each device use:   int[] maxSize = new int[1];
                                                            // (this needs an OpenGL context)                                                       GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
                                                            //                                                                                      myGLMaxTextureSize = maxSize[0];
    private static final int REQUEST_TAKE_PHOTO = 280;
    private static final int REQUEST_IMAGE_GALLERY = 157;
    private static final String PREFIX_IMAGE_NAME = "dishPhoto_";

    private static RestaurateurJsonManager manager = null;

    private DishesRecyclerAdapter adapter = null;
    Dish currentDish = null;
    EditText dishID;
    EditText dishName;
    EditText dishDesc;
    EditText dishQty;
    EditText dishPrice;
    ImageView dishPhoto;


    // FIXME: se modifichi l'immagine e ne hai già una salvata, tornando indietro anche senza salvare la imposta

    /* Listeners */
    View.OnClickListener saveDishFabListener = new View.OnClickListener(){

        @Override
        public void onClick(View view)
        {

            // check if all the required info are filled
            if(!isAllDataFilled()) {
                Toast.makeText(EditDish.this, R.string.error_some_empty_fill, Toast.LENGTH_SHORT).show();
                return;
            }

            // save data in manager
            if(EditDish.this.currentDish != null)
            {
                for (Dish d : EditDish.manager.getDishes())
                {
                    try
                    {
                        if (d.getID().equals(EditDish.this.currentDish.getID()))
                        {
                            // edit existing dish
                            d.setName(EditDish.this.dishName.getText().toString());
                            d.setDescription(EditDish.this.dishDesc.getText().toString());
                            d.setAvailability_qty(Integer.parseInt(EditDish.this.dishQty.getText().toString()));
                            d.setPrice(Double.parseDouble(EditDish.this.dishPrice.getText().toString()));
                            d.setPhotoPath(EditDish.this.currentDish.getPhotoPath());
                            //String photoPath = (String) EditDish.this.dishPhoto.getTag();
                            //if(photoPath != null)
                                //d.setPhotoPath(photoPath);
                            EditDish.manager.saveDbApp();
                            Toast.makeText(EditDish.this, R.string.confirm_save_dish, Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(EditDish.this, R.string.error_input_number, Toast.LENGTH_SHORT).show();
                    }
                }

                // Dish not found: add new dish
                try {
                    EditDish.this.currentDish.setName(EditDish.this.dishName.getText().toString());
                    EditDish.this.currentDish.setDescription(EditDish.this.dishDesc.getText().toString());
                    EditDish.this.currentDish.setAvailability_qty(Integer.parseInt(EditDish.this.dishQty.getText().toString()));
                    EditDish.this.currentDish.setPrice(Double.parseDouble(EditDish.this.dishPrice.getText().toString()));
                    //photoPath already set

                    //String photoPath = (String) EditDish.this.dishPhoto.getTag();
                    //if(photoPath != null)
                        //EditDish.this.currentDish.setPhotoPath(photoPath);

                    EditDish.manager.getDishes().add(EditDish.this.currentDish);
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
    };

    /* Standard Methods */
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
        if (fab != null)
            fab.setOnClickListener(saveDishFabListener);


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
            this.dishID.setText(this.currentDish.getID());
            this.dishName.setText(this.currentDish.getName());
            //setTitle(this.currentDish.getName()); // set Activity Title
            this.dishDesc.setText(this.currentDish.getDescription());
            this.dishQty.setText(Integer.toString(this.currentDish.getAvailability_qty()));
            this.dishPrice.setText(Double.toString(this.currentDish.getPrice()));
            String imgPath = this.currentDish.getPhotoPath();
            if(imgPath != null)
                this.dishPhoto.setImageURI(Uri.parse(imgPath));

        }else
        {
            try
            {
                // Crete new dish
                //setTitle(R.string.new_dish);
                this.currentDish = new Dish();
                // set ID
                this.currentDish.setID(getNextDishID(EditDish.manager.getDishes()));
            } catch (Exception e)
            {
                Toast.makeText(EditDish.this, R.string.error_create_newDish, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
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
                        String processedImgPath = processImg(imgPath);
                        this.dishPhoto.setImageURI(Uri.parse(processedImgPath));
                        //set tag in photo
                        //this.dishPhoto.setTag(processedImgPath);
                        //update info in activity
                        this.currentDish.setPhotoPath(processedImgPath);
                        finish();
                        startActivity(getIntent());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(EditDish.this, R.string.error_processing_img, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    /* Our Methods */


    /**
     * Method that copy the original img in the app internal directory and compress it
     * @param imgPath
     * @return the path of the new Img
     * @throws Exception
     */
    private String processImg(String imgPath) throws Exception
    {
        String imgName;
        // Take the original img and rotate it (if needed)
        Bitmap rotatedBitmapImg = rotateImg(imgPath);

        /** save bitmap into App Internal directory creating a compressed copy of it **/
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            // path: /data/data/<my_app>/app_imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // name: dishPhoto_<dishID>
            if(this.currentDish.getID() != null)
                imgName = PREFIX_IMAGE_NAME + this.currentDish.getID();
            else
                imgName = PREFIX_IMAGE_NAME + this.getNextDishID(EditDish.manager.getDishes());

            // Compress, scale and create img in: /data/data/<my_app>/app_data/imageDir/<imgName>
            File myImg = new File(directory, imgName);
            FileOutputStream fos = new FileOutputStream(myImg);

            /** scale photo **/
            int imgHeight = rotatedBitmapImg.getHeight();
            int imgWidth = rotatedBitmapImg.getWidth();
            int newImgHeight = imgHeight;
            int newImgWidth = imgWidth;
            int maxValue = Math.max(imgHeight,imgWidth);
            if(maxValue > MY_GL_MAX_TEXTURE_SIZE)
            {
                double scaleFactor = (double) maxValue / (double) MY_GL_MAX_TEXTURE_SIZE;
                newImgHeight = (int) (imgHeight / scaleFactor);
                newImgWidth = (int) (imgWidth / scaleFactor);
            }

            Bitmap bitmapImgScaled = Bitmap.createScaledBitmap(rotatedBitmapImg,newImgWidth,newImgHeight,false);
            bitmapImgScaled.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

        return myImg.getPath();
    }

    /**
     * Rotate the image which is located in the input imgPath
     * @param imgPath
     * @return the bitmap image rotated (if needed)
     * @throws Exception
     */
    private Bitmap rotateImg(String imgPath) throws Exception
    {
        int rotationInDegrees;
        Bitmap resultImg;

        // open image given
        File f = new File(imgPath);
        // obtain bitmap from original file
        Bitmap originalBitmapImg = BitmapFactory.decodeStream(new FileInputStream(f));

        // Reads Exif tags from the specified JPEG file.
        ExifInterface exif = new ExifInterface(imgPath);

        // find the current rotation
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        // Convert exif rotation to degrees:
        switch(rotation)
        {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotationInDegrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotationInDegrees = 180;
                break;
            case  ExifInterface.ORIENTATION_ROTATE_270:
                rotationInDegrees = 270;
                break;
            default:
                rotationInDegrees = 0;
                break;
        }

        // use the actual rotation of the image as a reference point to rotate the image using a Matrix
        Matrix matrix = new Matrix();
        if (rotation != 0f) // 0 float
            matrix.preRotate(rotationInDegrees);

        // create the new rotate img
        resultImg = Bitmap.createBitmap(originalBitmapImg, 0, 0, originalBitmapImg.getWidth(),originalBitmapImg.getHeight(), matrix, true);

        return resultImg;
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
     * Method that check if all the field of the activity are filled
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
