package com.example.androidlabs;

public class ECStations extends ECSfavorite {


    private String title;

    private String latitude;

    private String longitude;

    private String phoneNo;

    private String address;

    private boolean fav;


    public ECStations(){
        this.title = "Unknown";
        this.latitude = "Unknown";
        this.longitude = "Unknown";
        this.phoneNo = "Unknown";
        this.address = "Unknown";
        this.fav = false;
    }


    public ECStations(String title, String latitude, String longitude, String phoneNo, String address){
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;
        this.address = address;
        this.fav = false;
    }


    public ECStations(ECStations station){
        this(station.title,station.latitude,station.longitude,station.phoneNo,station.address);
        this.setFav(station.isFav());

    }


    public String getTitle1() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getLatitude() {
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }


    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

}





