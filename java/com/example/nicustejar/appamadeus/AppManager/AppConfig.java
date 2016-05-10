package com.example.nicustejar.appamadeus.AppManager;

/**
 * Class for storing static values for configuration:
 * WebServer details, display messages, bundles.
 */
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://109.166.212.237/users/session";

    // Server user login url
    public static String URL_ADD_MEASURE = "http://109.166.212.237/measurements";

    // Messages
    public static String LOG_IN_SUCCES = "1";
    public static String LOG_IN_FAIL = "0";
    public static String QUERY_SUCCES = "1";
    public static String QUERY_FAIL = "0";

    // Display messages
    public static String LOG_IN_SUCCES_MESSAGE = "Autentificare reusita";
    public static String LOG_IN_FAIL_MESSAGE = "Eroare la autentificaree";
    public static String QUERY_SUCCES_MESSAGE = "Trimis cu succes";
    public static String QUERY_FAIL_MESSAGE = "Eroare la trimitere";

    // Bundles
    public static String BUNDLE_ID = "idContor";
    public static String BUNDLE_INDEX = "indexContor";
    public static String BUNDLE_USERNAME = "username";
    public static String BUNDLE_PASS = "password";
    public static String RESPONSE_MSG = "responseMessage";
    public static String RESPONSE_LOG_IN = "isLoggedIn";
}
