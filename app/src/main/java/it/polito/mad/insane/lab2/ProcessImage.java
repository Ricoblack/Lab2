package it.polito.mad.insane.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Renato on 11/04/2016.
 */
public class ProcessImage {


    public static Bitmap processing(String pathToSave, ImageView btnImg) {
        Bitmap resultImg;

        // decode in Bitmap
        resultImg = ProcessImage.decodePhoto(pathToSave, btnImg);

        resultImg = ProcessImage.rotateImg(resultImg, pathToSave);

        return resultImg;
    }

    public static Bitmap rotateImg(Bitmap img, String imgPath) {
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
//            Toast.makeText(this, "Impossible to rotate the image", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
        return resultImg;
    }

    public static Bitmap decodePhoto(String pathToSave, ImageView btnImg) {
        int scaleFactor = 1, targetH = 0, targetW = 0;

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

}
