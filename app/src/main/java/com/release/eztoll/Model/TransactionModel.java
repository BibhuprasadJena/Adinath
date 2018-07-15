package com.release.eztoll.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class TransactionModel implements Serializable {

    String price = "";
    String date = "";
    String status = "";


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
