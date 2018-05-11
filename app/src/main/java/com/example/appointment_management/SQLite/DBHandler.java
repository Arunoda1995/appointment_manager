package com.example.appointment_management.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appointment_management.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 4/6/2018.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "APPOINTMENT_DB.db";
    public static final String TABLE_NAME = "APPOINTMENT";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DETAILS = "details";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version)
    {
        super(context,DATABASE_NAME,cursorFactory,DATABASE_VERSION);

    }

    //Creates the table and the column
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = " CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_DATE + " TEXT ," +
                COLUMN_TIME + " DATETIME ," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_DETAILS + " TEXT " +
                ");";

        db.execSQL(query);
    }

    //drops the current table if exists and  execute the OnCreate method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //create Appointment
    public int createAppointment(Appointment appointment)
    {
        SQLiteDatabase sqLiteDatabase  = getWritableDatabase();

        String sql = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_DATE + "=\'" + appointment.getDate() + "\'" + " AND " + COLUMN_TITLE
                + "=\'" + appointment.getTitle() + "\';";

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        if(cursor == null || !cursor.moveToFirst())
        {

           ContentValues contentValues = new ContentValues();

           contentValues.put(COLUMN_DATE, appointment.getDate());
           contentValues.put(COLUMN_TIME,appointment.getTime());
           contentValues.put(COLUMN_TITLE,appointment.getTitle());
           contentValues.put(COLUMN_DETAILS,appointment.getDetails());

           sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
           sqLiteDatabase.close();
           cursor.close();
           return 1;

        }

        else
        {
            return -1;

        }

    }


    //Goes through the database and returns all the appointments in the database
    public List<Appointment> viewAppointments(String date)
    {

        List<Appointment> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=\'" + date + "\'"
                + " ORDER BY " + COLUMN_TIME + " ASC";

        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {

            if(cursor.getString(cursor.getColumnIndex("title")) != null)
            {
                Appointment appointment = new Appointment(cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("details")));
                list.add(appointment);
            }

            cursor.moveToNext();

        }

        sqLiteDatabase.close();
        return list;


    }

    //Update an Appointment
    public int updateAppointments(Appointment appointment,String time,String title,String details)
    {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_DATE + "=\'" + appointment.getDate() + "\'" + " AND " +
                COLUMN_TITLE + "=\'" + appointment.getTitle() + "\';";


        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        if(cursor == null || !cursor.moveToFirst())
        {

           return -1;

        }

        else
        {

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_TIME,time);
            contentValues.put(COLUMN_TITLE,title);
            contentValues.put(COLUMN_DETAILS,details);

            sqLiteDatabase.update(TABLE_NAME, contentValues , COLUMN_DATE + "='" + appointment.getDate() + "'" + " AND " +
                    COLUMN_TITLE + "='" + appointment.getTitle() + "'" , null);

            sqLiteDatabase.close();
            cursor.close();
            return 1;

        }


    }

    //Delete Appointment
    public void deleteAppointments(String date)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=\'" + date + "\';");

        sqLiteDatabase.close();
    }

    public void deleteAppointments(String date,String title)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=\'" + date + "\'"
                + " AND " + COLUMN_TITLE + "=\'" + title + "\';");
        db.close();


    }

    //Move Appointment
    public int moveAppointment(Appointment appointment,String date)
    {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_DATE + "=\'" + appointment.getDate() + "\'" + " AND " +
                COLUMN_TITLE + "=\'" + appointment.getTitle() + "\';";

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        if(cursor == null || !cursor.moveToFirst())
        {

            return  -1;

        }

        else
        {
            ContentValues contentValues = new ContentValues();


            contentValues.put(COLUMN_DATE, date);

            sqLiteDatabase.update(TABLE_NAME, contentValues , COLUMN_DATE + "='" + appointment.getDate() + "'" + " AND " +
                    COLUMN_TITLE + "='" + appointment.getTitle() + "'" , null);
            sqLiteDatabase.close();
            cursor.close();
            return 1;

        }

    }


    public List<Appointment> displayAllAppointments()
    {

        List<Appointment> appointmentList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " ;";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {

            if(cursor.getString(cursor.getColumnIndex("title"))!= null)
            {

                Appointment appointment = new Appointment(cursor.getString(cursor.getColumnIndex("date")) ,
                        cursor.getString(cursor.getColumnIndex("time")) ,
                        cursor.getString(cursor.getColumnIndex("title")) ,
                        cursor.getString(cursor.getColumnIndex("details")) );
                appointmentList.add(appointment);

            }

            cursor.moveToNext();

        }
        db.close();
        return  appointmentList;
    }

}
