package com.release.eztoll.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MUVI on 02-Mar-18.
 */

public class Complain implements Serializable{
    String issue = "";
    String customer_name = "";
    String mobile = "";
    String address = "";
    String payment = "";
    String service_type = "";
    String product_name = "";
    String model = "";

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    String priority = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id = "";

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
