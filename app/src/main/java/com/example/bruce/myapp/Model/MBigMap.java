package com.example.bruce.myapp.Model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.BigMap.IBigMap;
import com.example.bruce.myapp.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BRUCE on 11/21/2017.
 */

public class MBigMap {
    private IBigMap callback;
    private DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    public MBigMap() {

    }

    public MBigMap(IBigMap callback) {

        this.callback = callback;
    }

    public void handleSaveHistoryAndBehavior (int location_Id,int kindOfLocation,String userKey) {
        final boolean[] a = {false};
        FirebaseDatabase.getInstance().getReference().child("User").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //có if để cho chạy 1 lần vi hàm này dựa trên sư thây đổi trên firebase ma chạy
                if(a[0] == false){
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    if(userProfile.Hobbie.isEmpty() == true){
                        mData.child("User").child(userKey).child("Hobbie").setValue(location_Id);
                    }
                    else {
                        mData.child("User").child(userKey).child("Hobbie").setValue(userProfile.Hobbie + ","+ location_Id);
                    }
                    if (userProfile.Behavior.isEmpty() == true){

                        mData.child("User").child(userKey).child("Behavior").setValue(kindOfLocation);
                    }
                    else{
                        mData.child("User").child(userKey).child("Behavior").setValue(userProfile.Behavior + ","+ kindOfLocation);
                    }
                    a[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        callback.saveHistory();
    }

    public double Radius(LatLng mLocation, LatLng touristLocation) {
        double distance;
        double a = touristLocation.latitude - mLocation.latitude;
        double b = touristLocation.longitude - mLocation.longitude;
        double c = Math.sqrt(a*a + b*b);
        distance = c*(111.2)*1000;
        return distance;
    }
}
