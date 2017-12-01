package com.example.bruce.myapp.View.Team;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Team.PTeam;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity implements IViewTeam {
    RecyclerView recyclerViewTeam;
    PTeam pTeam=new PTeam(this);
    TeamAdapter teamAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        initialize();
        pTeam.receivedAddListUser();
    }



    private void recyclerViewTeamCustom(RecyclerView recyclerView, TeamAdapter adapter, ArrayList<UserProfile> listUser) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new TeamAdapter(listUser, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initialize() {
        recyclerViewTeam=findViewById(R.id.recyclerViewTeam);


    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), HistoryAndHobbyActivity.class));
        super.onBackPressed();
    }

    @Override
    public ArrayList<UserProfile> GetListUser(ArrayList<UserProfile> listUser) {
        recyclerViewTeamCustom(recyclerViewTeam,teamAdapter,listUser);
        return null;
    }
}
