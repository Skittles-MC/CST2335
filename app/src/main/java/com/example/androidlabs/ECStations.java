package com.example.androidlabs;

public class ECStations  {


    private String title ;
    private String latitude;

    private String longitude;

    private String phoneNo;



    private long id;


    public ECStations(){


    }


    public ECStations(String title, String latitude, String longitude, String phoneNo){
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;


    }

    public ECStations(long id, String title, String latitude, String longitude, String phoneNo){
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;
        this.id = id;

    }




    public String getTitle1() {
        return title;
    }

   // public void setTitle(String title) {
     //   this.title = title;
 //   }


    public String getLatitude() {
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }


    public String getPhoneNo() {
        return phoneNo;
    }




    public Long getId() {
        return id;
    }
}





