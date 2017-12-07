package com.example.bruce.myapp.Presenter.BigMap;

import android.content.Context;

import com.example.bruce.myapp.Data.UserProfile;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.Circle;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Created by BRUCE on 11/21/2017.
 */

public interface IBigMap {
    void saveHistory();
    void addMakerMember(String key, GeoLocation location, DataSnapshot dataSnapshot);
    void circleMarkerCaptain(GeoLocation location,List<Circle> circle);
    void intoArea();
    void outArea();
}
