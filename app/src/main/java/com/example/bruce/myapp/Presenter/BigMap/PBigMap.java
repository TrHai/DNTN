package com.example.bruce.myapp.Presenter.BigMap;

import com.example.bruce.myapp.Model.MBigMap;
import com.example.bruce.myapp.View.BigMap.IViewBigMap;

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
    @Override
    public void saveHistory() {

        callbackToView.saveHistory();
    }
}
