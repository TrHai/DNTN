package com.example.bruce.myapp.View.Information_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.InfoAdapter;
import com.example.bruce.myapp.Data.Information;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Presenter.InformationFragment.PInformationFragment;
import com.example.bruce.myapp.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by BRUCE on 8/30/2017.
 */

public class Information_Fragment extends android.support.v4.app.Fragment implements IViewInformationFragment {

    View mView;


    ListView listView;
    ArrayList<Tourist_Location> tls;
    InfoAdapter moreInfoAdapter;
    TextView txtBI;

    //MVP
    PInformationFragment pInformationFragment = new PInformationFragment(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_information,container,false);
        initialize(mView);

        //set title cho infor fragment
        txtBI.setText(tls.get(0).getBasicInfo());

        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tls = getActivity().getIntent().getParcelableArrayListExtra("tourist_location");
        pInformationFragment.receivedHanleLoadData(tls.get(0).getLocation_ID(),getApplicationContext());

    }

    private void initialize(View mView){

        listView = mView.findViewById(R.id.infoListView);
        txtBI = mView.findViewById(R.id.txtBasicInfo);
    }

    @Override
    public ArrayList<Information> getDataSuccess(ArrayList<Information> informations) {

        //set up listview
        moreInfoAdapter = new InfoAdapter(getActivity(), R.layout.adaper_information, informations);
        listView.setAdapter(moreInfoAdapter);

        return null;
    }

    @Override
    public void getDataFailed() {
        Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_SHORT).show();
    }
}
