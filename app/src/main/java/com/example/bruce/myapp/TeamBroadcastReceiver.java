package com.example.bruce.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by BRUCE on 12/2/2017.
 */

public class TeamBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        Toast.makeText(context, "Location change", Toast.LENGTH_SHORT).show();
        if(isNetWorkAvailable(context,type) == true){
            Toast.makeText(context, "No sdasdasdInternet !!!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "No Internet 123123123!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNetWorkAvailable(Context context, int[] typeNetowrks){
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int typeNetwork : typeNetowrks){
                NetworkInfo networkInfo = cm.getNetworkInfo(typeNetwork);
                if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){

                    return true;
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
