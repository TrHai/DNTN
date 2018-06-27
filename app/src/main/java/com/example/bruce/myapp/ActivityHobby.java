package com.example.bruce.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.bruce.myapp.Adapter.HobbyAdapter;

public class ActivityHobby extends AppCompatActivity {
    GridView gridView;
    String placeName[]={
            "aưqewqe", "aưqe", "aqưe", "aqưe"
    };
    int  logoImage[]={
            R.drawable.america,R.drawable.asd,
            R.drawable.bag,R.drawable.button_action
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        HobbyAdapter hobbyAdapter=new HobbyAdapter(this,placeName,logoImage);
        gridView.setAdapter(hobbyAdapter);
    }
}
