package com.example.appointment_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by ACER on 4/14/2018.
 */

public class AppointmentAdapter extends ArrayAdapter<Appointment> {


    TextView title,details;

    public AppointmentAdapter(Context context, int tvResourceId, List<Appointment> appointmentList)
    {
        super(context,tvResourceId,appointmentList);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup){
        RelativeLayout relativeLayout = (RelativeLayout)view;

        if(null == relativeLayout){
            //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            relativeLayout = (RelativeLayout)inflater.inflate(R.layout.appointment_row_list, null);
        }

        //initializing the two text views
        title = (TextView)relativeLayout.findViewById(R.id.title);
        details = (TextView)relativeLayout.findViewById(R.id.details);


        //Set the resulting synonym category and synonyms in the TextViews
        title.setText(getItem(pos).getTitle());
        details.setText(getItem(pos).getDetails());

        return relativeLayout;


    }


}
