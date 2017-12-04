package com.piemicrosystems.hoodcop.object;

/**
 * Created by aangjnr on 20/06/2017.
 */

public class Address {

    String id;
    String name;
    String address;
    String latitude;
    String longitude;


    public Address(String id, String name, String address, String latitude, String lon) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = lon;


    }

    public Address() {


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String lon) {
        this.longitude = lon;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
