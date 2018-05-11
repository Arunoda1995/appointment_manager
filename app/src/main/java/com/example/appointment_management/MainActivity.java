package com.example.appointment_management;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.appointment_management.SQLite.DBHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private Button createAppointment,ediAppointment,deleteAppointment,moveAppointment,searchAppointments;
    CalendarView calendarView;
    private  String date;
    DBHandler dbHandler;
    PopupWindow popupWindow;
    Button deleteAll,deleteSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAppointment = (Button) findViewById(R.id.createBtn);
        ediAppointment = (Button) findViewById(R.id.editBtn);
        deleteAppointment = (Button) findViewById(R.id.deleteBtn);
        moveAppointment = (Button) findViewById(R.id.moveBtn);
        searchAppointments = (Button) findViewById(R.id.searchBtn);
        calendarView = (CalendarView) findViewById(R.id.calendar);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateSelected = simpleDateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                date = dateSelected;

            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateSelected = simpleDateFormat.format(new Date(calendarView.getDate()));
        date = dateSelected;
        dbHandler = new DBHandler(this, null, null, 1);

        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),SetAppointments.class);
                intent.putExtra("Date",date);
                startActivity(intent);

            }
        });


        ediAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EditAppointment.class);
                intent.putExtra("Date",date);
                startActivity(intent);

            }
        });

        deleteAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAppointmentPopUp(v);
            }
        });

        moveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MoveAppointments.class);
                intent.putExtra("Date",date);
                startActivity(intent);
            }
        });

        searchAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , SearchAppointment.class);
                startActivity(intent);
            }
        });

    }

public void deleteAppointmentPopUp(View v)
{

try
{
    final LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    final View layout = inflater.inflate(R.layout.delete_popup,(ViewGroup) findViewById(R.id.delete_popup));

    popupWindow = new PopupWindow(layout,1200,900,true);

    popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

    deleteAll = (Button) layout.findViewById(R.id.btnDeleteAll);

    deleteAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getBaseContext(),"Delete All the Appointments on" + date,Toast.LENGTH_LONG).show();
            dbHandler.deleteAppointments(date);
            popupWindow.dismiss();
        }
    });

    deleteSelected = (Button) layout.findViewById(R.id.btnDeleteSelected);

    deleteSelected.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), DeleteAppointments.class);
            intent.putExtra("Date",date);
            startActivity(intent);
            popupWindow.dismiss();
        }
    });


}
catch (Exception e)
{

e.printStackTrace();

}




}




}
