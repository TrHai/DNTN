package com.example.bruce.myapp.View.Information_Fragment;

import com.example.bruce.myapp.Data.Information;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/25/2017.
 */

public interface IViewInformationFragment {
    ArrayList<Information> getDataSuccess(ArrayList<Information> informations);
    void getDataFailed();
}
