package com.example.bruce.myapp.Model;

import android.util.Log;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Team.ITeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Admin on 01/12/2017.
 */

public class MTeam {
    ITeam callback;

    public MTeam(ITeam callback) {
        this.callback = callback;
    }

    public void handleAddListUser(TeamAdapter adapter,ArrayList<UserProfile> listUser) {


        FirebaseDatabase.getInstance().getReference("CheckTeam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String idCaptain= dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Captain").getValue().toString();
                FirebaseDatabase.getInstance().getReference("TeamUser").child(idCaptain).child("member")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot1, String s) {
                                FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                                        {
                                            if(dataSnapshot1.getKey().contains(dataSnapshot2.getKey()) && !dataSnapshot1.getKey().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) )
                                            {
                                                UserProfile constructer_userProfile=dataSnapshot2.getValue(UserProfile.class);
                                                listUser.add(constructer_userProfile);
                                                // callback.GetListUser(listUser);
                                                Log.d("dsad","sad");
                                                adapter.notifyDataSetChanged();
//                                    teamAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot1) {
                                FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                                        {
                                            if(dataSnapshot1.getKey().contains(dataSnapshot2.getKey()) && !dataSnapshot1.getKey().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) )
                                            {
                                                UserProfile constructer_userProfile=dataSnapshot2.getValue(UserProfile.class);
                                                listUser.remove(0);
                                                // callback.GetListUser(listUser);
                                                Log.d("xzccx","sad");
                                                adapter.notifyDataSetChanged();
//                                    teamAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot1, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void handleRemoveMember(){

    }
}
