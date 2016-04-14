package it.polito.mad.insane.lab2;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Renato on 08/04/2016.
 */
public class MySpinnerAdapter extends ArrayAdapter<String>
{
    public MySpinnerAdapter(Context context, int resource, List objects)
    {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position){
        return position != 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if(position == 0){
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);
        }
        else
            tv.setTextColor(Color.BLACK);

        return view;
    }
}
