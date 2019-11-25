package com.iteso.tanderomobile.utils;

/** Constats util class.*/
public abstract class Constants {
    /** User email key for SharedPreferences.*/
    public static final String CURRENT_USER_EMAIL = "CURRENT_USER_EMAIL";
    /** User password key for SharedPreferences.*/
    public static final String CURRENT_USER_PASSWORD = "CURRENT_USER_PASSWORD";
    /** User id key for SharedPreferences.*/
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    /** User tanda key for SharedPreferences.*/
    public static final String CURRENT_TANDA = "CURRENT_TANDA";
    /*Firebase constants*/
    //Collections
    /** */
    public static final String FB_COLLECTION_USERS = "users";
    /** */
    public static final String FB_COLLECTION_TANDA = "tanda";
    /** */
    public static final String FB_COLLECTION_USERTANDA = "user-tanda";
    // User's fields
    /** */
    public static final String FB_USER_EMAIL = "email";
    /** */
    public static final String FB_USER_IS_ACTIVATED = "isActivated";
    /** */
    public static final String FB_USER_IS_PRIVATE = "isPrivate";
    /** */
    public static final String FB_USER_PASSWORD = "password";
    /** */
    public static final String FB_USER_RATING_ORG = "ratingOrganizador";
    /** */
    public static final String FB_USER_RATING_PAR = "ratingParticipante";
    /** */
    public static final String FB_USER_TANDAS_OWNED = "tandasOwned";

    // Tanda's fields
    /** */
    public static final String FB_TANDA_QTY = "cantidadAportacion";
    /** */
    public static final String FB_TANDA_START_DATE = "diaInicio";
    /** */
    public static final String FB_TANDA_PAYMENT_DAY = "diasCobro";
    /** */
    public static final String FB_TANDA_PAYMENT_DATES = "fechasPago";
    /** */
    public static final String FB_TANDA_PAYMENT_FREQUENCY = "frecuenciaPago";
    /** */
    public static final String FB_TANDA_IS_CLOSED = "isClosed";
    /** */
    public static final String FB_TANDA_MAX_PAR = "maxParticipantes";
    /** */
    public static final String FB_TANDA_NAME = "name";
    /** */
    public static final String FB_TANDA_ORGANIZER = "organizador";
    /** */
    public static final String FB_TANDA_PAYMENTS_DONE = "pagosHechos";
    /** */
    public static final String FB_TANDA_UNIQUE_KEY = "uniqueKey";

    // User-Tanda's fields
    /** */
    public static final String FB_USERTANDA_IS_INVITED = "inInvited";
    /** */
    public static final String FB_USERTANDA_PAYMENT_INFO = "paymentInfo";
    /** */
    public static final String FB_USERTANDA_NAME = "name";


}
