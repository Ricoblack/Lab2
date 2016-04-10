package it.polito.mad.insane.lab2;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static RestaurateurJsonManager manager = null;
    private static final int REQUEST_IMAGE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditProfile.manager = RestaurateurJsonManager.getInstance();
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

    private void takePhotoFromGallery() {
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); // EXTERNAL_CONTENT_URI or INTERNAL_CONTENT_URI
        // start the image gallery intent
        startActivityForResult(imageGalleryIntent, REQUEST_IMAGE_GALLERY);
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

                    Bitmap finalImg = processImg(imgPath);
                    ImageView btnImg = (ImageView) findViewById(R.id.coverPhoto);
                    if(btnImg != null) {
                        btnImg.setImageBitmap(finalImg);
                    }
                    saveToInternalStorage(finalImg);

                }
                break;
            default:
                Toast.makeText(this, "Switch-case non trovato", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private Bitmap processImg(String pathToSave) {
            Bitmap resultImg;

            // decode in Bitmap
            resultImg = decodePhoto(pathToSave);

            resultImg = rotateImg(resultImg, pathToSave);

            return resultImg;
    }

    private Bitmap rotateImg(Bitmap img, String imgPath) {
        Bitmap resultImg = null;
        int rotationInDegrees;
        try
        {
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
            resultImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),img.getHeight(), matrix, true);

        } catch (IOException e)
        {
            Toast.makeText(this, "Impossible to rotate the image", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return resultImg;
    }

    private Bitmap decodePhoto(String pathToSave) {
        int scaleFactor = 1, targetH = 0, targetW = 0;

        // Get the dimensions of the View
        ImageView btnImg = (ImageView) findViewById(R.id.coverPhoto);

        if(btnImg != null) {
            targetH = btnImg.getHeight();
            targetW = btnImg.getWidth();
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToSave, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        if(photoW > targetW || photoH > targetH)
            // Compute the scaling ratio to avoid distortion
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(pathToSave, bmOptions);
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"cover.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            return "";
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                return "";
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage()
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f = new File(directory, "cover.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.coverPhoto);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            //nothing
            TextView tv = (TextView) findViewById(R.id.editCover);
            tv.setVisibility(View.GONE);
            return;
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

    public void saveData() {

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
            if(!String.valueOf(spinner.getSelectedItem()).equals(""))
                university = String.valueOf(spinner.getSelectedItem());
        }
        if((spinner = (Spinner) findViewById(R.id.cuisineSpinner)) != null) {
            if(!String.valueOf(spinner.getSelectedItem()).equals(""))
                cuisineType = String.valueOf(spinner.getSelectedItem());
        }

        Button b = (Button) findViewById(R.id.openingHour);
        String timeString = (String) b.getText();
        Date openingDate = null;
        if(!timeString.equals("Opening Hour")) {
            String[] parts = timeString.split(":");
            String hourString = parts[0];
            String minuteString = parts[1];
            int hour = Integer.parseInt(hourString);
            int minute = Integer.parseInt(minuteString);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, minute);
            openingDate = cal.getTime();
        }

        b = (Button) findViewById(R.id.closingHour);
        timeString = (String) b.getText();
        Date closingDate = null;
        if(!timeString.equals("Closing Hour")){
            String[] parts = timeString.split(":");
            String hourString = parts[0];
            String minuteString = parts[1];
            int hour = Integer.parseInt(hourString);
            int minute = Integer.parseInt(minuteString);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, minute);
            closingDate = cal.getTime();
        }

        this.manager = RestaurateurJsonManager.getInstance();
        RestaurateurProfile profile = new RestaurateurProfile(name, address, university, cuisineType, description, openingDate, closingDate,
                timeInfo, payment, services);
        //manager.setRestaurateurProfile(profile);
    }
}
