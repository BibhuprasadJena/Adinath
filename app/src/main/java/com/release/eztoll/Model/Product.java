package com.release.eztoll.Model;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class Product implements Serializable{

    String product_name = "";
    String product_model = "";
    String product_make = "";
    boolean product_selection = false;
    String product_id = "";
    String customer_id = "";
    int selected = 0;


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }



    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public String getProduct_make() {
        return product_make;
    }

    public void setProduct_make(String product_make) {
        this.product_make = product_make;
    }


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }





}
