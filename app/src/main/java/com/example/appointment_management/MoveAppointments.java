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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.appointment_management.SQLite.DBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class MoveAppointments extends AppCompatActivity {

    ArrayList<String> arrayList;
    List<Appointment> appointmentList;
    ArrayAdapter adapter;
    ListView list;
    DBHandler dbHandler;
    String date;
    Button move;
    String appointmentNumber;
    EditText numberEText;
    PopupWindow popupWindow;
    CalendarView calendarView;
    String moveDate;
    Button popUpMove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_appointments);

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        dbHandler = new DBHandler(this, null, null, 1);
        arrayList = new ArrayList<>();
        appointmentList = dbHandler.viewAppointments(date);



        numberEText = (EditText) findViewById(R.id.number);
        move = (Button) findViewById(R.id.moveBtn);

        for(int i=0;i<appointmentList.size();i++)
        {

            arrayList.add(i+1 + ". " + appointmentList.get(i).getTime() + " " + appointmentList.get(i).getTitle());

        }

        adapter = new ArrayAdapter<String>(this,R.layout.appointments_listview ,arrayList);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentNumber = numberEText.getText().toString();

                if(appointmentNumber.equals(null) || appointmentNumber.equals(""))
                {

                    numberEText.setError("Please select a valid appointment number");
                    numberEText.setText("");
                    return;
                }

                else
                {

                    try
                    {

                        moveAppointment(v);


                    }
                    catch (IndexOutOfBoundsException e){
                        numberEText.setText("");
                        Toast.makeText(getBaseContext(), "There's no appointment numbered " + appointmentNumber +
                                ". Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        numberEText.setText("");
                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }


    public  void moveAppointment(View v)
    {

        try
        {
            LayoutInflater inflater = (LayoutInflater) MoveAppointments.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View layout = inflater.inflate(R.layout.move_popup,(ViewGroup) findViewById(R.id.movePopUp));

            popupWindow = new PopupWindow(layout,1200,1400,true);

            popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

            calendarView = (CalendarView) layout.findViewById(R.id.calendar);

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateSelected = simpleDateFormat.format(new GregorianCalendar(year,month,dayOfMonth).getTime());
                    moveDate = dateSelected;


                }
            });

            popUpMove = (Button) layout.findViewById(R.id.moveBtn);

            popUpMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        dbHandler.moveAppointment(appointmentList.get(Integer.parseInt(appointmentNumber)-1),moveDate);
                        finish();
                        startActivity(getIntent());

                    }
                    catch (IndexOutOfBoundsException e){

                        Toast.makeText(getBaseContext(), "Couldn't find the specified appointment in the database." , Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }
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
