package com.example.appointment_management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.PopupWindow;
import com.example.appointment_management.SQLite.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class EditAppointment extends AppCompatActivity {

    DBHandler dbHandler;
    Button edit,update;
    ListView list;
    List<Appointment> appointmentList;
    String date;
    ArrayList<String> arrayList;
    ArrayAdapter adapter;
    EditText numberEText;
    PopupWindow popupWindow;
    EditText titleEText,timeEText,detailsEText;
    String appointmentNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        edit = (Button) findViewById(R.id.editBtn);
        numberEText = (EditText) findViewById(R.id.number);
        Intent intent = getIntent();
        date = intent.getStringExtra("Date");

        dbHandler = new DBHandler(this, null, null, 1);
        arrayList = new ArrayList<>();
        appointmentList = dbHandler.viewAppointments(date);

        for(int i=0;i<appointmentList.size();i++)
        {

            arrayList.add(i+1 + ". " + appointmentList.get(i).getTime() + " " + appointmentList.get(i).getTitle());

        }

        adapter = new ArrayAdapter<String>(this,R.layout.appointments_listview ,arrayList);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentNumber = numberEText.getText().toString();

                if(appointmentNumber.equals(null) || appointmentNumber.equals(""))
                {
                    numberEText.setError("Please Select a valida Appointment Number");
                    numberEText.setText("");
                    return;

                }
                else
                {

                    try
                    {
                        editAppointmentPopUp(v);
                        numberEText.setText("");
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        numberEText.setText("");
                        Toast.makeText(getBaseContext(), "There's no appointment numbered " + numberEText +
                                ". Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        numberEText.setText("");
                        Toast.makeText(getBaseContext(),"Invalid input. Please try again with a valid number.", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


    }

public void editAppointmentPopUp(View v)
{

    try
    {
                LayoutInflater inflater = (LayoutInflater) EditAppointment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.edit_popup,(ViewGroup) findViewById(R.id.editPopUp));

                popupWindow = new PopupWindow(layout,1000,1500,true);

                popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

                titleEText = (EditText) layout.findViewById(R.id.appointmentTitle);
                timeEText = (EditText) layout.findViewById(R.id.appointmentTime);
                detailsEText = (EditText) layout.findViewById(R.id.appointmentDetails);


                update = (Button) layout.findViewById(R.id.btnUpdate);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try
                        {
                            int status = dbHandler.updateAppointments(appointmentList.get(Integer.parseInt(appointmentNumber)-1),
                                    timeEText.getText().toString(), titleEText.getText().toString(), detailsEText.getText().toString());

                            if (status == 1) {

                                Toast.makeText(getBaseContext(), "Successfully updated the appointment", Toast.LENGTH_LONG).show();

                            } else if (status == -1) {

                                Toast.makeText(getBaseContext(), "There's no appointment numbered " + appointmentNumber +
                                        ". Please try again with a valid number.", Toast.LENGTH_SHORT).show();

                            }

                            finish();
                            startActivity(getIntent());

                        }



                    catch (IndexOutOfBoundsException e){

                        Toast.makeText(getBaseContext(), "Couldn't find the specified appointment in the database." , Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }

                        timeEText.setText("");
                        titleEText.setText("");
                        detailsEText.setText("");
                        popupWindow.dismiss();

                    }
                });



    }
    catch(Exception e)
    {

        e.printStackTrace();

    }

}

}
