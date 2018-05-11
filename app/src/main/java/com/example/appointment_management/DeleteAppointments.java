package com.example.appointment_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appointment_management.SQLite.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class DeleteAppointments extends AppCompatActivity {

    ArrayList<String> arrayList;
    List<Appointment> appointmentList;
    ArrayAdapter adapter;
    ListView list;
    DBHandler dbHandler;
    String date;
    Button delete;
    String appointmentNumber;
    EditText numberEText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_appointments);

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");

        delete = (Button) findViewById(R.id.deleteBtn);
        numberEText = (EditText) findViewById(R.id.number);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentNumber = numberEText.getText().toString();

                if(appointmentNumber.equals(null) || appointmentNumber.equals("") )
                {
                    numberEText.setError("Please Select a valid appointment Number");
                    numberEText.setText("");
                }

                else
                {
                    try
                    {
                        deleteAppointment("Would you like to delete event : ' " + appointmentList.get(Integer.parseInt(appointmentNumber)-1).getTitle() + " '?");


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


    public void deleteAppointment(String msg)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"Deleted the " + appointmentList.get(Integer.parseInt(appointmentNumber)-1).getTitle() + " appointment.",Toast.LENGTH_SHORT).show();
                dbHandler.deleteAppointments(date,appointmentList.get(Integer.parseInt(appointmentNumber)-1).getTitle());
                dialog.dismiss();

                finish();
                startActivity(getIntent());

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
