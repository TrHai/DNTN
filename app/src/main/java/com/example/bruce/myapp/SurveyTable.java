package com.example.bruce.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;

import com.example.bruce.myapp.View.Login.LoginActivity;

public class SurveyTable extends AppCompatActivity {
    Button btnContinue;
    AppCompatCheckBox cbxSea,cbxEcosystem,cbxCultural,cbxPark,cbxHistoricalSites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_table);
        initialize();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent target = new Intent(SurveyTable.this, LoginActivity.class);
                startActivity(target);
            }
        });
    }
    private void initialize(){
        btnContinue = findViewById(R.id.btnContinue);
        cbxSea= findViewById(R.id.cbxSea);
        cbxEcosystem =  findViewById(R.id.cbxEcosystem);
        cbxCultural =  findViewById(R.id.cbxCultural);
        cbxPark = findViewById(R.id.cbxPark);
        cbxHistoricalSites=  findViewById(R.id.cbxHistoricalSites);
    }

}
