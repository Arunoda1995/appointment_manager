package com.example.appointment_management.Thesaurus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appointment_management.R;

import java.util.List;

/**
 * Created by ACER on 4/10/2018.
 */

public class ThesaurusAdapter extends ArrayAdapter<Synonym>{

    TextView categoryTView,synonymsTView;



  public ThesaurusAdapter(Context context,int textViewId,List<Synonym> synonymList)
  {
        super(context,textViewId,synonymList);
  }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        LinearLayout linearLayout = (LinearLayout)view;
        if(null == linearLayout){

            LayoutInflater inflater = (LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearLayout = (LinearLayout)inflater.inflate(R.layout.thesaurus_adapter, null);
        }


        categoryTView = (TextView)linearLayout.findViewById(R.id.categoryID);
        synonymsTView = (TextView)linearLayout.findViewById(R.id.synonymsID);



        categoryTView.setText(getItem(position).getCategory());
        synonymsTView.setText(getItem(position).getSynonyms());

        return linearLayout;


    }

}
