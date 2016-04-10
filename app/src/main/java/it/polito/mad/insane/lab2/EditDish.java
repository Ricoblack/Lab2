package it.polito.mad.insane.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditDish extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        // show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Nome del piatto");
    }
}
