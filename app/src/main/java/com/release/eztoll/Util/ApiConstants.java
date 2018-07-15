package com.release.eztoll.Util;

/**
 * Created by MUVI on 10-Feb-18.
 */

public class ApiConstants {

    public static final String BASE_URL = "http://adinathtech.in/";
//    public static final String BASE_URL = "http://stage.alysiantech.com/agent_app/";

    public static final String LOGIN = "login";
    public static String LOGIN_BASE_URL = BASE_URL+"users/";

    public static final String LOGOUT = "logout";
    public static final String LOGOUT_BASE_URL =  BASE_URL+"src/public/";

    public static final String ASSIGNED_COMPLAIN = "listAssigned";
    public static final String ASSIGNED_COMPLAIN_BASE_URL =  BASE_URL+"complain/";

    public static final String UPDATE_ASSIGNED_COMPLAIN = "save";
    public static final String UPDATE_ASSIGNED_COMPLAIN_BASE_URL =  BASE_URL+"complain/";

    public static final String FORGOT_PASSWORD = "";
    public static final String FORGOT_PASSWORD_BASE_URL = "";

    public static final String ADD_CUSTOMER = "save";
    public static final String ADD_CUSTOMER_BASE_URL =  BASE_URL+"customer/";

    public static final String PRODUCT_LIST = "listProduct";
    public static final String PRODUCT_LIST_BASE_URL =  BASE_URL+"product/";

    public static final String CHANGE_PASSWORD = "changePassword";
    public static final String CHANGE_PASSWORD_BASE_URL =  BASE_URL+"users/";

    public static final String OTP_VALIDATE = "checkOtp";
    public static final String OTP_VALIDATE_BASE_URL =  BASE_URL+"users/";



    public static final String CREATE_OTP = "createOtp";
    public static final String CREATE_OTP_BASE_URL =  BASE_URL+"users/";

    public static final String CUSTOMER_ENQUERY = "";
    public static final String CUSTOMER_ENQUERY_BASE_URL =  BASE_URL+"customer/";

    public static final String PRODUCT_USER_DETAILS_FOR_COMPLAIN = "searchCustomer";
    public static final String PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL =  BASE_URL+"customer/";

    public static final String REGISTER_COMPLAIN = "customerComplinsave";
    public static final String REGISTER_COMPLAIN_BASE_URL =  BASE_URL+"complain/";
}


