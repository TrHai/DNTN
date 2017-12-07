package com.example.bruce.myapp.Presenter.BigMap;

import com.example.bruce.myapp.Model.MBigMap;
import com.example.bruce.myapp.View.BigMap.IViewBigMap;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Created by BRUCE on 11/21/2017.
 */

public class PBigMap implements IBigMap {

    private MBigMap modelBigMap = new MBigMap(this);
    private IViewBigMap callbackToView;

    public PBigMap(IViewBigMap callbackToView) {

        this.callbackToView = callbackToView;
    }

    public void receivedSaveHistoryAndBehavior(int location_Id,int kindOfLocation, String userKey){

        modelBigMap.handleSaveHistoryAndBehavior(location_Id,kindOfLocation,userKey);
    }
    public void receivedShowTeamUser(double lat,double log, List<Marker> locationUsers,List<Circle> circle){
        modelBigMap.handleShowTeamUser(lat,log,locationUsers, circle);
    }
    @Override
    public void saveHistory() {

        callbackToView.saveHistory();
    }

    @Override
    public void addMakerMember(String key, GeoLocation location, DataSnapshot dataSnapshot) {
        callbackToView.addMakerMember(key,location,dataSnapshot);
    }

    @Override
    public void circleMarkerCaptain(GeoLocation location,List<Circle> circle) {
        callbackToView.circleMarkerCaptain(location,circle);
    }

    @Override
    public void intoArea() {
        callbackToView.intoArea();
    }

    @Override
    public void outArea() {
        callbackToView.outArea();
    }
}
