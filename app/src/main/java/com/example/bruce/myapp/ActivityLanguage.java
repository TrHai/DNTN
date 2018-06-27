package com.example.bruce.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bruce.myapp.View.Login.LoginActivity;

import java.util.Locale;

public class ActivityLanguage extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    LinearLayout lnLanguageVN,lnLanguageEnglish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        lnLanguageVN=findViewById(R.id.lnLanguageVN);
        lnLanguageEnglish=findViewById(R.id.lnLanguageEnglish);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.getString("language","").isEmpty())
        {
            Locale locale=new Locale(sharedPreferences.getString("language",""));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            startActivity(getIntent());
            Intent intent=new Intent(ActivityLanguage.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        lnLanguageVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityLanguage.this, LoginActivity.class);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("language","vi");
                editor.commit();
                Locale locale=new Locale("vi");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                startActivity(intent);
                finish();
            }
        });
        lnLanguageEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityLanguage.this, LoginActivity.class);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("language","en");
                editor.commit();
                startActivity(intent);
                Locale locale=new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                startActivity(getIntent());
                finish();
            }
        });
    }
}
