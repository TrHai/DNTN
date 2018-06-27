package com.example.bruce.myapp.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruce.myapp.Data.Information;
import com.example.bruce.myapp.Presenter.InformationFragment.IInformationFragment;
import com.example.bruce.myapp.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/25/2017.
 */

public class MInformation_Fragment {
    private IInformationFragment callback;

    public MInformation_Fragment(IInformationFragment callback) {

        this.callback = callback;
    }

    public void handleLoadData(int location_ID, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String url_MoreInfomation="";
        if (sharedPreferences.getString("language","").equals("vi")) {
            url_MoreInfomation = Server.url_MoreInfomation;
        }
        else if(sharedPreferences.getString("language","").equals("en")){
            url_MoreInfomation = Server.url_MoreInfomation_en;
        }
        ArrayList<Information> informations = new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url_MoreInfomation+location_ID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response !=null)
                {
                    for(int i=0;i<response.length();i++)
                    {
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            Information moreInfo = new Information();
                            moreInfo.infomation_ID=jsonObject.getInt("id");
                            moreInfo.location_id=jsonObject.getInt("madiadiemdulich");
                            moreInfo.Info=jsonObject.getString("thongtin");
                            moreInfo.Image=jsonObject.getString("img");
                            informations.add(moreInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.getDataSuccess(informations);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.getDataFailed();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
