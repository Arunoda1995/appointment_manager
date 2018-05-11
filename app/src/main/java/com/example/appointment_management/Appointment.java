package com.example.appointment_management;



public class Appointment {


    private String date;
    private String time;
    private String title;
    private String details;


    public Appointment(String date,String time,String title,String details)
    {

        this.date = date;
        this.time = time;
        this.title = title;
        this.details = details;


    }

    public String getDate()
    {

        return date;
    }

    public String getTime()
    {

        return  time;
    }

    public String getDetails()
    {

        return  details;
    }

    public String getTitle()
    {
        return title;

    }

    public void setTime(String time)
    {

        this.time = time;

    }

}
