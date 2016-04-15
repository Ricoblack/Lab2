package it.polito.mad.insane.lab2;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int MY_GL_MAX_TEXTURE_SIZE = 1024;
    private static RestaurateurJsonManager manager = null;
    private static final int REQUEST_IMAGE_GALLERY = 581;
    private static Bitmap tempCoverPhoto = null;

    // FIXME: quando setto l'immagine in modalità portrait (o land) e poi la giro, l'immagine scompare

    /** Standard Methods **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EditProfile.manager = RestaurateurJsonManager.getInstance(this);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView img = (ImageView) findViewById(R.id.coverPhoto);
        if(img != null) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhotoFromGallery();
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Spinner uSpinner = (Spinner) findViewById(R.id.universitySpinner);
        List<String> universities = new ArrayList<>();
        Resources res = getResources();
        String[] uStrings = res.getStringArray(R.array.university_array);
        Collections.addAll(universities, uStrings);
        MySpinnerAdapter uAdapter = new MySpinnerAdapter(EditProfile.this, R.layout.support_simple_spinner_dropdown_item,
                universities);
        uAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        uSpinner.setAdapter(uAdapter);

        final Spinner cSpinner = (Spinner) findViewById(R.id.cuisineSpinner);
        List<String> cuisines = new ArrayList<>();
        res = getResources();
        String[] cStrings = res.getStringArray(R.array.cuisine_array);
        Collections.addAll(cuisines, cStrings);
        MySpinnerAdapter cAdapter = new MySpinnerAdapter(EditProfile.this, R.layout.support_simple_spinner_dropdown_item,
                cuisines);
        cAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if(cSpinner != null)
            cSpinner.setAdapter(cAdapter);

        //set image if available
        loadImageFromStorage();
        loadDataFromJson();

        // Fix Portrait Mode
        if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL ||
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(tempCoverPhoto != null){
            ImageView iv = (ImageView) findViewById(R.id.coverPhoto);
            iv.setImageBitmap(tempCoverPhoto);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);
        // If user change the default selection
        // First item is disable and it is used for hint
        if(position > 0){
            // Notify the selected item text
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void takePhotoFromGallery()
    {
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); // EXTERNAL_CONTENT_URI or INTERNAL_CONTENT_URI
        // start the image gallery intent
        startActivityForResult(imageGalleryIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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

                    try {
                        tempCoverPhoto = processImg(imgPath);
                    } catch (Exception e) {
                        Toast.makeText(EditProfile.this, R.string.error_processing_img, Toast.LENGTH_LONG).show();
                    }

                }
                break;
            default:
//                Toast.makeText(this, "Switch-case non trovato", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /** Our Methods **/

    /**
     * Method that copy the original img in the app internal directory and compress it
     * @param imgPath
     * @return the URI of the new Img
     * @throws Exception
     */
    private Bitmap processImg(String imgPath) throws Exception
    {
        Bitmap rotatedBitmapImg = rotateImg(imgPath);

        /** scale photo **/
        int imgHeight = rotatedBitmapImg.getHeight();
        int imgWidth = rotatedBitmapImg.getWidth();
        int newImgHeight = imgHeight;
        int newImgWidth = imgWidth;
        int maxValue = Math.max(imgHeight,imgWidth);
        if(maxValue > MY_GL_MAX_TEXTURE_SIZE){
            double scaleFactor = (double) maxValue / (double) MY_GL_MAX_TEXTURE_SIZE;
            newImgHeight = (int) (imgHeight / scaleFactor);
            newImgWidth = (int) (imgWidth / scaleFactor);
        }

        Bitmap bitmapImgScaled = Bitmap.createScaledBitmap(rotatedBitmapImg, newImgWidth ,newImgHeight, false);

        ImageView iv = (ImageView) findViewById(R.id.coverPhoto);
        if (iv != null) {
            iv.setImageBitmap(bitmapImgScaled);
        }

        return bitmapImgScaled;
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

    private void loadImageFromStorage()
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f = new File(directory, "restaurant_cover.jpg");
            if(!f.exists()){
                throw new FileNotFoundException();
            }
            ImageView img = (ImageView)findViewById(R.id.coverPhoto);
            if (img != null)
                img.setImageURI(Uri.parse(f.getPath()));
        }
        catch (FileNotFoundException e)
        {
            TextView tv = (TextView) findViewById(R.id.editCover);
            if (tv != null) {
                tv.setVisibility(View.GONE); //FIXME cambiando l'immagine quando c'è quella di default non compare
            }
        }
    }

    private void saveImageOnStorage(){
        /** save bitmap into App Internal directory creating a compressed copy of it **/
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path: /data/data/<my_app>/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Compress and create img in: /data/data/<my_app>/app_data/imageDir/<imgName>
        File myImg = new File(directory, "restaurant_cover.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myImg);
            tempCoverPhoto.compress(Bitmap.CompressFormat.JPEG, 100, fos); // FIXME: java.lang.NullPointerException: Attempt to invoke virtual method 'boolean android.graphics.Bitmap.compress(android.graphics.Bitmap$CompressFormat, int, java.io.OutputStream)' on a null object reference
            fos.close();
            tempCoverPhoto = null;
        } catch (java.io.IOException e) {
            Toast.makeText(EditProfile.this, R.string.error_save_image, Toast.LENGTH_LONG).show();
        }
    }

    public void showTimePickerDialog(View view) {
        switch (view.getId()){
            case(R.id.openingHour):
                DialogFragment openingFragment = new TimePickerFragment();
                openingFragment.show(getSupportFragmentManager(), "openingPicker");
                break;
            case (R.id.closingHour):
                DialogFragment closingFragment = new TimePickerFragment();
                closingFragment.show(getSupportFragmentManager(), "closingPicker");
                break;
        }
    }

    private void loadDataFromJson() {
        manager = RestaurateurJsonManager.getInstance(this);
        RestaurateurProfile profile = manager.getRestaurateurProfile();

        EditText et;

        et = (EditText) findViewById(R.id.editName);
        if (et != null) {
            if(profile.getRestaurantName() != null)
                et.setText(profile.getRestaurantName());
        }
        et = (EditText) findViewById(R.id.editAddress);
        if (et != null) {
            if(profile.getAddress() != null)
                et.setText(profile.getAddress());
        }
        et = (EditText) findViewById(R.id.editDescription);
        if (et != null) {
            if(profile.getDescription() != null)
                et.setText(profile.getDescription());
        }
        et = (EditText) findViewById(R.id.editTimeNotes);
        if (et != null) {
            if(profile.getTimeInfo() != null)
                et.setText(profile.getTimeInfo());
        }
        et = (EditText) findViewById(R.id.editPayment);
        if (et != null) {
            if(profile.getPaymentMethod() != null)
                et.setText(profile.getPaymentMethod());
        }
        et = (EditText) findViewById(R.id.editServices);
        if (et != null) {
            if(profile.getAdditionalServices() != null)
                et.setText(profile.getAdditionalServices());
        }

        Spinner spinner;
        spinner = (Spinner) findViewById(R.id.universitySpinner);
        if(spinner != null){
            String[] universities = getResources().getStringArray(R.array.university_array);
            for(int i=0; i<universities.length; i++){
                if (universities[i].equals(profile.getNearbyUniversity()))
                    spinner.setSelection(i);
            }
        }
        spinner = (Spinner) findViewById(R.id.cuisineSpinner);
        if(spinner != null){
            String[] cuisines = getResources().getStringArray(R.array.cuisine_array);
            for(int i=0; i<cuisines.length; i++){
                if (cuisines[i].equals(profile.getCuisineType()))
                    spinner.setSelection(i);
            }
        }

        Button button;
        button = (Button) findViewById(R.id.openingHour);
        if(button != null){
            Date date = profile.getOpeningHour();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            button.setText(String.format("%d:%d", hourOfDay, minute));
        }
        button = (Button) findViewById(R.id.closingHour);
        if(button != null){
            Date date = profile.getClosingHour();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            button.setText(String.format("%d:%d", hourOfDay, minute));
        }
    }

    public void saveData() {

        if(tempCoverPhoto != null)
            saveImageOnStorage();

        String name = null, address = null, description = null, payment = null, timeInfo = null, university = null,
                cuisineType = null, services = null;

        EditText et;
        if ((et = (EditText) findViewById(R.id.editName)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                name = String.valueOf(et.getText());
        }
        if ((et = (EditText) findViewById(R.id.editAddress)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                address = String.valueOf(et.getText());
        }
        if ((et = (EditText) findViewById(R.id.editDescription)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                description = String.valueOf(et.getText());
        }
        if ((et = (EditText) findViewById(R.id.editPayment)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                payment = String.valueOf(et.getText());
        }
        if ((et = (EditText) findViewById(R.id.editTimeNotes)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                timeInfo = String.valueOf(et.getText());
        }
        if ((et = (EditText) findViewById(R.id.editServices)) != null) {
            if(!String.valueOf(et.getText()).equals(""))
                services = String.valueOf(et.getText());
        }

        Spinner spinner;
        if((spinner = (Spinner) findViewById(R.id.universitySpinner)) != null) {
            String[] universities = getResources().getStringArray(R.array.university_array);
            if(!String.valueOf(spinner.getSelectedItem()).equals(universities[0])) {
                university = String.valueOf(spinner.getSelectedItem());
            }
        }
        if((spinner = (Spinner) findViewById(R.id.cuisineSpinner)) != null) {
            String[] cuisines = getResources().getStringArray(R.array.cuisine_array);
            if(!String.valueOf(spinner.getSelectedItem()).equals(cuisines[0])) {
                cuisineType = String.valueOf(spinner.getSelectedItem());
            }
        }

        Button b = (Button) findViewById(R.id.openingHour);
        String timeString = (String) b.getText();
        Date openingDate = null;
        if(!timeString.equals(getResources().getString(R.string.select))) {
            String[] parts = timeString.split(":");
            String hourString = parts[0];
            String minuteString = parts[1];
            int hour = Integer.parseInt(hourString);
            int minute = Integer.parseInt(minuteString);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            openingDate = cal.getTime();
        }

        b = (Button) findViewById(R.id.closingHour);
        timeString = (String) b.getText();
        Date closingDate = null;
        if(!timeString.equals(getResources().getString(R.string.select))){
            String[] parts = timeString.split(":");
            String hourString = parts[0];
            String minuteString = parts[1];
            int hour = Integer.parseInt(hourString);
            int minute = Integer.parseInt(minuteString);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            closingDate = cal.getTime();
        }

        manager = RestaurateurJsonManager.getInstance(this);
        RestaurateurProfile profile = new RestaurateurProfile(name, address, university, cuisineType, description, openingDate, closingDate,
                timeInfo, payment, services);
        manager.setRestaurateurProfile(profile);
        manager.saveDbApp();

        Toast.makeText(this, R.string.confirm_profile_updated, Toast.LENGTH_SHORT).show();
        finish();
    }
}
