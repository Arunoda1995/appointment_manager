package com.example.appointment_management;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.appointment_management.SQLite.DBHandler;
import com.example.appointment_management.Thesaurus.ThesaurusAdapter;
import com.example.appointment_management.Thesaurus.XMLParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class SetAppointments extends AppCompatActivity {

    private EditText titleEText,timeEText,detailsEText, thesaurusEText;
    Button saveBtn,thesaurusBtn,detailsThesBtn;
    DBHandler dbHandler;
    String searchText;
    PopupWindow popupWindow;
    ListView thesaurusLView;
    ThesaurusAdapter thesaurusAdapter;

    //String URL = "http://thesaurus.altervista.org/thesaurus/v1?word=peace&language=en_US&key=SO962LBBYPb0zwUCw7lm&output=xml";



    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointments);

        titleEText = (EditText) findViewById(R.id.appointmentTitle);
        timeEText = (EditText) findViewById(R.id.appointmentTime);
        detailsEText = (EditText) findViewById(R.id.appointmentDetails);
        saveBtn = (Button) findViewById(R.id.btnSave);
        thesaurusBtn = (Button) findViewById(R.id.thesaurusBtn);
        detailsThesBtn = (Button) findViewById(R.id.detailsthesaurusBtn);
        dbHandler = new DBHandler(this, null, null, 1);
        thesaurusEText = (EditText) findViewById(R.id.searchText);
        
        final Intent intent = getIntent();
        date = intent.getStringExtra("Date");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        saveBtn.setOnClickListener(new View.OnClickListener() {


            /*public void showSoftKeyboard(View view) {
                if (view.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }*/

            @Override
            public void onClick(View v) {

               //showSoftKeyboard(v);

                String title = titleEText.getText().toString();
                String time = timeEText.getText().toString();
                String details = detailsEText.getText().toString();


                if(TextUtils.isEmpty(title))
                {

                    titleEText.setError("Please Set Appointment Title");
                    return;
                }

                else if(TextUtils.isEmpty(time))
                {

                    timeEText.setError("Please Set Appointment Time");
                    return;

                }

                else if(TextUtils.isEmpty(details))
                {
                    detailsEText.setError("Please Set the Appointment Details");
                    return;
                }
                else
                {
                    Appointment  appointment = new Appointment(date,time,title,details);
                    int status = dbHandler.createAppointment(appointment);
                    if(status == 1)
                    {
                        String msg = "Appointment " + title + " on " + date + " was created successfully.";
                        setMessage(msg);

                        titleEText.setText("");
                        timeEText.setText("");
                        detailsEText.setText("");
                    }

                    else if(status == -1)
                    {

                        String msg = "Appointment "+ title +" already exists, please choose a different event title";
                        setMessage(msg);


                    }

                }

            }
        });


        thesaurusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = thesaurusEText.getText().toString();

                if(searchText.equals(null)|| searchText.equals(""))
                {

                    thesaurusEText.setError("Please Enter a word to Search");

                }
                else
                {

                    thesaurusPopUp(v);

                }

            }
        });


        detailsThesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int startText = detailsEText.getSelectionStart();
                int endText = detailsEText.getSelectionEnd();

                searchText = detailsEText.getText().toString().substring(startText, endText);
                Toast.makeText(getBaseContext(),"Selected Word \"" + searchText + "\"",Toast.LENGTH_SHORT).show();

                thesaurusPopUp(v);

            }
        });

    }




    public void setMessage(String msg)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this );
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void thesaurusPopUp(View v)
    {

        try
        {
            LayoutInflater inflater =  (LayoutInflater) SetAppointments.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.thesaurus_popup,(ViewGroup)findViewById(R.id.thesaurusPopUp));

            popupWindow = new PopupWindow(layout,1300,1600,true);

            popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

            thesaurusLView = (ListView) layout.findViewById(R.id.thesaurusList);

            if(isNetWorkAvailable())
            {

                DownloadSitesTask downloadSites = new DownloadSitesTask();
                downloadSites.execute();

            }

            else
            {

                Toast.makeText(getBaseContext(),"No internet Connection. Please connect " +
                        "your device to the internet and try again",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

        }
        catch(Exception e)
        {

            e.printStackTrace();
        }


    }

    public boolean isNetWorkAvailable()
    {


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo !=null && networkInfo.isConnected();

    }



    public class DownloadSitesTask  extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            try
            {

                DownloadFromUrl("http://thesaurus.altervista.org/thesaurus/v1?word=" +searchText+ "&language=en_US&key=SO962LBBYPb0zwUCw7lm&output=xml",openFileOutput("synonyms.xml", Context.MODE_PRIVATE));

            }
            catch(FileNotFoundException e)
            {

                e.printStackTrace();

            }


            return null;
        }


        protected void onPostExecute(Void result)
        {

            thesaurusAdapter = new ThesaurusAdapter(SetAppointments.this,-1, XMLParser.getSynonyms(SetAppointments.this));
            thesaurusLView.setAdapter(thesaurusAdapter);

        }


    }



    public static void DownloadFromUrl(String URL, FileOutputStream fileOutputStream)
    {

        try
        {

            java.net.URL url = new URL(URL);

            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte data[] = new byte[1024];

            int count;

            while((count = bufferedInputStream.read(data)) != -1)
            {

                bufferedOutputStream.write(data,0,count);

            }

            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }

        catch (IOException e)
        {

           e.printStackTrace();

        }


    }

}
