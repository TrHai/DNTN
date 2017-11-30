package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.CommentFragment.ICommentFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/29/2017.
 */

public class MCommentFragment {
    DatabaseReference mData;

    ICommentFragment callback;

    public MCommentFragment(ICommentFragment callback) {
        this.callback = callback;
    }

    public void handleGetDataComment(int location_ID){
        ArrayList<Comment> comments = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Comment comment_contructor = dataSnapshot.getValue(Comment.class);
                comment_contructor.commentImages = new ArrayList<>();
                if(location_ID == comment_contructor.locationID) {

                    for(DataSnapshot commentImage: dataSnapshot.getChildren()) {
                        for (DataSnapshot child_of_CommentImage : commentImage.getChildren()) {
                            comment_contructor.commentImages.add(child_of_CommentImage.getValue().toString());
                        }
                    }
                    mData.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            UserProfile constructer_userProfile=dataSnapshot.getValue(UserProfile.class);
                            if(dataSnapshot.getKey().toString().equals(comment_contructor.userID))
                            {
                                comment_contructor.userImage = constructer_userProfile.Image;
                                comments.add(comment_contructor);
                                callback.getDataComment(comments);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
