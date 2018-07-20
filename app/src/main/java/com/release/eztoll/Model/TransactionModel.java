package com.release.eztoll.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class TransactionModel implements Serializable {

    String price = "";
    String date = "";
    String status = "";

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    String vno = "";
    String vowner = "";




    public String getVowner() {
        return vowner;
    }

    public void setVowner(String vowner) {
        this.vowner = vowner;
    }


    public String getToll_name() {
        return toll_name;
    }

    public void setToll_name(String toll_name) {
        this.toll_name = toll_name;
    }

    String toll_name = "";


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
