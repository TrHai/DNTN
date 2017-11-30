package com.example.bruce.myapp.Presenter.InformationFragment;

import android.content.Context;

import com.example.bruce.myapp.Data.Information;
import com.example.bruce.myapp.Model.MInformation_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.IViewInformationFragment;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/25/2017.
 */

public class PInformationFragment implements IInformationFragment  {

    private IViewInformationFragment callbackToView;
    private MInformation_Fragment model = new MInformation_Fragment(this);

    public PInformationFragment(IViewInformationFragment callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedHanleLoadData(int location_ID, Context context){

        model.handleLoadData(location_ID,context);
    }

    @Override
    public ArrayList<Information> getDataSuccess(ArrayList<Information> informations) {
        return callbackToView.getDataSuccess(informations);
    }

    @Override
    public void getDataFailed() {
        callbackToView.getDataFailed();
    }
}
