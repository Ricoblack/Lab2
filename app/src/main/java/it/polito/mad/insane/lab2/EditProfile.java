package it.polito.mad.insane.lab2;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_IMAGE_GALLERY = 0;
    private SharedPreferences preferences;
    static final String PREFS_NAME = "MyPrefsFile";
    private static String mCurrentPhotoPath;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhotoFromGallery();
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
        cSpinner.setAdapter(cAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);
        // If user change the default selection
        // First item is disable and it is used for hint
        if(position > 0){
            // Notify the selected item text
            Toast.makeText
                    (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                    .show();
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

    private ViewTreeObserver.OnGlobalLayoutListener loadingLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() //Callback method to be invoked when the global layout state or the visibility of views within the view tree changes
        {
            // Here your view is already layed out and measured for the first time
            ImageView btnImg = (ImageView) findViewById(R.id.editCoverPhoto);
            EditProfile.this.preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor ed = EditProfile.this.preferences.edit();
            if(btnImg != null) {
                ed.putInt("Height", btnImg.getHeight());
                ed.putInt("Width", btnImg.getWidth());
            }
            ed.commit();
        }
    };

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

                    String pathToSave;
                    pathToSave = imgPath;
                    Bitmap finalImg = processImg(pathToSave); // use an array in order to let the method to update it
                    ImageView btnImg = (ImageView) findViewById(R.id.editCoverPhoto);
                    if(btnImg != null) {
                        btnImg.setImageBitmap(finalImg);
                        btnImg.setTag(pathToSave); // save the image path as a Tag
                        this.path = (String) btnImg.getTag();
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
        ImageView btnImg = (ImageView) findViewById(R.id.editCoverPhoto);

        this.preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if(this.preferences != null){
            targetH = this.preferences.getInt("Height", 0);
            targetW = this.preferences.getInt("Width", 0);
        }

        if(targetH == 0 || targetW == 0)
            if(btnImg != null) {
                targetH = btnImg.getHeight();
                targetW = btnImg.getWidth();
            }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        if(photoW > targetW || photoH > targetH)
            // Compute the scaling ratio to avoid distortion
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(path, bmOptions);
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            // bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 0, fos);
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

}
