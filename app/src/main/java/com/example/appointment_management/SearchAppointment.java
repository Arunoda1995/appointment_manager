 package com.example.appointment_management;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment_management.SQLite.DBHandler;

import java.util.ArrayList;
import java.util.List;

 public class SearchAppointment extends AppCompatActivity {

    Button searchButton;
    EditText searchEText;

    ListView listView;

    List<Appointment> allAppointmentList;
    List<Appointment> selectAppointmentList;

    DBHandler dbHandler;
    PopupWindow popupWindow;
    String searchAppointment;

    AppointmentAdapter appointmentAdapter;
    TextView titleTView,timeTView,detailsTView;
    Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_appointment);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchEText = (EditText) findViewById(R.id.searchAppointment);

        dbHandler = new DBHandler(this,null,null,1);

        allAppointmentList = dbHandler.displayAllAppointments();

        listView = (ListView) findViewById(R.id.appointmentList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                appointment = appointmentAdapter.getItem(position);

                Toast.makeText(getBaseContext(),appointment.getTitle(),Toast.LENGTH_SHORT).show();

                LayoutInflater inflater = (LayoutInflater) SearchAppointment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.search_popup,(ViewGroup) findViewById(R.id.searchPopUp));

                titleTView = (TextView) layout.findViewById(R.id.appointmentTitle);

                timeTView = (TextView) layout.findViewById(R.id.appointmentTime);

                detailsTView = (TextView) layout.findViewById(R.id.appointmentDetails);

                popupWindow = new PopupWindow(layout, 1200, 900 ,  true);
                // display the popup in the center
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                titleTView.setText(appointment.getTitle());

                timeTView.setText(appointment.getTime());

                detailsTView.setText(appointment.getDetails());
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(searchEText.getText().toString().equals("")||searchEText.getText().toString().equals(null))
                    {

                        searchEText.setError("Please Enter Appointment Title");

                    }
                    else
                    {

                        selectAppointmentList = new ArrayList<>();

                        searchAppointment = searchEText.getText().toString();

                        for(Appointment appointment : allAppointmentList)
                        {

                            if(appointment.getTitle().contains(searchAppointment))
                            {
                                selectAppointmentList.add(appointment);

                            }

                        }

                        appointmentAdapter = new AppointmentAdapter(getBaseContext(),-1,selectAppointmentList);
                        listView.setAdapter(appointmentAdapter);


                        if(selectAppointmentList.size()==0)
                        {

                            Toast.makeText(getBaseContext(),"Cannot Find Any Appointments",Toast.LENGTH_SHORT).show();

                        }



                    }
                }
                catch (Exception e)
                {

                    Toast.makeText(getBaseContext(),"Cannot Find Any Appointments",Toast.LENGTH_SHORT).show();

                }

                searchEText.setText("");

            }
        });


    }




}
