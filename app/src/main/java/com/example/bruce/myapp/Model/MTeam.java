package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Team.ITeam;
import com.google.firebase.auth.FirebaseAuth;
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

    public void handleAddListUser() {
        ArrayList<UserProfile> listUser=new ArrayList<>();
        DatabaseReference mData;
        mData= FirebaseDatabase.getInstance().getReference("TeamUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("member");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.getKey().contains(dataSnapshot2.getKey()) && dataSnapshot1.getKey()!= FirebaseAuth.getInstance().getCurrentUser().getUid())
                                {
                                    UserProfile constructer_userProfile=dataSnapshot2.getValue(UserProfile.class);
                                    listUser.add(constructer_userProfile);

//                                    teamAdapter.notifyDataSetChanged();

                                }
                            }
                            callback.GetListUser(listUser);
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
    }
}
