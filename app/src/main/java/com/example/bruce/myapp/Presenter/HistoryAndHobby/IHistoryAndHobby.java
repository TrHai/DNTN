package com.example.bruce.myapp.Presenter.HistoryAndHobby;

import com.example.bruce.myapp.Data.Tourist_Location;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/17/2017.
 */

public interface IHistoryAndHobby {
    void EnableGPS_API22();
    void EnableGPS_APILater22();
    ArrayList<Tourist_Location> GetLocationData(ArrayList<Tourist_Location> tourist_locations);
    String GetUserHistory(String history);
    String getUserBehavior(String behavior);
    ArrayList<Tourist_Location> GetUserHistoryLocation(ArrayList<Tourist_Location> tourist_locations);
    ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations);
}
