package com.example.bruce.myapp.Data;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment {
    public String userID;
    public int locationID;
    public String userName;
    public String userImage;
    public String comment;
    public String date;
    public   int like = 0;
    public ArrayList<String> commentImages;

    public Comment() {

    }

    public Comment(String userID, int locationID, String userName, String userImage, String comment, String date, int like) {
        this.userID = userID;
        this.locationID = locationID;
        this.userName = userName;
        this.userImage = userImage;
        this.comment = comment;
        this.date = date;
        this.like = like;
    }

    public Comment(ArrayList<String> commentImages) {
        this.commentImages = commentImages;
    }

    public Comment(String userID, int locationID, String userName, String userImage, String comment, String date, int like, ArrayList<String> commentImages) {
        this.userID = userID;
        this.locationID = locationID;
        this.userName = userName;
        this.userImage = userImage;
        this.comment = comment;
        this.date = date;
        this.like = like;
        this.commentImages = commentImages;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
