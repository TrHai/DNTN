package com.example.bruce.myapp;

/**
 * Created by BRUCE on 9/11/2017.
 */

public class Server {
    public  static String localHost = "hiwhereami.000webhostapp.com";
    public static String url_TouristLocation = "http://"+localHost+"/getdiadiemdulich.php";
    public static String url_MoreInfomation = "http://"+localHost+ "/getchitietdulich.php?iddulich=";
    public static String url_RatingStar = "http://"+localHost+"/getratingstar.php";
    public static String url_SearchName= "http://"+localHost+ "/search.php?name=";
    public static String url_PostRating="https://"+localHost+"/postRating.php";

    public static String url_TouristLocation_en = "http://"+localHost+"/getLocation.php";
    public static String url_MoreInfomation_en = "http://"+localHost+ "/getDetailsLocation.php?iddulich=";

}
