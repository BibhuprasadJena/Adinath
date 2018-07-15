package com.release.eztoll.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class TollModel implements Serializable {

    String tollname = "";
    String address = "";
    String price = "";
    String latitide = "";
    String longitude = "";

    public String getTollname() {
        return tollname;
    }

    public void setTollname(String tollname) {
        this.tollname = tollname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLatitide() {
        return latitide;
    }

    public void setLatitide(String latitide) {
        this.latitide = latitide;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
