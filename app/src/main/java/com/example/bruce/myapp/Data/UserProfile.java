package com.example.bruce.myapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BRUCE on 6/15/2017.
 */

public class UserProfile implements Parcelable {
    public String Email;
    public String Name;
    public String Image;
    public String Hobbie;
    public String Behavior;

    public UserProfile() {
    }

    public UserProfile(String email, String name, String image) {
        Email = email;
        Name = name;
        Image = image;
    }

    public UserProfile(String email, String name, String image, String hobbie) {
        this.Email = email;
        this.Name = name;
        this.Image = image;
        this.Hobbie = hobbie;
    }

    protected UserProfile(Parcel in) {
        Email = in.readString();
        Name = in.readString();
        Image = in.readString();
        Hobbie = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(Hobbie);
    }
}
