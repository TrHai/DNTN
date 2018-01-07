package com.example.bruce.myapp.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.BigMap.IBigMap;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    boolean area=true;

    public void handleShowTeamUser(double lat,double log, List<Marker> locationUser,List<Circle> circle) {
        {
            for (Marker maker :locationUser){
                maker.remove();
            }
            locationUser.clear();
            DatabaseReference mDataTeamUser = FirebaseDatabase.getInstance().getReference("TeamUser");
            DatabaseReference mDataCheckTeam = FirebaseDatabase.getInstance().getReference("CheckTeam");
            mDataCheckTeam.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists())
                    {
                        String idCaptain= dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Captain").getValue().toString();
                        GeoFire geoFire=new GeoFire(mDataTeamUser.child(idCaptain).child("member"));
                        //Đấy latLog của Thành viên trong team lên firebase
                        mDataTeamUser.child(idCaptain).child("member").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                {
                                    geoFire.getLocation(String.valueOf(dataSnapshot1.getKey()), new LocationCallback() {
                                        @Override
                                        public void onLocationResult(String key, final GeoLocation location) {
                                            if (location != null)
                                            {
                                                callback.addMakerMember(key,location,dataSnapshot1);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        // Đẩy location của User đang đăng nhập lên firebase
                        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(lat, log));
                        circleCaptain(geoFire,idCaptain,circle);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void circleCaptain(GeoFire geoFire,String idCaptain,List<Circle> circle) {
        geoFire.getLocation(idCaptain, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    if(circle!=null)
                    {
                        for (Circle cirlcee:circle)
                        {
                            cirlcee.remove();
                        }
                        circle.clear();
                    }
                    callback.circleMarkerCaptain(location,circle);
                    GeoQuery geoQuery;
                    geoQuery=geoFire.queryAtLocation(new GeoLocation(location.latitude,location.longitude),0.5f);
                    geoQuery.removeAllListeners();
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onKeyEntered(String key, final GeoLocation location) {

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid() == key) {
                                if (area == true) {
                                    callback.intoArea();
                                    area=false;
                                }
                            }
                        }
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onKeyExited(String key) {
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid() == key) {
                                if (area == false) {
                                    callback.outArea();
                                    area=true;
                                }
                            }
                        }
                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {
                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });
    }


}
